package com.bizideal.mn.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.bizideal.mn.utils.CookieUtils;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月24日 下午8:18:49
 * @version 1.0
 */
public class RememberInterceptor implements HandlerInterceptor {

	@Value("${cookie.cookieName}")
	/* cookie的键名 */
	private String cookieName;

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
		request.setAttribute("phone", split[0]);
		request.setAttribute("password", "1234567890");
		request.setAttribute("rememberMe", true);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
