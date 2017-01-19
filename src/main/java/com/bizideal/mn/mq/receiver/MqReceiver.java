package com.bizideal.mn.mq.receiver;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.listener.SessionAwareMessageListener;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月28日 下午9:02:08
 * @version 1.0
 */
public class MqReceiver implements SessionAwareMessageListener<Message> {

	@Override
	public void onMessage(Message message, Session session) throws JMSException {
		try {
			ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
			final String ms = msg.getText();
			System.out.println("quenues1接收到消息:" + ms);
		} catch (Exception e) {
			e.printStackTrace();
			// 消费失败 保存到数据库 或者发送到mq的error队列
		}
	}

}
