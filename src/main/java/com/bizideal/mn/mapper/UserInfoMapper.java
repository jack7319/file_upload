package com.bizideal.mn.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.bizideal.mn.entity.UserInfo;
import com.bizideal.mn.entity.UserWeixinInfo;

import tk.mybatis.mapper.common.Mapper;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月1日 下午2:40:45
 * @version 1.0
 */
public interface UserInfoMapper extends Mapper<UserInfo> {

	@Update("UPDATE user_info SET phone = #{phone} WHERE user_id = #{userId}")
	public void updatePhone(@Param("userId") String userId, @Param("phone") String phone);

	@Select("SELECT * FROM user_info ui LEFT JOIN user_weixin_info uw ON ui.user_id = uw.user_id WHERE ui.phone = #{phone} AND ui.`password` = #{password}")
	public List<UserWeixinInfo> login(UserInfo userInfo);

	@Update("UPDATE user_info SET password = #{password} WHERE user_id = #{userId} ")
	public int updatePassword(@Param("userId") String userId, @Param("password") String password);

	@Select("SELECT * FROM user_info ui LEFT JOIN user_weixin_info uw ON ui.user_id = uw.user_id WHERE ui.phone = #{phone}")
	public UserWeixinInfo selectByPhone(String phone);

}
