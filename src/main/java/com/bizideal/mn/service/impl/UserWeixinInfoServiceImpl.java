package com.bizideal.mn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizideal.mn.entity.UserWeixinInfo;
import com.bizideal.mn.mapper.UserWeixinInfoMapper;
import com.bizideal.mn.service.IUserWeixinInfoService;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月24日 下午3:39:14
 * @version 1.0
 */
@Service("UserWeixinInfoService")
public class UserWeixinInfoServiceImpl extends BaseOpBizImpl<UserWeixinInfo> implements IUserWeixinInfoService {

	@Autowired
	private UserWeixinInfoMapper userWeixinInfoMapper;

	@Override
	public int updateExpireTime(UserWeixinInfo weixinInfo) {
		return userWeixinInfoMapper.updateExpireTime(weixinInfo);
	}

	@Override
	public UserWeixinInfo selectByPhone(String phone) {
		return userWeixinInfoMapper.selectByPhone(phone);
	}

}
