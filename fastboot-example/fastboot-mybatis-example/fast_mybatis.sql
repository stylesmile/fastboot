/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306_local
 Source Server Type    : MySQL
 Source Server Version : 100901
 Source Host           : localhost:3306
 Source Schema         : fast_mybatis

 Target Server Type    : MySQL
 Target Server Version : 100901
 File Encoding         : 65001

 Date: 20/07/2022 19:46:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `age` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '1', 1);

SET FOREIGN_KEY_CHECKS = 1;
