package com.bizideal.mn.entity;

import java.io.Serializable;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class BaseEntity implements Serializable {
	private static final long serialVersionUID = -1449875354570226204L;
	@Transient
	protected Integer pageNum;
	@Transient
	protected Integer pageSize;

	@JsonUnwrapped
	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	@JsonUnwrapped
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
