/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1:3306
Source Server Version : 50553
Source Host           : 127.0.0.1:3306
Source Database       : leslie

Target Server Type    : MYSQL
Target Server Version : 50553
File Encoding         : 65001

Date: 2017-01-03 19:54:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user_info`
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `user_id` varchar(50) NOT NULL DEFAULT '',
  `phone` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL COMMENT 'MD5加密后的密码',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uq_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('58464d04bf07542148e95416', '13937988294', '3139464330cc99617ba6257f4a84a02b');
INSERT INTO `user_info` VALUES ('584687ccbf07541db40b0974', '18302575253', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `user_info` VALUES ('584687ccbf07541db40b097e', '13262852857', 'e10adc3949ba59abbe56e057f20f883e');

-- ----------------------------
-- Table structure for `user_weixin_info`
-- ----------------------------
DROP TABLE IF EXISTS `user_weixin_info`;
CREATE TABLE `user_weixin_info` (
  `user_id` varchar(50) DEFAULT NULL,
  `unionid` varchar(50) DEFAULT NULL,
  `openid` varchar(50) DEFAULT NULL,
  `subscribe` varchar(50) DEFAULT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `sex` varchar(10) DEFAULT NULL,
  `headimgurl` varchar(100) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `province` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `subscribe_time` bigint(50) DEFAULT NULL,
  `language` varchar(50) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `groupid` varchar(50) DEFAULT NULL,
  `weixin` varchar(50) DEFAULT NULL,
  `expire_time` bigint(11) DEFAULT NULL,
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_weixin_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_weixin_info
-- ----------------------------
INSERT INTO `user_weixin_info` VALUES ('58464d04bf07542148e95416', null, null, null, '123', '', '1', null, null, null, null, '1481002244129', null, null, null, null, null);
INSERT INTO `user_weixin_info` VALUES ('584687ccbf07541db40b0974', null, null, null, null, null, null, null, null, null, null, '1481017292731', null, null, null, null, null);
INSERT INTO `user_weixin_info` VALUES ('584687ccbf07541db40b097e', null, null, null, null, null, null, null, null, null, null, '1481017292733', null, null, null, null, '1482595648616');
