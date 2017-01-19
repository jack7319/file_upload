package com.bizideal.mn.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.bizideal.mn.entity.UserWeixinInfo;

import tk.mybatis.mapper.common.Mapper;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月1日 下午3:10:55
 * @version 1.0
 */
public interface UserWeixinInfoMapper extends Mapper<UserWeixinInfo> {

	@Select("SELECT * FROM user_weixin_info uw LEFT JOIN user_info ui ON uw.user_id=ui.user_id WHERE uw.unionid = #{unionid}")
	public UserWeixinInfo selectByUnionid(String unionid);

	@Select("SELECT phone FROM user_info WHERE phone = #{phone}")
	public String selectPhone(String phone);

	@Select("SELECT * FROM user_weixin_info uw LEFT JOIN user_info ui ON uw.user_id=ui.user_id WHERE uw.user_id = #{userId}")
	public UserWeixinInfo selectByUserId(String userId);

	@Select("SELECT * FROM user_weixin_info uw LEFT JOIN user_info ui ON uw.user_id=ui.user_id WHERE ui.phone = #{phone}")
	public UserWeixinInfo selectByPhone(String phone);

	@Update("UPDATE user_weixin_info SET expire_time = #{expireTime} WHERE user_id = #{userId}")
	public int updateExpireTime(UserWeixinInfo weixinInfo);

}
