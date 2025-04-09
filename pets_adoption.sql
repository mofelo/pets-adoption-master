/*
 Navicat MySQL Data Transfer

 Source Server         : locahost_3306
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : pets_adoption

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 01/04/2025 12:50:50
 整合文件说明：本文件整合了pets_adoption.sql、pet_community.sql和community_module.sql的所有表结构和数据
 更新说明：
 1. 宠物类型限制为猫和狗
 2. 所有日期更新为2025年
 3. 添加社区交流模块的示例数据
 4. 添加情绪性格匹配功能的表结构和数据
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for adoptions
-- ----------------------------
DROP TABLE IF EXISTS `adoptions`;
CREATE TABLE `adoptions`  (
  `ado_id` int NOT NULL AUTO_INCREMENT COMMENT '领养记录id',
  `user_id` int NULL DEFAULT NULL COMMENT '领养人id',
  `pet_id` int NULL DEFAULT NULL COMMENT '宠物id',
  `ado_date` date NULL DEFAULT NULL COMMENT '领养时间',
  `ado_status` int NULL DEFAULT NULL COMMENT '领养状态：0失败，1成功，2处理中',
  `ado_note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注信息，领养情况',
  PRIMARY KEY (`ado_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of adoptions
-- ----------------------------
INSERT INTO `adoptions` VALUES (13, 1000000019, 1000000015, '2025-03-29', 0, NULL);
INSERT INTO `adoptions` VALUES (14, 1000000020, 1000000005, '2025-03-17', 0, '用户被冻结，领养失败。');
INSERT INTO `adoptions` VALUES (15, 1000000020, 1000000005, '2025-03-29', 0, NULL);
INSERT INTO `adoptions` VALUES (16, 1000000020, 1000000005, '2025-03-29', 1, NULL);
INSERT INTO `adoptions` VALUES (17, 1000000020, 1000000015, '2025-03-29', 0, NULL);
INSERT INTO `adoptions` VALUES (18, 1000000019, 1000000015, '2025-03-29', 0, '用户取消领养');
INSERT INTO `adoptions` VALUES (19, 1000000020, 1000000015, '2025-03-29', 0, '用户取消领养');
INSERT INTO `adoptions` VALUES (20, 1000000020, 1000000015, '2025-03-29', 1, NULL);
INSERT INTO `adoptions` VALUES (21, 1000000019, 1000000009, '2025-03-29', 1, NULL);
INSERT INTO `adoptions` VALUES (22, 1000000019, 1000000010, '2025-03-29', 0, NULL);
INSERT INTO `adoptions` VALUES (23, 1000000019, 1000000010, '2025-03-25', 0, '领养失败！');
INSERT INTO `adoptions` VALUES (24, 1000000020, 1000000010, '2025-03-29', 1, '领养成功！');
INSERT INTO `adoptions` VALUES (25, 1000000021, 1000000011, '2025-03-29', 1, '领养成功！');
INSERT INTO `adoptions` VALUES (26, 1000000019, 1000000012, '2025-03-21', 1, '领养成功！');
INSERT INTO `adoptions` VALUES (27, 1000000019, 1000000013, '2025-03-20', 0, '用户取消领养');
INSERT INTO `adoptions` VALUES (28, 1000000022, 1000000017, '2025-03-29', 1, '领养成功！');
INSERT INTO `adoptions` VALUES (29, 1000000023, 1000000013, '2025-03-01', 2, NULL);

-- ----------------------------
-- Table structure for notices
-- ----------------------------
DROP TABLE IF EXISTS `notices`;
CREATE TABLE `notices`  (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告编号',
  `notice_type` int NULL DEFAULT NULL COMMENT '公告类型：1公示，2领养日志',
  `user_id` int NULL DEFAULT NULL COMMENT '发布人编号',
  `notice_date` date NULL DEFAULT NULL COMMENT '发布时间',
  `notice_title` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `notice_context` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容',
  `user_realname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布人姓名',
  `notice_image` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公告图片',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notices
-- ----------------------------
INSERT INTO `notices` VALUES (1, 1, 1000000014, '2025-03-29', '测试', '可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？', '复读机', NULL);
INSERT INTO `notices` VALUES (2, 1, 1000000019, '2025-03-29', '我和树獭', '最喜欢的树懒
他居然会爬树！', '路人甲', 'cde292c0-39fa-41a2-a987-ee8ca31ea5d2.jpg');
INSERT INTO `notices` VALUES (3, 1, 1000000020, '2025-03-29', '我喜欢的宠物', '他真的超级可爱
可是这和我是一个冷酷的复读机又有什么关系呢？
', 'qwq', 'd99efbb1-7f95-43ad-94d7-0d2abc0ad900.jpg');
INSERT INTO `notices` VALUES (4, 2, 1000000020, '2025-03-29', '测试', '我丢我丢我丢
这是我第一次
领养宠物
状
他真的很可爱', 'qwq', '54284a1f-86a1-43d5-872a-07e07c94cc0f.jpg');
INSERT INTO `notices` VALUES (5, 1, 1000000019, '2025-03-29', '形势不容乐观', '1、兄弟们。
2、姐妹们
3、打包们！
可是这和我是一个冷酷的复读机又有什么关系呢？', '路人甲', '6d039273-c8b4-440b-8094-8abc84ca689b.jpg');
INSERT INTO `notices` VALUES (6, 1, 1000000019, '2025-03-29', '不得了啦', '1、我丢
2、可是这和我是一个冷酷的复读机又有什么关系呢？
3、可是这和我是一个冷酷的复读机又有什么关系呢？
4、可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？可是这和我是一个冷酷的复读机又有什么关系呢？', '路人甲', '2d64f4a1-41a2-44f2-9125-9e68deaad7ae.jpg');
INSERT INTO `notices` VALUES (7, 1, 1000000014, '2025-03-30', '累死了真的', '开发这个东西真滴好累啊
可是这和我是一个冷酷的复读机又有什么关系呢？
可是这和我是一个冷酷的复读机又有什么关系呢？
可是这和我是一个冷酷的复读机又有什么关系呢？
可是这和我是一个冷酷的复读机又有什么关系呢？
可是这和我是一个冷酷的复读机又有什么关系呢？
可是这和我是一个冷酷的复读机又有什么关系呢？
可是这和我是一个冷酷的复读机又有什么关系呢？
可是这和我是一个冷酷的复读机又有什么关系呢？
', '复读机', '8d1122e7-42d0-464b-b284-b9ecd83ab5b7.jpg');

-- ----------------------------
-- Table structure for pets
-- ----------------------------
DROP TABLE IF EXISTS `pets`;
CREATE TABLE `pets`  (
  `pet_id` int NOT NULL AUTO_INCREMENT COMMENT '宠物编号',
  `pet_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '宠物昵称',
  `pet_type` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '宠物品种',
  `pet_sex` int NULL DEFAULT NULL COMMENT '宠物性别：1雄，0雌',
  `pet_age` date NULL DEFAULT NULL COMMENT '宠物生日，用于获取年龄',
  `pet_indata` date NULL DEFAULT NULL COMMENT '宠物入园时间',
  `pet_image` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '宠物照片文件名',
  `pet_introduction` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '宠物简介',
  `pet_personality` int NULL DEFAULT NULL COMMENT '宠物性格：1外向，0内向',
  `pet_status` int NULL DEFAULT NULL COMMENT '宠物状态：0待领养，1被领养，2被申领，3离世',
  `user_id` int NULL DEFAULT NULL COMMENT '领养人id',
  PRIMARY KEY (`pet_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000000018 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pets
-- ----------------------------
INSERT INTO `pets` VALUES (1000000000, '小橘', '橘猫', 0, '2025-03-26', '2025-08-12', '', '非常可爱1', 0, 0, NULL);
INSERT INTO `pets` VALUES (1000000005, '黑豆', '家猫', 1, '2025-03-28', '2025-03-26', '4aad4d0f-acc8-49f1-b649-2b5dd86e24b2.jpg', '可爱的小猫咪', 0, 1, NULL);
INSERT INTO `pets` VALUES (1000000010, '雪球', '理花猫', 1, '2025-03-28', '2025-03-26', '1f7b5949-934a-4292-9245-d8478cee9b89.jpg', '可是这和我是一个冷酷的复读机又有什么关系呢？', 1, 1, NULL);
INSERT INTO `pets` VALUES (1000000011, '奶糖', '贵宾犬', 1, '2025-03-28', '2025-03-26', '21fe878a-eb58-4b8f-89ed-aa3bf88edc47.jpg', '五千万', 0, 1, NULL);
INSERT INTO `pets` VALUES (1000000012, '点点', '比熊犬', 1, '2025-03-06', '2025-03-26', '8f681507-092a-4856-8d50-d8892334c1db.jpg', '全文', 1, 1, NULL);
INSERT INTO `pets` VALUES (1000000013, '小黑', '泰迪犬', 1, '2025-03-09', '2025-03-26', 'ec8d7a19-d95a-4aea-8b03-36f0cfaf5bf4.jpg', '可是这和我是一个冷酷的复读机又有什么关系呢？', 0, 2, NULL);
INSERT INTO `pets` VALUES (1000000014, '联联', '边境牧羊犬', 1, '2025-03-29', '2025-03-26', 'e4420511-f549-452a-be9b-3911b9d39724.jpg', 'qwqwq', 0, 0, NULL);
INSERT INTO `pets` VALUES (1000000015, '胖胖', '英国短毛猫', 1, '2025-03-28', '2025-03-27', '8310bad6-569a-4460-b496-3fb25cdd67df.jpg', 'ssss', 0, 1, NULL);
INSERT INTO `pets` VALUES (1000000017, '阳阳', '金毛犬', 1, '2025-03-13', '2025-03-30', '22a85b55-bd12-4508-bcec-855d11ebd391.jpg', '超级可爱的金毛犬
   可是ksks可是这和我是一个冷酷的复读机又有什么关系呢？
我丢
可是这和我是一个冷酷的复读机又有什么关系呢？', 0, 1, NULL);
INSERT INTO `pets` VALUES (1000000018, '毛毛', '理花猫', 0, '2025-03-01', '2025-03-30', 'image1.jpg', '温顺可爱', 1, 0, NULL);
INSERT INTO `pets` VALUES (1000000019, '小黑猫', '暹罗猫', 1, '2025-03-02', '2025-03-30', 'image2.jpg', '活泼好动', 0, 0, NULL);
INSERT INTO `pets` VALUES (1000000020, '小白狗', '哈士奇', 0, '2025-03-03', '2025-03-30', 'image3.jpg', '聪明伶俐', 1, 0, NULL);
INSERT INTO `pets` VALUES (1000000021, '布丁', '拉布拉多', 1, '2025-03-04', '2025-03-30', 'image4.jpg', '忠诚友好', 0, 0, NULL);
INSERT INTO `pets` VALUES (1000000022, '小白狗', '中华田园犬', 0, '2025-03-03', '2025-03-30', 'image3.jpg', '聪明伶俐', 1, 0, NULL);
INSERT INTO `pets` VALUES (1000000023, '哈哈', '拉布拉多', 1, '2025-03-04', '2025-03-30', 'image4.jpg', '忠诚友好', 0, 0, NULL);
INSERT INTO `pets` VALUES (1000000024, '小黄狗', '金毛', 0, '2025-03-03', '2025-03-30', 'image3.jpg', '聪明伶俐', 1, 0, NULL);
INSERT INTO `pets` VALUES (1000000025, '可可', '拉布拉多', 1, '2025-03-04', '2025-03-30', 'image4.jpg', '忠诚友好', 0, 0, NULL);
INSERT INTO `pets` VALUES (1000000026, '小白狗', '中华田园犬', 0, '2025-03-03', '2025-03-30', 'image3.jpg', '聪明伶俐', 1, 0, NULL);
INSERT INTO `pets` VALUES (1000000027, '小黄狗', '拉布拉多', 1, '2025-03-04', '2025-03-30', 'image4.jpg', '忠诚友好', 0, 0, NULL);
INSERT INTO `pets` VALUES (1000000028, '小白狗', '贵宾犬', 0, '2025-03-03', '2025-03-30', 'image3.jpg', '聪明伶俐', 1, 0, NULL);
INSERT INTO `pets` VALUES (1000000029, '小黄狗', '拉布拉多', 1, '2025-03-04', '2025-03-30', 'image4.jpg', '忠诚友好', 0, 0, NULL);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户账户',
  `user_password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码',
  `user_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `user_sex` int NULL DEFAULT NULL COMMENT '用户性别:1男，0女，2未设置',
  `user_introduction` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户简介',
  `user_registertime` date NULL DEFAULT NULL COMMENT '用户注册时间',
  `user_realname` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户真实姓名',
  `user_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户手机号',
  `user_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户地址',
  `user_status` int NULL DEFAULT NULL COMMENT '用户状态，0未申领，1申领中，2领养后未按规发布领养日志，3暂停使用',
  `user_type` int NULL DEFAULT NULL COMMENT '用户类型，0普通用户，1管理员，2领养人',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `users_pk`(`user_account`) USING BTREE,
  UNIQUE INDEX `users_pk2`(`user_phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000000024 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1000000014, 'lurenjia', '1111', '大帅哥', 1, '可是这和我是一个冷酷的复读机又有什么关系呢？', '2025-03-27', '复读机', '17674185013', '路人甲的地址是在上上上山的路上。', 0, 1);
INSERT INTO `users` VALUES (1000000019, 'ccc', '111', 'ccc', 2, NULL, '2025-03-27', '路人甲', '17670844220', '可是这和我是一个冷酷的复读机又有什么关系呢？', 0, 1);
INSERT INTO `users` VALUES (1000000020, 'qwq', 'qwq', 'qwq', 2, NULL, '2025-03-27', 'qwq', '16356356663', '可是这和我是一个冷酷的复读机又有什么关系呢？', 0, 2);
INSERT INTO `users` VALUES (1000000021, 'rrr', '111', 'rrr', 2, NULL, '2025-03-29', '真实姓名', '17676738442', '可是这和我是一个冷酷的复读机又有什么关系呢？', 3, 2);
INSERT INTO `users` VALUES (1000000022, 'cccc', '1111', 'cccc', 1, '大帅哥', '2025-03-30', '大大怪', '17676787441', '可是这和我是一个冷酷的复读机又有什么关系呢？', 3, 1);
INSERT INTO `users` VALUES (1000000023, 'wwww', '1111', 'wwww', 2, NULL, '2025-03-30', '大坏蛋', '17676722221', '可是这和我是一个冷酷的复读机又有什么关系呢？', 1, 0);

-- ----------------------------
-- Table structure for community_posts
-- ----------------------------
DROP TABLE IF EXISTS `community_posts`;
CREATE TABLE `community_posts`  (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '发布用户ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '帖子标题',
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '帖子内容',
  `post_image` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '帖子图片',
  `post_type` int NULL DEFAULT NULL COMMENT '帖子类型：1饲养经验，2求助问答，3宠物展示',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览次数',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数',
  `comment_count` int NULL DEFAULT 0 COMMENT '评论数',
  `status` int NULL DEFAULT 1 COMMENT '状态：0删除，1正常',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '社区帖子表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for community_comments
-- ----------------------------
DROP TABLE IF EXISTS `community_comments`;
CREATE TABLE `community_comments`  (
  `comment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` bigint NULL DEFAULT NULL COMMENT '帖子ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '评论用户ID',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论内容',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父评论ID，0表示一级评论',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` int NULL DEFAULT 1 COMMENT '状态：0删除，1正常',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `idx_post_id`(`post_id`) USING BTREE COMMENT '帖子ID索引',
  INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT '用户ID索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for community_likes
-- ----------------------------
DROP TABLE IF EXISTS `community_likes`;
CREATE TABLE `community_likes`  (
  `like_id` bigint NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `post_id` bigint NULL DEFAULT NULL COMMENT '帖子ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`like_id`) USING BTREE,
  UNIQUE INDEX `idx_post_user`(`post_id`, `user_id`) USING BTREE COMMENT '确保用户只能点赞一次',
  INDEX `idx_post_id`(`post_id`) USING BTREE COMMENT '帖子ID索引',
  INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT '用户ID索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '点赞表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 功能说明
-- ----------------------------
/*
社区交流模块主要功能：

1. 帖子管理功能
   - 发布帖子：用户可以发布不同类型的帖子（饲养经验、求助问答、宠物展示）
   - 查看帖子列表：支持分页查询、按类型筛选、按用户筛选
   - 查看帖子详情：显示帖子内容、评论、点赞状态等
   - 更新帖子：用户可以修改自己发布的帖子
   - 删除帖子：用户可以删除自己发布的帖子（逻辑删除）

2. 评论功能
   - 发表评论：用户可以对帖子发表评论
   - 回复评论：用户可以回复其他用户的评论，形成评论树结构
   - 查看评论列表：查看帖子下的所有评论，包括评论的层级关系
   - 删除评论：用户可以删除自己发表的评论（逻辑删除）

3. 点赞功能
   - 点赞/取消点赞：用户可以对帖子进行点赞或取消点赞
   - 查看点赞状态：查询用户是否已对某帖子点赞
*/

-- ----------------------------
-- 情绪性格匹配功能的表结构和示例数据
-- ----------------------------

-- ----------------------------
-- Table structure for personality_traits
-- ----------------------------
DROP TABLE IF EXISTS `personality_traits`;
CREATE TABLE `personality_traits` (
  `trait_id` int NOT NULL AUTO_INCREMENT COMMENT '特质ID',
  `trait_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '特质名称',
  `trait_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '特质描述',
  `trait_type` int NOT NULL COMMENT '特质类型：1-性格特质，2-情绪特质',
  PRIMARY KEY (`trait_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '性格特质表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of personality_traits
-- ----------------------------
INSERT INTO `personality_traits` VALUES (1, '外向', '喜欢社交，精力充沛，善于表达', 1);
INSERT INTO `personality_traits` VALUES (2, '内向', '安静，独立，深思熟虑', 1);
INSERT INTO `personality_traits` VALUES (3, '敏感', '容易受到情绪影响，情感丰富', 1);
INSERT INTO `personality_traits` VALUES (4, '稳定', '情绪稳定，不易受外界影响', 1);
INSERT INTO `personality_traits` VALUES (5, '活跃', '精力充沛，喜欢运动和玩耍', 1);
INSERT INTO `personality_traits` VALUES (6, '安静', '喜欢安静的环境，不喜欢过度活动', 1);
INSERT INTO `personality_traits` VALUES (7, '独立', '自主性强，不依赖他人', 1);
INSERT INTO `personality_traits` VALUES (8, '依赖', '喜欢陪伴，依赖他人', 1);
INSERT INTO `personality_traits` VALUES (9, '友好', '友善，容易相处', 1);
INSERT INTO `personality_traits` VALUES (10, '警惕', '对陌生人或环境保持警惕', 1);
INSERT INTO `personality_traits` VALUES (11, '快乐', '经常表现出愉悦的情绪', 2);
INSERT INTO `personality_traits` VALUES (12, '焦虑', '容易紧张或担忧', 2);
INSERT INTO `personality_traits` VALUES (13, '平静', '情绪平稳，不易激动', 2);
INSERT INTO `personality_traits` VALUES (14, '敏感', '对外界刺激反应强烈', 2);
INSERT INTO `personality_traits` VALUES (15, '好奇', '对新事物充满好奇心', 2);

-- ----------------------------
-- Table structure for user_personality
-- ----------------------------
DROP TABLE IF EXISTS `user_personality`;
CREATE TABLE `user_personality` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `trait_id` int NOT NULL COMMENT '特质ID',
  `trait_value` int NOT NULL COMMENT '特质值（1-10，表示程度）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_trait` (`user_id`, `trait_id`) COMMENT '确保用户特质唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户性格表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_personality
-- ----------------------------
INSERT INTO `user_personality` VALUES (1, 1000000019, 1, 8);
INSERT INTO `user_personality` VALUES (2, 1000000019, 5, 7);
INSERT INTO `user_personality` VALUES (3, 1000000019, 9, 9);
INSERT INTO `user_personality` VALUES (4, 1000000019, 11, 8);
INSERT INTO `user_personality` VALUES (5, 1000000019, 15, 7);
INSERT INTO `user_personality` VALUES (6, 1000000020, 2, 7);
INSERT INTO `user_personality` VALUES (7, 1000000020, 4, 8);
INSERT INTO `user_personality` VALUES (8, 1000000020, 6, 6);
INSERT INTO `user_personality` VALUES (9, 1000000020, 13, 9);
INSERT INTO `user_personality` VALUES (10, 1000000020, 7, 7);

-- ----------------------------
-- Table structure for pet_personality
-- ----------------------------
DROP TABLE IF EXISTS `pet_personality`;
CREATE TABLE `pet_personality` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pet_id` int NOT NULL COMMENT '宠物ID',
  `trait_id` int NOT NULL COMMENT '特质ID',
  `trait_value` int NOT NULL COMMENT '特质值（1-10，表示程度）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_pet_trait` (`pet_id`, `trait_id`) COMMENT '确保宠物特质唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '宠物性格表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pet_personality
-- ----------------------------
INSERT INTO `pet_personality` VALUES (1, 1000000000, 1, 7);
INSERT INTO `pet_personality` VALUES (2, 1000000000, 5, 8);
INSERT INTO `pet_personality` VALUES (3, 1000000000, 9, 9);
INSERT INTO `pet_personality` VALUES (4, 1000000000, 11, 8);
INSERT INTO `pet_personality` VALUES (5, 1000000000, 15, 7);
INSERT INTO `pet_personality` VALUES (6, 1000000001, 2, 8);
INSERT INTO `pet_personality` VALUES (7, 1000000001, 6, 7);
INSERT INTO `pet_personality` VALUES (8, 1000000001, 7, 9);
INSERT INTO `pet_personality` VALUES (9, 1000000001, 13, 8);
INSERT INTO `pet_personality` VALUES (10, 1000000001, 14, 6);
INSERT INTO `pet_personality` VALUES (11, 1000000002, 2, 9);
INSERT INTO `pet_personality` VALUES (12, 1000000002, 4, 8);
INSERT INTO `pet_personality` VALUES (13, 1000000002, 6, 9);
INSERT INTO `pet_personality` VALUES (14, 1000000002, 13, 7);
INSERT INTO `pet_personality` VALUES (15, 1000000002, 8, 6);
INSERT INTO `pet_personality` VALUES (16, 1000000003, 1, 6);
INSERT INTO `pet_personality` VALUES (17, 1000000003, 3, 7);
INSERT INTO `pet_personality` VALUES (18, 1000000003, 5, 5);
INSERT INTO `pet_personality` VALUES (19, 1000000003, 9, 8);
INSERT INTO `pet_personality` VALUES (20, 1000000003, 15, 7);
INSERT INTO `pet_personality` VALUES (21, 1000000004, 1, 8);
INSERT INTO `pet_personality` VALUES (22, 1000000004, 5, 9);
INSERT INTO `pet_personality` VALUES (23, 1000000004, 9, 7);
INSERT INTO `pet_personality` VALUES (24, 1000000004, 11, 8);
INSERT INTO `pet_personality` VALUES (25, 1000000004, 15, 9);
INSERT INTO `pet_personality` VALUES (26, 1000000005, 2, 6);
INSERT INTO `pet_personality` VALUES (27, 1000000005, 4, 7);
INSERT INTO `pet_personality` VALUES (28, 1000000005, 8, 8);
INSERT INTO `pet_personality` VALUES (29, 1000000005, 9, 9);
INSERT INTO `pet_personality` VALUES (30, 1000000005, 13, 7);
INSERT INTO `pet_personality` VALUES (31, 1000000006, 1, 9);
INSERT INTO `pet_personality` VALUES (32, 1000000006, 5, 9);
INSERT INTO `pet_personality` VALUES (33, 1000000006, 9, 8);
INSERT INTO `pet_personality` VALUES (34, 1000000006, 11, 9);
INSERT INTO `pet_personality` VALUES (35, 1000000006, 15, 8);
INSERT INTO `pet_personality` VALUES (36, 1000000007, 1, 7);
INSERT INTO `pet_personality` VALUES (37, 1000000007, 4, 8);
INSERT INTO `pet_personality` VALUES (38, 1000000007, 7, 6);
INSERT INTO `pet_personality` VALUES (39, 1000000007, 9, 9);
INSERT INTO `pet_personality` VALUES (40, 1000000007, 11, 7);

-- ----------------------------
-- Table structure for personality_compatibility
-- ----------------------------
DROP TABLE IF EXISTS `personality_compatibility`;
CREATE TABLE `personality_compatibility` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `trait1_id` int NOT NULL COMMENT '特质1 ID',
  `trait2_id` int NOT NULL COMMENT '特质2 ID',
  `compatibility_score` int NOT NULL COMMENT '兼容性分数（1-10，10表示最兼容）',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '兼容性描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_traits` (`trait1_id`, `trait2_id`) COMMENT '确保特质对唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '性格兼容性表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of personality_compatibility
-- ----------------------------
INSERT INTO `personality_compatibility` VALUES (1, 1, 1, 9, '外向的人和外向的宠物能够互相激发活力');
INSERT INTO `personality_compatibility` VALUES (2, 1, 2, 6, '外向的人可能会让内向的宠物感到压力，但也能帮助宠物社交化');
INSERT INTO `personality_compatibility` VALUES (3, 1, 5, 10, '外向的人和活跃的宠物是完美的搭配');
INSERT INTO `personality_compatibility` VALUES (4, 1, 6, 5, '外向的人可能会让安静的宠物感到不适');
INSERT INTO `personality_compatibility` VALUES (5, 2, 2, 8, '内向的人和内向的宠物能够互相理解，共享安静的时光');
INSERT INTO `personality_compatibility` VALUES (6, 2, 1, 5, '内向的人可能会被外向的宠物打扰，但也能被带动');
INSERT INTO `personality_compatibility` VALUES (7, 2, 6, 9, '内向的人和安静的宠物是很好的搭配');
INSERT INTO `personality_compatibility` VALUES (8, 2, 5, 4, '内向的人可能会被活跃的宠物消耗精力');
INSERT INTO `personality_compatibility` VALUES (9, 3, 4, 7, '敏感的人和稳定的宠物能够互相平衡');
INSERT INTO `personality_compatibility` VALUES (10, 3, 3, 6, '敏感的人和敏感的宠物可能会互相影响情绪');
INSERT INTO `personality_compatibility` VALUES (11, 4, 3, 8, '稳定的人能够给敏感的宠物提供安全感');
INSERT INTO `personality_compatibility` VALUES (12, 4, 4, 9, '稳定的人和稳定的宠物能够建立稳固的关系');
INSERT INTO `personality_compatibility` VALUES (13, 5, 5, 10, '活跃的人和活跃的宠物能够一起享受运动的乐趣');
INSERT INTO `personality_compatibility` VALUES (14, 5, 6, 5, '活跃的人可能会让安静的宠物感到压力');
INSERT INTO `personality_compatibility` VALUES (15, 6, 6, 9, '安静的人和安静的宠物能够共享平静的环境');
INSERT INTO `personality_compatibility` VALUES (16, 6, 5, 4, '安静的人可能会被活跃的宠物打扰');
INSERT INTO `personality_compatibility` VALUES (17, 7, 8, 7, '独立的人和依赖的宠物能够互相补充');
INSERT INTO `personality_compatibility` VALUES (18, 7, 7, 6, '独立的人和独立的宠物可能会缺乏互动');
INSERT INTO `personality_compatibility` VALUES (19, 8, 7, 5, '依赖的人可能会让独立的宠物感到压力');
INSERT INTO `personality_compatibility` VALUES (20, 8, 8, 9, '依赖的人和依赖的宠物能够互相陪伴');
INSERT INTO `personality_compatibility` VALUES (21, 9, 9, 10, '友好的人和友好的宠物能够建立亲密的关系');
INSERT INTO `personality_compatibility` VALUES (22, 9, 10, 7, '友好的人能够帮助警惕的宠物建立信任');
INSERT INTO `personality_compatibility` VALUES (23, 10, 9, 6, '警惕的人可能会让友好的宠物感到困惑');
INSERT INTO `personality_compatibility` VALUES (24, 10, 10, 8, '警惕的人和警惕的宠物能够互相理解');
INSERT INTO `personality_compatibility` VALUES (25, 11, 11, 9, '快乐的人和快乐的宠物能够互相感染积极情绪');
INSERT INTO `personality_compatibility` VALUES (26, 11, 12, 7, '快乐的人能够帮助焦虑的宠物放松');
INSERT INTO `personality_compatibility` VALUES (27, 12, 13, 8, '焦虑的人和平静的宠物能够互相平衡');
INSERT INTO `personality_compatibility` VALUES (28, 12, 12, 5, '焦虑的人和焦虑的宠物可能会互相加剧情绪');
INSERT INTO `personality_compatibility` VALUES (29, 13, 12, 9, '平静的人能够给焦虑的宠物提供安全感');
INSERT INTO `personality_compatibility` VALUES (30, 13, 13, 10, '平静的人和平静的宠物能够共享宁静的环境');
INSERT INTO `personality_compatibility` VALUES (31, 14, 13, 7, '敏感的人和平静的宠物能够互相平衡');
INSERT INTO `personality_compatibility` VALUES (32, 14, 14, 6, '敏感的人和敏感的宠物可能会互相影响');
INSERT INTO `personality_compatibility` VALUES (33, 15, 15, 10, '好奇的人和好奇的宠物能够一起探索新事物');
INSERT INTO `personality_compatibility` VALUES (34, 15, 10, 8, '好奇的人能够帮助警惕的宠物建立信心');

-- ----------------------------
-- 添加社区交流模块的示例数据
-- ----------------------------

-- ----------------------------
-- Records of community_posts
-- ----------------------------
INSERT INTO `community_posts` VALUES (1, 1000000019, '如何照顾新领养的猫咪', '我刚刚领养了一只小猫咪，想请教大家有什么照顾猫咪的经验和建议？特别是关于饮食、洗澡和训练方面的。', 'post_image1.jpg', 2, '2025-03-15 10:30:00', '2025-03-15 10:30:00', 120, 15, 8, 1);
INSERT INTO `community_posts` VALUES (2, 1000000020, '分享我家金毛的日常', '这是我家的金毛"小金"，超级可爱！每天早上都要出去遛一圈，回来后就躺在沙发上晒太阳。最喜欢吃的是鸡肉和胡萝卜。', 'post_image2.jpg', 3, '2025-03-16 14:20:00', '2025-03-16 14:20:00', 85, 20, 5, 1);
INSERT INTO `community_posts` VALUES (3, 1000000021, '猫咪饮食指南', '作为一个养猫多年的老手，我想分享一些关于猫咪饮食的经验。首先，猫咪是肉食动物，需要高蛋白质的食物。其次，不要给猫咪喂食人类的食物，特别是巧克力、葡萄和洋葱，这些对猫咪有毒。', NULL, 1, '2025-03-17 09:45:00', '2025-03-17 09:45:00', 200, 30, 12, 1);
INSERT INTO `community_posts` VALUES (4, 1000000022, '狗狗训练技巧', '想要训练好一只狗狗，关键是耐心和一致性。使用正面强化训练法，即当狗狗做对时给予奖励，而不是做错时惩罚。每天坚持短时间的训练，比一次长时间训练效果更好。', 'post_image3.jpg', 1, '2025-03-18 16:10:00', '2025-03-18 16:10:00', 150, 25, 10, 1);
INSERT INTO `community_posts` VALUES (5, 1000000023, '我家猫咪不爱喝水怎么办？', '最近发现我家猫咪很少喝水，担心会导致泌尿系统问题。有什么好的方法可以鼓励猫咪多喝水吗？', NULL, 2, '2025-03-19 11:30:00', '2025-03-19 11:30:00', 90, 10, 15, 1);

-- ----------------------------
-- Records of community_comments
-- ----------------------------
INSERT INTO `community_comments` VALUES (1, 1, 1000000020, '可以尝试使用流动的水源，比如猫咪饮水机，很多猫咪更喜欢流动的水。', 0, '2025-03-15 11:20:00', 1);
INSERT INTO `community_comments` VALUES (2, 1, 1000000021, '新猫咪刚到家可能会比较紧张，建议给它一个安静的空间，不要急着抱它或逗它玩。', 0, '2025-03-15 12:05:00', 1);
INSERT INTO `community_comments` VALUES (3, 1, 1000000022, '关于洗澡，猫咪通常自己会清洁，不需要经常洗澡，一般2-3个月洗一次就可以了。', 0, '2025-03-15 13:40:00', 1);
INSERT INTO `community_comments` VALUES (4, 2, 1000000019, '好可爱的金毛！我也有一只，它们真的很粘人呢。', 0, '2025-03-16 15:10:00', 1);
INSERT INTO `community_comments` VALUES (5, 2, 1000000021, '金毛确实很适合家庭饲养，性格温顺，特别适合有小孩的家庭。', 0, '2025-03-16 16:30:00', 1);
INSERT INTO `community_comments` VALUES (6, 3, 1000000020, '感谢分享！我家猫咪最近食欲不太好，可能需要调整一下饮食。', 0, '2025-03-17 10:20:00', 1);
INSERT INTO `community_comments` VALUES (7, 3, 1000000022, '除了这些，还建议定期给猫咪驱虫，保持良好的肠道健康。', 0, '2025-03-17 11:15:00', 1);
INSERT INTO `community_comments` VALUES (8, 3, 1000000023, '我家猫咪很挑食，有什么好的方法可以让它接受新食物吗？', 0, '2025-03-17 12:30:00', 1);
INSERT INTO `community_comments` VALUES (9, 3, 1000000021, '可以尝试少量多次地引入新食物，混合在它喜欢的食物中，慢慢增加比例。', 8, '2025-03-17 13:45:00', 1);
INSERT INTO `community_comments` VALUES (10, 4, 1000000019, '训练狗狗确实需要很多耐心，特别是幼犬。', 0, '2025-03-18 17:05:00', 1);
INSERT INTO `community_comments` VALUES (11, 4, 1000000020, '我发现使用点击器训练效果很好，狗狗很快就能理解我的指令。', 0, '2025-03-18 18:20:00', 1);
INSERT INTO `community_comments` VALUES (12, 5, 1000000021, '可以尝试使用猫咪饮水机，或者在水中加入一点点金枪鱼汁来增加吸引力。', 0, '2025-03-19 12:15:00', 1);
INSERT INTO `community_comments` VALUES (13, 5, 1000000022, '也可以考虑增加湿粮的比例，湿粮含水量高，可以帮助猫咪摄入更多水分。', 0, '2025-03-19 13:30:00', 1);
INSERT INTO `community_comments` VALUES (14, 5, 1000000020, '我家猫咪也有这个问题，我在家里不同的地方放了几个水碗，效果不错。', 0, '2025-03-19 14:45:00', 1);
INSERT INTO `community_comments` VALUES (15, 5, 1000000023, '谢谢大家的建议，我会尝试这些方法！', 0, '2025-03-19 15:50:00', 1);

-- ----------------------------
-- Records of community_likes
-- ----------------------------
INSERT INTO `community_likes` VALUES (1, 1, 1000000020, '2025-03-15 11:30:00');
INSERT INTO `community_likes` VALUES (2, 1, 1000000021, '2025-03-15 12:10:00');
INSERT INTO `community_likes` VALUES (3, 1, 1000000022, '2025-03-15 13:45:00');
INSERT INTO `community_likes` VALUES (4, 2, 1000000019, '2025-03-16 15:15:00');
INSERT INTO `community_likes` VALUES (5, 2, 1000000021, '2025-03-16 16:35:00');
INSERT INTO `community_likes` VALUES (6, 2, 1000000022, '2025-03-16 17:20:00');
INSERT INTO `community_likes` VALUES (7, 2, 1000000023, '2025-03-16 18:05:00');
INSERT INTO `community_likes` VALUES (8, 3, 1000000019, '2025-03-17 10:25:00');
INSERT INTO `community_likes` VALUES (9, 3, 1000000020, '2025-03-17 11:20:00');
INSERT INTO `community_likes` VALUES (10, 3, 1000000022, '2025-03-17 12:35:00');
INSERT INTO `community_likes` VALUES (11, 4, 1000000019, '2025-03-18 17:10:00');
INSERT INTO `community_likes` VALUES (12, 4, 1000000020, '2025-03-18 18:25:00');

-- =============================================
-- 宠物图片更新脚本
-- =============================================

-- 更新宠物图片路径
-- 更新所有包含"猫"字的宠物为猫图片
UPDATE pets SET pet_image = 'cat.jpg' WHERE pet_name LIKE '%猫%';

-- 更新所有包含"狗"字的宠物为狗图片
UPDATE pets SET pet_image = 'dog.jpg' WHERE pet_name LIKE '%狗%';

-- 更新其他宠物（如果有的话）
UPDATE pets SET pet_image = 'dog.jpg' WHERE pet_image IS NULL OR pet_image = '';

-- =============================================
-- 宠物昵称更新脚本
-- =============================================

-- 更新宠物昵称为可爱的名字
UPDATE pets SET pet_name = '小橘', pet_type = '橘猫' WHERE pet_id = 1000000000;
UPDATE pets SET pet_name = '花花', pet_type = '家猫' WHERE pet_id = 1000000005;
UPDATE pets SET pet_name = '黑豆', pet_type = '理花猫' WHERE pet_id = 1000000010;
UPDATE pets SET pet_name = '奶糖', pet_type = '贵宾犬' WHERE pet_id = 1000000011;
UPDATE pets SET pet_name = '点点', pet_type = '比熊犬' WHERE pet_id = 1000000012;
UPDATE pets SET pet_name = '小黑', pet_type = '泰迪犬' WHERE pet_id = 1000000013;
UPDATE pets SET pet_name = '聪聪', pet_type = '边境牧羊犬' WHERE pet_id = 1000000014;
UPDATE pets SET pet_name = '胖胖', pet_type = '英国短毛猫' WHERE pet_id = 1000000015;
UPDATE pets SET pet_name = '阳阳', pet_type = '金毛犬' WHERE pet_id = 1000000017;
UPDATE pets SET pet_name = '毛毛', pet_type = '波斯猫' WHERE pet_id = 1000000018;
UPDATE pets SET pet_name = '蓝蓝', pet_type = '暹罗猫' WHERE pet_id = 1000000019;
UPDATE pets SET pet_name = '雪球', pet_type = '哈士奇' WHERE pet_id = 1000000020;
UPDATE pets SET pet_name = '布丁', pet_type = '拉布拉多' WHERE pet_id = 1000000021;
UPDATE pets SET pet_name = '银银', pet_type = '中华田园犬' WHERE pet_id = 1000000022;
UPDATE pets SET pet_name = '哈哈', pet_type = '拉布拉多' WHERE pet_id = 1000000023;
UPDATE pets SET pet_name = '阳光', pet_type = '金毛' WHERE pet_id = 1000000024;
UPDATE pets SET pet_name = '可可', pet_type = '拉布拉多' WHERE pet_id = 1000000025;
UPDATE pets SET pet_name = '旺财', pet_type = '中华田园犬' WHERE pet_id = 1000000026;
UPDATE pets SET pet_name = '巧克力', pet_type = '拉布拉多' WHERE pet_id = 1000000027;
UPDATE pets SET pet_name = '棉花糖', pet_type = '贵宾犬' WHERE pet_id = 1000000028;
UPDATE pets SET pet_name = '奶茶', pet_type = '拉布拉多' WHERE pet_id = 1000000029;

-- =============================================
-- 用户恢复脚本
-- =============================================

-- 恢复被暂停的用户账户
UPDATE users SET user_status = 0 WHERE user_account = 'cccc';

-- 创建一个新的测试账户（如果需要）
INSERT INTO users (user_id, user_account, user_password, user_name, user_sex, user_introduction, user_registertime, user_realname, user_phone, user_address, user_status, user_type)
VALUES (1000000099, 'test123', '123456', 'test123', 1, '测试账户', '2025-04-09', '测试用户', '13800138000', '测试地址', 0, 0)
ON DUPLICATE KEY UPDATE user_status = 0;

-- =============================================
-- 社区功能表结构和数据
-- =============================================

-- 创建社区帖子表
CREATE TABLE IF NOT EXISTS `community_post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `user_id` bigint NOT NULL COMMENT '发布者ID',
  `title` varchar(100) NOT NULL COMMENT '帖子标题',
  `content` text NOT NULL COMMENT '帖子内容',
  `post_image` varchar(255) DEFAULT NULL COMMENT '帖子图片',
  `post_type` tinyint NOT NULL DEFAULT '1' COMMENT '帖子类型：1饲养经验，2求助问答，3宠物展示，4宠物行为解读',
  `view_count` int NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `comment_count` int NOT NULL DEFAULT '0' COMMENT '评论数量',
  `like_count` int NOT NULL DEFAULT '0' COMMENT '点赞数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0正常，1删除',
  PRIMARY KEY (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_post_type` (`post_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='社区帖子表';

-- 创建社区评论表
CREATE TABLE IF NOT EXISTS `community_comment` (
  `comment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` bigint NOT NULL COMMENT '帖子ID',
  `user_id` bigint NOT NULL COMMENT '评论者ID',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父评论ID，0表示一级评论',
  `reply_to_user_id` bigint DEFAULT NULL COMMENT '回复的用户ID',
  `content` varchar(500) NOT NULL COMMENT '评论内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0正常，1删除',
  PRIMARY KEY (`comment_id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='社区评论表';

-- 创建社区点赞表
CREATE TABLE IF NOT EXISTS `community_like` (
  `like_id` bigint NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `post_id` bigint NOT NULL COMMENT '帖子ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`like_id`),
  UNIQUE KEY `uk_post_user` (`post_id`,`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='社区点赞表';

-- 插入一些测试数据
INSERT INTO `community_post` (`user_id`, `title`, `content`, `post_image`, `post_type`, `view_count`, `comment_count`, `like_count`, `create_time`, `status`)
VALUES
(1000000022, '如何照顾新领养的猫咪', '最近领养了一只小猫咪，想请教大家有什么好的照顾方法？猫咪大概3个月大，是橘猫。', 'cat.jpg', 1, 10, 2, 5, '2025-04-09 10:00:00', 0),
(1000000022, '我家狗狗不爱吃狗粮怎么办', '我家金毛最近不爱吃狗粮，只吃人类的食物，这样对健康好吗？', 'dog.jpg', 2, 15, 3, 7, '2025-04-09 11:00:00', 0),
(1000000022, '分享我家可爱的小猫咪', '这是我家的小猫咪，名叫小花，非常可爱，喜欢睡觉和玩毛线球。', 'cat.jpg', 3, 20, 5, 10, '2025-04-09 12:00:00', 0);

-- 插入一些评论
INSERT INTO `community_comment` (`post_id`, `user_id`, `parent_id`, `reply_to_user_id`, `content`, `create_time`, `status`)
VALUES
(1, 1000000022, 0, NULL, '建议多给猫咪准备一些玩具，让它有事可做。', '2025-04-09 10:30:00', 0),
(1, 1000000022, 0, NULL, '新猫咪需要适应环境，给它一个安静的空间很重要。', '2025-04-09 10:35:00', 0),
(2, 1000000022, 0, NULL, '可以尝试换不同品牌的狗粮，或者添加一些肉汤增加香味。', '2025-04-09 11:30:00', 0),
(3, 1000000022, 0, NULL, '好可爱的猫咪！毛色真漂亮！', '2025-04-09 12:30:00', 0),
(3, 1000000022, 0, NULL, '看起来很健康，你一定照顾得很好！', '2025-04-09 12:35:00', 0);
INSERT INTO `community_likes` VALUES (13, 4, 1000000021, '2025-03-18 19:40:00');
INSERT INTO `community_likes` VALUES (14, 5, 1000000021, '2025-03-19 12:20:00');
INSERT INTO `community_likes` VALUES (15, 5, 1000000022, '2025-03-19 13:35:00');


SET FOREIGN_KEY_CHECKS = 1;

