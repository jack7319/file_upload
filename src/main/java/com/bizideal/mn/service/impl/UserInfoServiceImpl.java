package com.bizideal.mn.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizideal.mn.entity.UserInfo;
import com.bizideal.mn.entity.UserWeixinInfo;
import com.bizideal.mn.mapper.UserInfoMapper;
import com.bizideal.mn.service.IUserInfoService;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月24日 下午3:36:28
 * @version 1.0
 */
@Service("UserInfoService")
public class UserInfoServiceImpl extends BaseOpBizImpl<UserInfo> implements IUserInfoService {

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Override
	public List<UserWeixinInfo> login(UserInfo userInfo) {
		return userInfoMapper.login(userInfo);
	}

}
