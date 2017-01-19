package com.bizideal.mn.entity;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月28日 下午8:39:16
 * @version 1.0
 */
public class QuenuesName {

	private String quenuesName;

	public QuenuesName() {
		super();
	}

	public QuenuesName(String quenuesName) {
		super();
		this.quenuesName = quenuesName;
	}

	public String getQuenuesName() {
		return quenuesName;
	}

	public void setQuenuesName(String quenuesName) {
		this.quenuesName = quenuesName;
	}

	@Override
	public String toString() {
		return "QuenuesName [quenuesName=" + quenuesName + "]";
	}

}
