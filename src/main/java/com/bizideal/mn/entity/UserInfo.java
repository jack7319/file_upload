package com.bizideal.mn.entity;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月1日 上午10:53:00
 * @version 1.0 用户基本信息类
 */
@Table(name = "user_info")
public class UserInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	@Id
	private String userId;
	private String phone;
	private String password;

	public UserInfo() {
		super();
	}

	public UserInfo(String userId, String phone, String password) {
		super();
		this.userId = userId;
		this.phone = phone;
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", phone=" + phone
				+ ", password=" + password + "]";
	}

}
