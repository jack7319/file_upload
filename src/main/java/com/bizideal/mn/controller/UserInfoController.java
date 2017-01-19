package com.bizideal.mn.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bizideal.mn.entity.HttpResult;
import com.bizideal.mn.entity.UserInfo;
import com.bizideal.mn.entity.UserWeixinInfo;
import com.bizideal.mn.service.IUserInfoService;
import com.bizideal.mn.service.IUserWeixinInfoService;
import com.bizideal.mn.utils.CookieUtils;
import com.bizideal.mn.utils.HttpClientUtils;
import com.bizideal.mn.utils.MD5;
import com.bizideal.mn.utils.ObjectId;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月24日 下午3:40:32
 * @version 1.0
 */
@Controller
@RequestMapping("/user")
public class UserInfoController {

	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private IUserWeixinInfoService userWeixinInfoService;

	@Value("${cookie.cookieName}")
	/* cookie的键名 */
	private String cookieName;

	@Value("${cookie.expireTime}")
	/* cookie过期时间设置为7天 */
	private long expireTime;

	@RequestMapping("/login")
	public ModelAndView login(UserInfo userInfo, String isRemember, String rememberMe, ModelAndView modelAndView,
			HttpServletRequest request, HttpServletResponse response) {
		if ("true".equals(isRemember)) {
			// 用户记住密码登陆
			return loginByRememberMe(rememberMe, modelAndView, request, response);
		} else {
			// 用户通过密码登陆
			return loginByPwd(userInfo, rememberMe, modelAndView, request, response);
		}
	}

	/* 通过密码登陆 */
	public ModelAndView loginByPwd(UserInfo userInfo, String rememberMe, ModelAndView modelAndView,
			HttpServletRequest request, HttpServletResponse response) {
		userInfo.setPassword(MD5.getMD5(userInfo.getPassword()));
		List<UserWeixinInfo> weixinInfos = userInfoService.login(userInfo);
		if (null != weixinInfos && weixinInfos.size() > 0) {
			UserWeixinInfo weixinInfo = weixinInfos.get(0);
			request.getSession().setAttribute("user", weixinInfo);
			dealCookie(userWeixinInfoService, rememberMe, cookieName, expireTime, weixinInfo, request, response);
			modelAndView.addObject("message", "登陆成功");
			modelAndView.setViewName("login_success");
		} else {
			modelAndView.setViewName("login");
			modelAndView.addObject("message", "用户名或密码不正确");
			modelAndView.addObject("phone", userInfo.getPhone());
		}
		return modelAndView;
	}

	/* 通过记住密码登陆 */
	public ModelAndView loginByRememberMe(String rememberMe, ModelAndView modelAndView, HttpServletRequest request,
			HttpServletResponse response) {
		String base64Str = CookieUtils.getCookieValue(request, cookieName, true);
		if (StringUtils.isBlank(base64Str)) {
			modelAndView.addObject("message", "cookie过期1");
			modelAndView.setViewName("login");
			return modelAndView;
		}
		// 第一步，对cookie中的值进行base64解密
		String md5Str = new String(Base64.decodeBase64(base64Str));
		String[] split = StringUtils.split(md5Str, ":");
		if (split.length != 3) {
			modelAndView.addObject("message", "cookie过期2");
			modelAndView.setViewName("login");
			return modelAndView;
		}
		// 第二步，过期时间与系统当前时间比较，是否过期
		if (Long.valueOf(split[1]) < System.currentTimeMillis()) {
			modelAndView.addObject("message", "cookie过期3");
			modelAndView.setViewName("login");
			return modelAndView;
		}
		// 第三步，通过用户名取到数据库查询用户
		UserWeixinInfo weixinInfo = userWeixinInfoService.selectByPhone(split[0]);
		if (null == weixinInfo) {
			modelAndView.addObject("message", "用户不存在");
			modelAndView.setViewName("login");
			return modelAndView;
		}
		String md5Str2 = MD5
				.getMD5(weixinInfo.getPhone() + weixinInfo.getPassword() + weixinInfo.getExpireTime() + cookieName);
		if (split[2].equals(md5Str2)) {
			// 验证成功
			request.getSession().setAttribute("user", weixinInfo);
			// 处理cookie
			dealCookie(userWeixinInfoService, rememberMe, cookieName, expireTime, weixinInfo, request, response);
			modelAndView.addObject("message", "登陆成功");
			modelAndView.setViewName("login_success");
			return modelAndView;
		} else {
			modelAndView.addObject("message", "密码不正确");
			modelAndView.addObject("phone", weixinInfo.getPhone());
			modelAndView.setViewName("login");
			return modelAndView;
		}
	}

	@RequestMapping("/loginSuccess")
	public String loginSuccess() {
		return "login_success";
	}

	/* 登陆成功后，处理cookie操作抽离 */
	public static void dealCookie(IUserWeixinInfoService userWeixinInfoService, String rememberMe, String cookieName,
			long expireTime, UserWeixinInfo weixinInfo, HttpServletRequest request, HttpServletResponse response) {
		if ("true".equals(rememberMe)) {
			// 用户再次选择记住密码
			long nextExpireTime = System.currentTimeMillis() + expireTime;
			weixinInfo.setExpireTime(nextExpireTime);
			userWeixinInfoService.updateExpireTime(weixinInfo);
			// 将密码写入cookie
			// 第一步：手机号+MD5密码+记住密码过期时间+webKey 得到MD5加密前字符串
			String str = weixinInfo.getPhone() + weixinInfo.getPassword() + nextExpireTime + cookieName;
			// 第二步：得到MD5明文字符串
			String md5Str = MD5.getMD5(str);
			// 第三步：得到Base64加密后的值
			String base64Str = Base64
					.encodeBase64String((weixinInfo.getPhone() + ":" + nextExpireTime + ":" + md5Str).getBytes());
			// 第四步：储存到cookie中,设置的过期时间是秒
			CookieUtils.setCookie(request, response, cookieName, base64Str, (int) expireTime / 1000, true);
		} else {
			// 将cookie中的密码信息去掉
			CookieUtils.deleteCookie(request, response, cookieName);
		}
	}

	@RequestMapping("/register")
	@ResponseBody
	public HttpResult register(@RequestBody UserInfo userInfo, HttpServletRequest request) {
		userInfo.setUserId(ObjectId.get().toString());
		userInfo.setPassword(MD5.getMD5(userInfo.getPassword()));
		userInfoService.save(userInfo);
		return HttpResult.toJson(0, "ok", userInfo);
	}

	public static void main(String[] args) throws InterruptedException {
		String encodeBase64 = Base64.encodeBase64String("921228".getBytes());
		System.out.println(new String());
		System.out.println(MD5.getMD5("921228"));

		String test = "1:2:3";
		String s1 = Base64.encodeBase64String(test.getBytes());
		String s2 = new String(Base64.decodeBase64(s1));
		System.out.println(s1);
		System.out.println(s2);

		System.out.println(7 * 24 * 3600);
		
		System.out.println(2<<3);
	}
}
