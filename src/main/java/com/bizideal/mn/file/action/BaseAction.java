package com.bizideal.mn.file.action;


//import org.apache.catalina.connector.ClientAbortException;

public class BaseAction {

	/**	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
//	public void prepare() throws Exception {
//		// TODO Auto-generated method stub
//		getResponse().setHeader("Cache-Control", "no-cache");
//		getResponse().setHeader("Pragma", "no-cache");
//		getResponse().setDateHeader("Expires", 0);
//	}

	/**
	 * Response取得
	 * 
	 * @return
	 */
//	public HttpServletResponse getResponse() {
//		return ServletActionContext.getResponse();
//	}
//
//	public void outJson(Object obj) {
//		outJsonToResponse(obj, "application/Json;charset=UTF-8");
//	}

	public AjaxResultDto outJson(String errcode, String errmsg, Object obj) {
		AjaxResultDto resultDto = new AjaxResultDto();
		resultDto.setErrcode(errcode);
		resultDto.setErrmsg(errmsg);
		resultDto.setObj(obj);
		return resultDto;
	}

	/*public void outJsonToResponse(Object obj, String contentType) {
		getResponse().setContentType(contentType);
		AjaxResultDto resultDto = new AjaxResultDto();
		resultDto.setObjData(obj);
		if ((this.getActionErrors() != null && this.getActionErrors().size() > 0)
				|| (this.getFieldErrors() != null && this.getFieldErrors()
						.size() > 0)) {
			resultDto.setResultCode(WebConstants.AJAX_RESULT_ERROR);
		} else {
			resultDto.setResultCode(WebConstants.AJAX_RESULT_OK);
		}
		// データなし
		resultDto.setMessage(getText(""));
		outObjectToResponse(resultDto);
		resultDto = null;
	}

	public void outJsonToResponse(String resultCode, Object obj,
			String contentType) {
		getResponse().setContentType(contentType);
		AjaxResultDto resultDto = new AjaxResultDto();
		resultDto.setResultCode(resultCode);
		resultDto.setObjData(obj);
		// データなし
		resultDto.setMessage(getText(""));
		outObjectToResponse(resultDto);
		resultDto = null;
	}*/

	/**
	 *
	 * <pre>
	 * outString
	 * </pre>
	 *
	 * @param str
	 */
	/*private void outObjectToResponse(Object object) {
		try {
			PrintWriter out = getResponse().getWriter();
			WebConstants.JSON_WRITER.writeValue(out, object);
			out.flush();
			object = null;
		} catch (Exception e) {
			throw new SystemException(e);
		}

	}*/

	/**
	 * actionError
	 *
	 * @param errorNo
	 *            メッセージのキー
	 */
//	@Override
//	public void addActionError(String errorNo) {
//		super.addActionError(getText(errorNo));
//	}

	/**
	 * actionError
	 *
	 * @param errorNo
	 *            メッセージのキー
	 * @param param
	 *            メッセージのパラメータ
	 */
//	public void addActionError(String errorNo, String[] param) {
//		if (param == null) {
//			super.addActionError(getText(errorNo));
//		} else {
//			super.addActionError(getText(errorNo, param));
//		}
//	}

	/**
	 * actionErrorメッセージの内容を追加
	 *
	 * @param errorMessaes
	 *            メッセージの内容
	 */
//	public void addActionErrorMessages(String errorMessaes) {
//		super.addActionError(StringUtils.nvl(errorMessaes));
//	}

	/**
	 * <pre>
	 * フィールドのエラーメッセージを追加する
	 * 			パラメータある
	 * </pre>
	 *
	 * @param fialdName
	 *            フィールド名
	 * @param param
	 *            メッセージのパラメータ
	 * @param msgkey
	 *            メッセージのキー
	 */
//	public void addFieldError(String fieldName, String[] param, String Mskey) {
//		if (param == null) {
//			super.addFieldError(fieldName, getText(Mskey));
//		} else {
//			super.addFieldError(fieldName, getText(Mskey, param));
//		}
//
//	}

//	public void addFieldError(String fieldName, String Mskey) {
//		super.addFieldError(fieldName, getText(Mskey));
//	}

}
