package com.bizideal.mn.entity;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月28日 下午8:46:21
 * @version 1.0
 */
public class MqMessage {

	/* 要发送到的目的的 */
	private String quenueName;
	/* 发送的具体内容，json字符串 */
	private String message;

	public MqMessage() {
		super();
	}

	public MqMessage(String quenueName, String message) {
		super();
		this.quenueName = quenueName;
		this.message = message;
	}

	public String getQuenueName() {
		return quenueName;
	}

	public void setQuenueName(String quenueName) {
		this.quenueName = quenueName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MqMessage [quenueName=" + quenueName + ", message=" + message + "]";
	}

}
