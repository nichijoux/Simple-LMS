/*
Navicat MySQL Data Transfer

Source Server         : nichijou
Source Server Version : 80023
Source Host           : localhost:3306
Source Database       : library

Target Server Type    : MYSQL
Target Server Version : 80023
File Encoding         : 65001

Date: 2021-04-25 20:20:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `BookID` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '唯一主键,ID',
  `BookName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '书名',
  `Author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '作者',
  `BookType` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '书的类别',
  `BookDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `BookNum` int unsigned NOT NULL DEFAULT '0' COMMENT '剩余数量',
  PRIMARY KEY (`BookID`),
  KEY `BookType` (`BookType`),
  CONSTRAINT `book_ibfk_1` FOREIGN KEY (`BookType`) REFERENCES `booktype` (`BookType`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `book_chk_1` CHECK ((`BookNum` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES ('00000000001', '三体', '刘慈欣', '科幻文学', '2014-10-01 00:00:00', '19734');
INSERT INTO `book` VALUES ('00000000002', '模式识别', '边肇琪', '工学', '2004-02-16 21:43:43', '3287');
INSERT INTO `book` VALUES ('00000000003', '高等数学', '林益', '理学', '1990-06-02 00:00:00', '1234');
INSERT INTO `book` VALUES ('00000000004', '海战世界', '张三', '军事类', '2011-03-11 00:00:00', '283');
INSERT INTO `book` VALUES ('00000000005', '贫嘴张大民的辛福生活', '谷城来', '网络文学', '1999-06-12 00:00:00', '2773');
INSERT INTO `book` VALUES ('00000000006', 'Java程序设计', 'James Gosling', '工学', '1996-06-01 00:00:00', '18355');
INSERT INTO `book` VALUES ('00000000007', '孙子兵法', '孙武', '军事类', '0016-06-19 00:00:00', '327');
INSERT INTO `book` VALUES ('00000000008', '西线无战事', '雷马克', '军事类', '1916-06-19 00:00:00', '120');
INSERT INTO `book` VALUES ('00000000009', '福尔摩斯探案集', '柯南·道尔', '推理文学', '1886-04-17 00:00:00', '32488');
INSERT INTO `book` VALUES ('00000000010', '苏菲的世界', '乔斯坦·贾德', '哲学类', '1991-03-10 00:00:00', '12883');
INSERT INTO `book` VALUES ('00000000011', '樱之诗', 'SCA-自', '网络文学', '2015-07-30 00:00:00', '190');
INSERT INTO `book` VALUES ('00000000012', '大川之水', '芥川龙之介', '经典文学', '1914-04-17 00:00:00', '372');
INSERT INTO `book` VALUES ('00000000013', '老年', '芥川龙之介', '经典文学', '1914-04-10 00:00:00', '218');
INSERT INTO `book` VALUES ('00000000014', '罗生门', '芥川龙之介', '经典文学', '1915-11-21 00:00:00', '1267');
INSERT INTO `book` VALUES ('00000000015', '运气', '芥川龙之介', '经典文学', '1917-01-17 00:00:00', '231');
INSERT INTO `book` VALUES ('00000000016', '忠义', '芥川龙之介', '经典文学', '1917-03-17 00:00:00', '324');
INSERT INTO `book` VALUES ('00000000017', '斯泰尔斯庄园奇案', '阿加莎·克里斯蒂', '推理文学', '1920-03-17 00:00:00', '21839');
INSERT INTO `book` VALUES ('00000000018', '斯泰尔斯的神秘案件', '阿加莎·克里斯蒂', '推理文学', '1920-03-17 00:00:00', '12783');
INSERT INTO `book` VALUES ('00000000019', '罗杰疑案', '阿加莎·克里斯蒂', '推理文学', '1926-03-20 00:00:00', '128932');
INSERT INTO `book` VALUES ('00000000020', '东方快车谋杀案', '阿加莎·克里斯蒂', '推理文学', '1934-01-17 00:00:00', '1287832');
INSERT INTO `book` VALUES ('00000000021', 'ABC谋杀案', '阿加莎·克里斯蒂', '推理文学', '1936-01-17 00:00:00', '32877');
INSERT INTO `book` VALUES ('00000000022', '尼罗河上的惨案', '阿加莎·克里斯蒂', '推理文学', '1937-01-17 00:00:00', '138787');
INSERT INTO `book` VALUES ('00000000023', '呐喊', '鲁迅', '经典文学', '1923-08-02 00:00:00', '1289');
INSERT INTO `book` VALUES ('00000000024', '彷徨', '鲁迅', '经典文学', '1926-08-02 00:00:00', '3243');
INSERT INTO `book` VALUES ('00000000025', '且介亭杂文', '鲁迅', '经典文学', '1937-07-02 00:00:00', '3178');
INSERT INTO `book` VALUES ('00000000026', '开拓者', '蒋子龙', '经典文学', '1971-04-17 00:00:00', '129');
INSERT INTO `book` VALUES ('00000000027', '神曲', '阿利盖利·但丁', '宗教类', '1307-04-20 17:16:47', '217');
INSERT INTO `book` VALUES ('00000000028', '软件设计师教程-第5版', '褚华', '工学', '2021-04-14 17:18:20', '12732');
INSERT INTO `book` VALUES ('00000000029', '希腊棺材之谜', '埃勒里·奎因', '推理文学', '1947-04-22 00:00:00', '8371');

-- ----------------------------
-- Table structure for booktype
-- ----------------------------
DROP TABLE IF EXISTS `booktype`;
CREATE TABLE `booktype` (
  `BookType` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '书的类别',
  PRIMARY KEY (`BookType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of booktype
-- ----------------------------
INSERT INTO `booktype` VALUES ('军事类');
INSERT INTO `booktype` VALUES ('哲学类');
INSERT INTO `booktype` VALUES ('宗教类');
INSERT INTO `booktype` VALUES ('工学');
INSERT INTO `booktype` VALUES ('推理文学');
INSERT INTO `booktype` VALUES ('理学');
INSERT INTO `booktype` VALUES ('科幻文学');
INSERT INTO `booktype` VALUES ('经典文学');
INSERT INTO `booktype` VALUES ('网络文学');

-- ----------------------------
-- Table structure for borrow
-- ----------------------------
DROP TABLE IF EXISTS `borrow`;
CREATE TABLE `borrow` (
  `BorrowID` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `UserID` int(11) unsigned zerofill NOT NULL,
  `BookID` int(11) unsigned zerofill NOT NULL,
  `BorrowDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借书时间',
  `ReturnDate` datetime DEFAULT NULL COMMENT '还书时间',
  PRIMARY KEY (`BorrowID`,`UserID`,`BookID`),
  KEY `borrowBookID` (`BookID`),
  KEY `borrowUserID` (`UserID`),
  CONSTRAINT `borrowBookID` FOREIGN KEY (`BookID`) REFERENCES `book` (`BookID`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `borrowUserID` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of borrow
-- ----------------------------
INSERT INTO `borrow` VALUES ('00000000001', '00000000001', '00000000001', '2021-04-25 17:20:24', '2021-04-25 18:33:20');
INSERT INTO `borrow` VALUES ('00000000002', '00000000001', '00000000003', '2021-04-25 17:20:29', '2021-04-25 19:28:05');
INSERT INTO `borrow` VALUES ('00000000003', '00000000001', '00000000007', '2021-04-25 17:20:46', null);
INSERT INTO `borrow` VALUES ('00000000004', '00000000002', '00000000013', '2021-04-25 17:20:51', null);
INSERT INTO `borrow` VALUES ('00000000005', '00000000003', '00000000012', '2021-04-25 17:20:57', null);
INSERT INTO `borrow` VALUES ('00000000006', '00000000003', '00000000015', '2021-04-25 17:21:54', null);
INSERT INTO `borrow` VALUES ('00000000007', '00000000001', '00000000006', '2021-04-25 18:55:33', '2021-04-25 19:24:22');
INSERT INTO `borrow` VALUES ('00000000008', '00000000001', '00000000003', '2021-04-25 19:28:50', null);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `UserID` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `UserName` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `PassWord` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CreateDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UserType` tinyint(1) NOT NULL DEFAULT '0' COMMENT '用户类型,true为管理员,false为普通用户',
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('00000000001', 'test', '123456', '2018-06-12 21:52:03', '1');
INSERT INTO `user` VALUES ('00000000002', 'root', '52583344zh?', '2021-04-22 19:00:04', '1');
INSERT INTO `user` VALUES ('00000000003', '喜洋洋', '123456', '2021-04-18 23:00:16', '0');
