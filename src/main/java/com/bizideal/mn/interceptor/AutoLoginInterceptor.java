package com.bizideal.mn.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.bizideal.mn.controller.UserInfoController;
import com.bizideal.mn.entity.UserWeixinInfo;
import com.bizideal.mn.service.IUserWeixinInfoService;
import com.bizideal.mn.utils.CookieUtils;
import com.bizideal.mn.utils.MD5;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月24日 下午9:38:25
 * @version 1.0
 */
public class AutoLoginInterceptor implements HandlerInterceptor {

	@Value("${cookie.cookieName}")
	/* cookie的键名 */
	private String cookieName;

	@Value("${cookie.expireTime}")
	/* cookie过期时间设置为7天 */
	private long expireTime;

	@Autowired
	private IUserWeixinInfoService userWeixinInfoService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String base64Str = CookieUtils.getCookieValue(request, cookieName, true);
		if (StringUtils.isBlank(base64Str)) {
			return true;
		}
		// 第一步，对cookie中的值进行base64解密
		String md5Str = new String(Base64.decodeBase64(base64Str));
		String[] split = StringUtils.split(md5Str, ":");
		if (split.length != 3) {
			return true;
		}
		// 第二步，过期时间与系统当前时间比较，是否过期
		if (Long.valueOf(split[1]) < System.currentTimeMillis()) {
			return true;
		}
		// 第三步，通过用户名取到数据库查询用户
		UserWeixinInfo weixinInfo = userWeixinInfoService.selectByPhone(split[0]);
		if (null == weixinInfo) {
			return true;
		}
		String md5Str2 = MD5
				.getMD5(weixinInfo.getPhone() + weixinInfo.getPassword() + weixinInfo.getExpireTime() + cookieName);
		if (split[2].equals(md5Str2)) {
			// 验证成功
			request.getSession().setAttribute("user", weixinInfo);
			// 处理cookie
			UserInfoController.dealCookie(userWeixinInfoService, "true", cookieName, expireTime, weixinInfo, request,
					response);
			response.sendRedirect("user/loginSuccess");
		} else {
			request.setAttribute("phone", weixinInfo.getPhone());
			return true;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
