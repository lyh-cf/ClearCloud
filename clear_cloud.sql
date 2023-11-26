/*
 Navicat Premium Data Transfer

 Source Server         : pz
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Host           : 47.115.217.108:3306
 Source Schema         : clear_cloud

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : 65001

 Date: 26/11/2023 21:32:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect`  (
  `pk_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL COMMENT 'userId',
  `video_id` int NULL DEFAULT NULL COMMENT 'videoId',
  `author_id` int NULL DEFAULT NULL COMMENT 'authorId',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`pk_id`) USING BTREE,
  UNIQUE INDEX `unique_index_video_id`(`user_id` ASC, `video_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES (16, 54, 3, NULL, '2023-11-07 17:11:05');
INSERT INTO `collect` VALUES (17, 56, 3, NULL, '2023-11-07 20:51:58');
INSERT INTO `collect` VALUES (18, 56, 31, NULL, '2023-11-07 20:54:39');
INSERT INTO `collect` VALUES (19, 56, 24, NULL, '2023-11-07 20:59:20');
INSERT INTO `collect` VALUES (20, 56, 6, NULL, '2023-11-07 21:09:23');
INSERT INTO `collect` VALUES (21, 56, 30, NULL, '2023-11-07 21:41:34');
INSERT INTO `collect` VALUES (22, 56, 22, NULL, '2023-11-07 21:46:23');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `pk_comment_id` int NOT NULL AUTO_INCREMENT COMMENT '评论Id',
  `parent_comment_id` int NULL DEFAULT NULL COMMENT '父级评论Id，若无则为-1',
  `video_id` int NULL DEFAULT NULL COMMENT '所属的视频Id',
  `user_id` int NULL DEFAULT NULL COMMENT '评论者UserId',
  `replied_user_id` int NULL DEFAULT NULL COMMENT '被评论者UserId',
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评论内容',
  `comment_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论附带图片URL',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`pk_comment_id`) USING BTREE,
  UNIQUE INDEX `unique_index_video_id`(`video_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`  (
  `pk_id` int NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `user_id` int NULL DEFAULT NULL COMMENT 'UserId',
  `follow_id` int NULL DEFAULT NULL COMMENT '所关注的用户Id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`pk_id`) USING BTREE,
  UNIQUE INDEX `multiple_column_index`(`user_id` ASC, `follow_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of follow
-- ----------------------------
INSERT INTO `follow` VALUES (26, 56, 55, NULL);
INSERT INTO `follow` VALUES (27, 56, 57, NULL);
INSERT INTO `follow` VALUES (28, 56, 58, NULL);
INSERT INTO `follow` VALUES (29, 59, 56, NULL);
INSERT INTO `follow` VALUES (30, 60, 56, NULL);
INSERT INTO `follow` VALUES (31, 61, 56, NULL);

-- ----------------------------
-- Table structure for like
-- ----------------------------
DROP TABLE IF EXISTS `like`;
CREATE TABLE `like`  (
  `pk_id` int NOT NULL COMMENT 'id',
  `user_id` int NULL DEFAULT NULL COMMENT 'userId',
  `video_id` int NULL DEFAULT NULL COMMENT 'videoId',
  `author_id` int NULL DEFAULT NULL COMMENT 'authorId',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`pk_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of like
-- ----------------------------

-- ----------------------------
-- Table structure for user_count
-- ----------------------------
DROP TABLE IF EXISTS `user_count`;
CREATE TABLE `user_count`  (
  `pk_user_id` int NOT NULL COMMENT 'UserId',
  `follow_count` int NULL DEFAULT 0 COMMENT '关注数',
  `fan_count` int NULL DEFAULT 0 COMMENT '粉丝数',
  `liked_count` int NULL DEFAULT 0 COMMENT '获赞数',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`pk_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_count
-- ----------------------------
INSERT INTO `user_count` VALUES (54, 0, 0, 0, '2023-11-05 14:19:43', NULL);
INSERT INTO `user_count` VALUES (55, 0, 0, 0, '2023-11-05 16:51:09', NULL);
INSERT INTO `user_count` VALUES (56, 0, 0, 0, '2023-11-07 15:08:55', NULL);
INSERT INTO `user_count` VALUES (57, 0, 0, 0, '2023-11-07 20:29:05', NULL);
INSERT INTO `user_count` VALUES (58, 0, 0, 0, '2023-11-07 20:29:11', NULL);
INSERT INTO `user_count` VALUES (59, 0, 0, 0, '2023-11-07 20:29:15', NULL);
INSERT INTO `user_count` VALUES (60, 0, 0, 0, '2023-11-07 20:29:21', NULL);
INSERT INTO `user_count` VALUES (61, 0, 0, 0, '2023-11-07 20:29:25', NULL);

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `pk_user_id` int NOT NULL AUTO_INCREMENT COMMENT 'UserId',
  `user_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '清云-user' COMMENT '昵称',
  `pass_word` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'http://s32vad0na.bkt.clouddn.com/default_avatar.jpeg' COMMENT '头像URL',
  `signature` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '签名',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`pk_user_id`) USING BTREE,
  UNIQUE INDEX `unique_index_email`(`user_email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (54, '3157904941@qq.com', '自由人', '207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb', 'http://s32vad0na.bkt.clouddn.com/54-1699355399201-QQ图片20220517131539.jpg', '这个牢终于坐完了', NULL, '2023-11-07 19:10:06');
INSERT INTO `user_info` VALUES (55, '3102189885@qq.com', '清云-user', 'c68ac63173fcfc537bf22f19a425977029d7dd35ddc5d76b36e58af222dfda39', 'http://s32vad0na.bkt.clouddn.com/default_avatar.jpeg', '加油！奥利给！', '2023-11-05 16:51:09', '2023-11-07 20:47:37');
INSERT INTO `user_info` VALUES (56, '123456@qq.com', '自由', '207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb', 'http://s32vad0na.bkt.clouddn.com/56-1699365638769-QQ图片20220517131539.jpg', '加油加油', '2023-11-07 15:08:55', '2023-11-07 22:00:48');
INSERT INTO `user_info` VALUES (57, '123@qq.com', '清云-user', '207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb', 'http://s32vad0na.bkt.clouddn.com/default_avatar.jpeg', '希望晋级！', '2023-11-07 20:29:05', '2023-11-07 20:50:18');
INSERT INTO `user_info` VALUES (58, '1234@qq.com', '清云-user', '207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb', 'http://s32vad0na.bkt.clouddn.com/default_avatar.jpeg', '希望晋级！', '2023-11-07 20:29:11', '2023-11-07 20:50:19');
INSERT INTO `user_info` VALUES (59, '12345@qq.com', '清云-user', '207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb', 'http://s32vad0na.bkt.clouddn.com/default_avatar.jpeg', '希望晋级！', '2023-11-07 20:29:14', '2023-11-07 20:50:21');
INSERT INTO `user_info` VALUES (60, '123457@qq.com', '清云-user', '207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb', 'http://s32vad0na.bkt.clouddn.com/default_avatar.jpeg', '希望晋级！', '2023-11-07 20:29:21', '2023-11-07 20:50:22');
INSERT INTO `user_info` VALUES (61, '1234579@qq.com', '清云-user', '207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb', 'http://s32vad0na.bkt.clouddn.com/default_avatar.jpeg', '希望晋级！', '2023-11-07 20:29:25', '2023-11-07 20:50:24');

-- ----------------------------
-- Table structure for video_count
-- ----------------------------
DROP TABLE IF EXISTS `video_count`;
CREATE TABLE `video_count`  (
  `pk_video_id` int NOT NULL COMMENT 'VideoId',
  `liked_count` int NULL DEFAULT 0 COMMENT '获赞数',
  `commented_count` int NULL DEFAULT 0 COMMENT '评论数',
  `collected_count` int NULL DEFAULT 0 COMMENT '收藏数',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`pk_video_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of video_count
-- ----------------------------
INSERT INTO `video_count` VALUES (3, 0, 0, 0, '2023-11-05 14:49:34', NULL);
INSERT INTO `video_count` VALUES (6, 0, 0, 0, '2023-11-05 14:57:09', NULL);
INSERT INTO `video_count` VALUES (7, 0, 0, 0, '2023-11-05 14:58:10', NULL);
INSERT INTO `video_count` VALUES (8, 0, 0, 0, '2023-11-05 14:59:19', NULL);
INSERT INTO `video_count` VALUES (22, 0, 0, 0, '2023-11-07 14:53:59', NULL);
INSERT INTO `video_count` VALUES (23, 0, 0, 0, '2023-11-07 14:55:04', NULL);
INSERT INTO `video_count` VALUES (24, 0, 0, 0, '2023-11-07 14:57:15', NULL);
INSERT INTO `video_count` VALUES (25, 0, 0, 0, '2023-11-07 15:00:39', NULL);
INSERT INTO `video_count` VALUES (26, 0, 0, 0, '2023-11-07 15:01:42', NULL);
INSERT INTO `video_count` VALUES (28, 0, 0, 0, '2023-11-07 15:03:33', NULL);
INSERT INTO `video_count` VALUES (29, 0, 0, 0, '2023-11-07 15:10:34', NULL);
INSERT INTO `video_count` VALUES (31, 0, 0, 0, '2023-11-07 15:15:18', NULL);
INSERT INTO `video_count` VALUES (32, 0, 0, 0, '2023-11-07 15:16:39', NULL);
INSERT INTO `video_count` VALUES (34, 0, 0, 0, '2023-11-07 15:21:23', NULL);
INSERT INTO `video_count` VALUES (35, 0, 0, 0, '2023-11-07 22:01:30', NULL);

-- ----------------------------
-- Table structure for video_info
-- ----------------------------
DROP TABLE IF EXISTS `video_info`;
CREATE TABLE `video_info`  (
  `pk_video_id` int NOT NULL AUTO_INCREMENT COMMENT 'VideoId',
  `user_id` int NULL DEFAULT NULL COMMENT '作者的UserId',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分区类型',
  `video_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '视频描述',
  `video_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '视频封面URL',
  `play_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '播放URL',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`pk_video_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of video_info
-- ----------------------------
INSERT INTO `video_info` VALUES (3, 54, '娱乐', '我希望我的最后一口气是在讲台上呼出去的', 'http://s32vad0na.bkt.clouddn.com/54-1699165546734-1.jpg', 'http://s32vad0na.bkt.clouddn.com/54-1699165546734-1.mp4', '2023-11-05 14:49:34');
INSERT INTO `video_info` VALUES (6, 54, '娱乐', '于文亮为什么突然火了', 'http://s32vad0na.bkt.clouddn.com/54-1699167075973-2.jpg', 'http://s32vad0na.bkt.clouddn.com/54-1699167075973-2.mp4', '2023-11-05 14:57:09');
INSERT INTO `video_info` VALUES (7, 54, '娱乐', '爱你，老妈', 'http://s32vad0na.bkt.clouddn.com/54-1699167462534-3.jpg', 'http://s32vad0na.bkt.clouddn.com/54-1699167462534-3.mp4', '2023-11-05 14:58:10');
INSERT INTO `video_info` VALUES (8, 54, '时尚', '张柏芝的神仙颜值', 'http://s32vad0na.bkt.clouddn.com/54-1699167531962-4.jpg', 'http://s32vad0na.bkt.clouddn.com/54-1699167531962-4.mp4', '2023-11-05 14:59:19');
INSERT INTO `video_info` VALUES (22, 54, '时尚', '古装小姐姐', 'http://s32vad0na.bkt.clouddn.com/54-1699339954273-5.jpg', 'http://s32vad0na.bkt.clouddn.com/54-1699339954273-5.mp4', '2023-11-07 14:53:58');
INSERT INTO `video_info` VALUES (23, 54, '音乐', '浅yin阿《三国恋》', 'http://s32vad0na.bkt.clouddn.com/54-1699340051512-6.jpg', 'http://s32vad0na.bkt.clouddn.com/54-1699340051512-6.mp4', '2023-11-07 14:55:03');
INSERT INTO `video_info` VALUES (24, 54, '娱乐', '女儿的长相与声音完全不符', 'http://s32vad0na.bkt.clouddn.com/54-1699340172044-7.jpg', 'http://s32vad0na.bkt.clouddn.com/54-1699340172044-7.mp4', '2023-11-07 14:57:15');
INSERT INTO `video_info` VALUES (25, 54, '美食', '蒜香牛肉粒', 'http://s32vad0na.bkt.clouddn.com/54-1699340410042-8.jpg', 'http://s32vad0na.bkt.clouddn.com/54-1699340410042-8.mp4', '2023-11-07 15:00:38');
INSERT INTO `video_info` VALUES (26, 54, '娱乐', '泡沫', 'http://s32vad0na.bkt.clouddn.com/54-1699340478463-9.jpg', 'http://s32vad0na.bkt.clouddn.com/54-1699340478463-9.mp4', '2023-11-07 15:01:42');
INSERT INTO `video_info` VALUES (28, 54, '音乐', '这是测试视频', 'http://s32vad0na.bkt.clouddn.com/54-1699340570456-11.jpg', 'http://s32vad0na.bkt.clouddn.com/54-1699340570456-11.mp4', '2023-11-07 15:03:33');
INSERT INTO `video_info` VALUES (29, 56, '音乐', '这是测试视频', 'http://s32vad0na.bkt.clouddn.com/56-1699341001113-12.jpg', 'http://s32vad0na.bkt.clouddn.com/56-1699341001113-12.mp4', '2023-11-07 15:10:34');
INSERT INTO `video_info` VALUES (31, 56, '美食', '这是测试视频', 'http://s32vad0na.bkt.clouddn.com/56-1699341264583-14.jpg', 'http://s32vad0na.bkt.clouddn.com/56-1699341264583-14.mp4', '2023-11-07 15:15:18');
INSERT INTO `video_info` VALUES (32, 56, '美食', '这是测试视频', 'http://s32vad0na.bkt.clouddn.com/56-1699341338888-15.jpg', 'http://s32vad0na.bkt.clouddn.com/56-1699341338888-15.mp4', '2023-11-07 15:16:39');
INSERT INTO `video_info` VALUES (34, 56, '动漫', '这是测试视频', 'http://s32vad0na.bkt.clouddn.com/56-1699341648437-17.jpg', 'http://s32vad0na.bkt.clouddn.com/56-1699341648437-17.mp4', '2023-11-07 15:21:23');
INSERT INTO `video_info` VALUES (35, 56, 'Option3', 'hi', 'http://s32vad0na.bkt.clouddn.com/56-1699365674758-7367bd4e0798f36cbd53e66b8a98fea6.jpg', 'http://s32vad0na.bkt.clouddn.com/56-1699365674758-7367bd4e0798f36cbd53e66b8a98fea6.mp4', '2023-11-07 22:01:30');

SET FOREIGN_KEY_CHECKS = 1;
