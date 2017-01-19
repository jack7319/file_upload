package com.bizideal.mn.file.action;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月21日 上午11:23:02
 * @version 1.0
 */
public class AjaxResultDto {

	private String errcode;
	private String errmsg;
	private Object obj;

	public AjaxResultDto() {
		super();
	}

	public AjaxResultDto(String errcode, String errmsg, Object obj) {
		super();
		this.errcode = errcode;
		this.errmsg = errmsg;
		this.obj = obj;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
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
		return "AjaxResultDto [errcode=" + errcode + ", errmsg=" + errmsg
				+ ", obj=" + obj + "]";
	}

}
