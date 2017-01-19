package com.bizideal.mn.entity;

public class BaseOpEntity extends BaseEntity {
	private static final long serialVersionUID = -4699926016484251329L;

	/** 创建人 **/
	protected String createUser;

	/** 创建时间 **/
	protected Long createTime;

	/** 更新人 **/
	protected String updateUser;

	/** 更新时间 **/
	protected Long updateTime;

	/** 删除标识(0:正常,1:删除,2:审核) **/
	protected String delFlag;

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

}
