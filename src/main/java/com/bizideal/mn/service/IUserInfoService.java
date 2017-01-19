package com.bizideal.mn.service;

import java.util.List;

import com.bizideal.mn.entity.UserInfo;
import com.bizideal.mn.entity.UserWeixinInfo;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月24日 下午3:34:50
 * @version 1.0
 */
public interface IUserInfoService extends BaseOpBiz<UserInfo> {
	public List<UserWeixinInfo> login(UserInfo userInfo);
}
