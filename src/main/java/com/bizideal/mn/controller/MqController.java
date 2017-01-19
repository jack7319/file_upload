package com.bizideal.mn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizideal.mn.entity.HttpResult;
import com.bizideal.mn.entity.MqMessage;
import com.bizideal.mn.mq.sender.MqSender;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月28日 下午9:13:35
 * @version 1.0
 */
@Controller
@RequestMapping("/mq")
public class MqController {

	@Autowired
	private MqSender mqSender;

	@RequestMapping("/t")
	public String t() {
		return "mq";
	}

	/**
	 * 统一的发送站
	 * 
	 * @param quenuesName
	 * @param message
	 * @return
	 */
	@RequestMapping("/{quenuesName}/{message}")
	@ResponseBody
	public HttpResult send(@PathVariable("quenuesName") String quenuesName, @PathVariable("message") String message) {
		return mqSender.send(new MqMessage(quenuesName, message));
	}
}
