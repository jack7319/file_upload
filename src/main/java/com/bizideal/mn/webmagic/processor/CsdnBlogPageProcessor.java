package com.bizideal.mn.webmagic.processor;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import com.bizideal.mn.webmagic.dao.CsdnBlogDao;
import com.bizideal.mn.webmagic.entity.CsdnBlog;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * CSDN博客爬虫
 * 
 * @describe 可以爬取指定用户的csdn博客所有文章，并保存到数据库中。
 * @date 2016-4-30
 * 
 * @author steven
 * @csdn qq598535550
 * @website lyf.soecode.com
 */
public class CsdnBlogPageProcessor implements PageProcessor {

	private static String username = "qq598535550";// 设置csdn用户名
	private static int size = 0;// 共抓取到的文章数量

	// 抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
	private Site site;

	@Override
	public Site getSite() {
		// site定义抽取配置，以及开始url等
		if (site == null) {
			site = Site.me().setDomain("blog\\.csdn\\.net/").addStartUrl("blog\\.csdn\\.net/").setUserAgent(
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
		}
		return site;
	}

	@Override
	// process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
	public void process(Page page) {
		// 列表页
		if (!page.getUrl().regex("http://blog\\.csdn\\.net/" + username + "/article/details/\\d+").match()) {
			// 添加所有文章页
			page.addTargetRequests(page.getHtml().xpath("//dl[@class='list_c clearfix']").links()// 限定文章列表获取区域
					.regex("/" + username + "/article/details/\\d+")
					.replace("/" + username + "/", "http://blog.csdn.net/" + username + "/")// 巧用替换给把相对url转换成绝对url
					.all());
			// 添加其他列表页
			page.addTargetRequests(page.getHtml().xpath("//div[@id='papelist']").links()// 限定其他列表页获取区域
					.regex("/" + username + "/article/list/\\d+")
					.replace("/" + username + "/", "http://blog.csdn.net/" + username + "/")// 巧用替换给把相对url转换成绝对url
					.all());
			// 文章页
		} else {
			size++;// 文章数量加1
			// 用CsdnBlog类来存抓取到的数据，方便存入数据库
			CsdnBlog csdnBlog = new CsdnBlog();
			// 设置编号
			csdnBlog.setId(Integer.parseInt(
					page.getUrl().regex("http://blog\\.csdn\\.net/" + username + "/article/details/(\\d+)").get()));
			// 设置标题
			csdnBlog.setTitle(page.getHtml().xpath("//h3[@class='list_c_t']/a/text()").get());
			// 设置日期
			String year = page.getHtml().xpath("//div[@class='date_t']/span/text()").get();
			String month = page.getHtml().xpath("//div[@class='date_t']/em/text()").get();
			String day = page.getHtml().xpath("//div[@class='date_b']/html()").get();
			csdnBlog.setDate(toDate(year, month, day));
			// 设置标签（可以有多个，用,来分割）
			csdnBlog.setTags(listToString(page.getHtml()
					.xpath("//div[@class='article_l']/span[@class='link_categories']/a/allText()").all()));
			// 设置类别（可以有多个，用,来分割）
			csdnBlog.setCategory(listToString(page.getHtml().xpath("//p[@class='detail_p']/label/em/text()").all()));
			// 设置阅读人数
			csdnBlog.setView(Integer
					.parseInt(page.getHtml().xpath("//p[@class='read_r']/label[1]/span/text()").regex("\\d++").get()));
			// 设置评论人数
			csdnBlog.setComments(Integer.parseInt(
					page.getHtml().xpath("//p[@class='read_r']/label[2]/span/text()").regex("\\d+").get()));
			// 设置是否原创
			csdnBlog.setCopyright(page.getHtml().regex("set_old").match() ? 1 : 0);
			// 把对象存入数据库
			try {
				new CsdnBlogDao().add(csdnBlog);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// 把对象输出控制台
			System.out.println(csdnBlog);
		}
	}

	// 把list转换为string，用,分割
	public static String listToString(List<String> stringList) {
		if (stringList == null) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		boolean flag = false;
		for (String string : stringList) {
			if (flag) {
				result.append(",");
			} else {
				flag = true;
			}
			result.append(string);
		}
		return result.toString();
	}

	/* 日期处理 */
	public static String toDate(String year, String month, String day) {
		switch (month) {
		case "一月":
			month = "01";
			break;
		case "二月":
			month = "02";
			break;
		case "三月":
			month = "03";
			break;
		case "四月":
			month = "04";
			break;
		case "五月":
			month = "05";
			break;
		case "六月":
			month = "06";
			break;
		case "七月":
			month = "07";
			break;
		case "八月":
			month = "08";
			break;
		case "九月":
			month = "09";
			break;
		case "十月":
			month = "10";
			break;
		case "十一月":
			month = "11";
			break;
		case "十二月":
			month = "12";
			break;
		}
		return year + "-" + month + "-" + day;

	}

	public static void main(String[] args) {
		long startTime, endTime;
		System.out.println("【爬虫开始】请耐心等待一大波数据到你碗里来...");
		startTime = System.currentTimeMillis();
		// 从用户博客首页开始抓，开启5个线程，启动爬虫
		Spider.create(new CsdnBlogPageProcessor()).addUrl("http://blog.csdn.net/" + username).thread(5).run();
		endTime = System.currentTimeMillis();
		System.out.println("【爬虫结束】共抓取" + size + "篇文章，耗时约" + ((endTime - startTime) / 1000) + "秒，已保存到数据库，请查收！");
	}
}
