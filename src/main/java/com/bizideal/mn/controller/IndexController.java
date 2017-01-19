package com.bizideal.mn.controller;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizideal.mn.entity.HttpResult;
import com.bizideal.mn.entity.UserWeixinInfo;
import com.bizideal.mn.utils.ImageUtil;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月24日 下午4:17:02
 * @version 1.0
 */
@Controller
@RequestMapping("/")
public class IndexController {

	@RequestMapping("/")
	public String index() {
		return "redirect:login";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/signIn")
	public String signIn() {
		return "signIn";
	}

	@RequestMapping("/toLoginByCode")
	public String toLoginByCode() {
		return "loginByCode";
	}

	// 获取图形验证码
	@RequestMapping("/createImage")
	public void createImage(HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		Map<String, BufferedImage> imageMap = ImageUtil.createImage();
		String code = imageMap.keySet().iterator().next();
		request.getSession().setAttribute("imageCode", code);
		BufferedImage image = imageMap.get(code);
		response.setContentType("image/jpeg");
		OutputStream ops = response.getOutputStream();
		ImageIO.write(image, "jpeg", ops);
		ops.close();
	}

	@RequestMapping("/loginByCode")
	@ResponseBody
	public HttpResult loginByCode(HttpServletRequest request,
			@RequestBody UserWeixinInfo weixinInfo) {
		String code = (String) request.getSession().getAttribute("imageCode");
		if (StringUtils.isBlank(code)
				|| !code.equalsIgnoreCase(weixinInfo.getMsgCode())) {
			// 验证码匹配失败
			return HttpResult.toJson(1, "验证码错误", null);
		}
		if ("liulq".equals((weixinInfo.getRealName()))
				&& "123456".equals(weixinInfo.getPassword())) {
			// 登陆成功
			return HttpResult.toJson(0, "登陆成功", null);
		}
		return HttpResult.toJson(1, "用户名或密码错误", null);
	}
}
