package com.bizideal.mn.entity;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月24日 下午4:00:45
 * @version 1.0
 */
public class HttpResult {

	private int errcode;
	private String errmsg;
	private Object obj;

	public HttpResult() {
		super();
	}

	public HttpResult(int errcode, String errmsg, Object obj) {
		super();
		this.errcode = errcode;
		this.errmsg = errmsg;
		this.obj = obj;
	}

	public static HttpResult toJson(int errcode, String errmsg, Object obj) {
		return new HttpResult(errcode, errmsg, obj);
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		return "HttpResult [errcode=" + errcode + ", errmsg=" + errmsg + ", obj=" + obj + "]";
	}

}
