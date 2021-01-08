/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : xchat

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 29/05/2020 15:23:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `state` int(1) NOT NULL,
  `ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `attended_mode` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `equipment` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `created_time` timestamp(0) NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', NULL, 1, '60.29.18.117', 'Chrome / 77.0.3865.120', 'Windows 10', '2020-04-08 15:17:45');
INSERT INTO `user` VALUES (2, NULL, NULL, 1, '192.168.106.212', 'Chrome / 77.0.3865.120', 'Windows 10', '2019-10-11 16:53:49');
INSERT INTO `user` VALUES (3, NULL, NULL, 1, '127.0.0.1', 'Chrome 8 / 80.0.3987.163', 'Windows 10', '2020-04-08 12:06:19');

SET FOREIGN_KEY_CHECKS = 1;
