package com.bizideal.mn.service;

import com.bizideal.mn.entity.UserWeixinInfo;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月24日 下午3:38:44
 * @version 1.0
 */
public interface IUserWeixinInfoService extends BaseOpBiz<UserWeixinInfo> {

	public int updateExpireTime(UserWeixinInfo weixinInfo);

	public UserWeixinInfo selectByPhone(String phone);
}
