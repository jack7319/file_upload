package com.bizideal.mn.mq.sender;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.bizideal.mn.entity.HttpResult;
import com.bizideal.mn.entity.MqMessage;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月28日 下午8:36:04
 * @version 1.0
 */
@Component("mqSender")
public class MqSender {
	@Autowired
	private JmsTemplate jmsTemplate;
	@Value("${mq.quenuesNames}")
	private String names;

	public HttpResult send(MqMessage mqMessage) {
		try {
			final String qName = mqMessage.getQuenueName();
			final String message = mqMessage.getMessage();

			String[] quenues = new String[] {};
			if (names.contains(",")) {
				quenues = names.split(",");
			} else {
				quenues[0] = names;
			}

			for (String name : quenues) {
				if (name.equals(qName)) {
					try {
						jmsTemplate.send(name, new MessageCreator() {
							@Override
							public Message createMessage(Session session) throws JMSException {
								return session.createTextMessage(message);
							}
						});
						return HttpResult.toJson(0, "ok", "ok");
					} catch (Exception e) {
						e.printStackTrace();
						// 报名信息发送到mq失败
						return HttpResult.toJson(400, "exception", e.getMessage());
					}
				}

			}
			return HttpResult.toJson(500, "未知队列名称", mqMessage.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			// 报名信息发送到mq失败
			return HttpResult.toJson(400, "exception", e.getMessage());
		}
	}
}
