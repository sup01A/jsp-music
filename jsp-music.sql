/*
 Navicat Premium Dump SQL

 Source Server         : 192.168.1.8_3306
 Source Server Type    : MySQL
 Source Server Version : 80038 (8.0.38)
 Source Host           : 172.17.0.1:3306
 Source Schema         : jsp-music

 Target Server Type    : MySQL
 Target Server Version : 80038 (8.0.38)
 File Encoding         : 65001

 Date: 28/08/2024 22:42:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for Admin
-- ----------------------------
DROP TABLE IF EXISTS `Admin`;
CREATE TABLE `Admin` (
  `adminId` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `adminUsername` varchar(255) DEFAULT NULL,
  `adminPassword` varchar(255) DEFAULT NULL,
  `adminRegisterDate` datetime DEFAULT NULL,
  `adminLastDate` datetime DEFAULT NULL,
  PRIMARY KEY (`adminId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of Admin
-- ----------------------------
BEGIN;
INSERT INTO `Admin` (`adminId`, `adminUsername`, `adminPassword`, `adminRegisterDate`, `adminLastDate`) VALUES (0000000001, 'admin', 'admin', '2024-07-21 17:11:04', '2024-07-21 17:11:06');
INSERT INTO `Admin` (`adminId`, `adminUsername`, `adminPassword`, `adminRegisterDate`, `adminLastDate`) VALUES (0000000002, 'admin2', 'admin2', '2024-07-21 17:30:26', '2024-07-21 17:30:26');
COMMIT;

-- ----------------------------
-- Table structure for Album
-- ----------------------------
DROP TABLE IF EXISTS `Album`;
CREATE TABLE `Album` (
  `albumId` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `singerId` int DEFAULT NULL,
  `albumTitle` varchar(255) DEFAULT NULL,
  `albumPic` varchar(255) DEFAULT NULL,
  `albumPubDate` datetime DEFAULT NULL,
  `albumPubCom` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`albumId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of Album
-- ----------------------------
BEGIN;
INSERT INTO `Album` (`albumId`, `singerId`, `albumTitle`, `albumPic`, `albumPubDate`, `albumPubCom`) VALUES (0000000001, 1, 'Close Up', 'Close Up.jpg', '2024-07-16 01:38:33', '未知');
INSERT INTO `Album` (`albumId`, `singerId`, `albumTitle`, `albumPic`, `albumPubDate`, `albumPubCom`) VALUES (0000000002, 2, '夏至未至', 'xiazhiweizhi.jpg', '2024-07-16 17:12:15', '未知');
INSERT INTO `Album` (`albumId`, `singerId`, `albumTitle`, `albumPic`, `albumPubDate`, `albumPubCom`) VALUES (0000000003, 2, '勿念', 'wunian.jpg', '2024-07-16 22:02:18', '未知');
INSERT INTO `Album` (`albumId`, `singerId`, `albumTitle`, `albumPic`, `albumPubDate`, `albumPubCom`) VALUES (0000000004, 3, '崇拜', '1721666264168.jpg', '2024-07-23 00:00:00', '未知');
INSERT INTO `Album` (`albumId`, `singerId`, `albumTitle`, `albumPic`, `albumPubDate`, `albumPubCom`) VALUES (0000000005, 5, '小梦大半', '1724681028734.jpg', '2024-08-26 00:00:00', '未知');
COMMIT;

-- ----------------------------
-- Table structure for Comments
-- ----------------------------
DROP TABLE IF EXISTS `Comments`;
CREATE TABLE `Comments` (
  `commentId` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `songId` int NOT NULL,
  `commentText` varchar(255) DEFAULT NULL,
  `commentDate` datetime DEFAULT NULL,
  PRIMARY KEY (`commentId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of Comments
-- ----------------------------
BEGIN;
INSERT INTO `Comments` (`commentId`, `userId`, `songId`, `commentText`, `commentDate`) VALUES (0000000001, 1, 2, '很好听！！！！！！！！！！', '2024-07-16 23:45:56');
INSERT INTO `Comments` (`commentId`, `userId`, `songId`, `commentText`, `commentDate`) VALUES (0000000002, 2, 2, '真的好听', '2024-07-17 01:00:23');
INSERT INTO `Comments` (`commentId`, `userId`, `songId`, `commentText`, `commentDate`) VALUES (0000000003, 1, 1, 'asdasd', '2024-07-21 00:23:27');
INSERT INTO `Comments` (`commentId`, `userId`, `songId`, `commentText`, `commentDate`) VALUES (0000000004, 1, 2, '红红火火和', '2024-07-21 00:27:48');
INSERT INTO `Comments` (`commentId`, `userId`, `songId`, `commentText`, `commentDate`) VALUES (0000000005, 1, 2, '阿斯大赛的', '2024-07-21 00:35:46');
INSERT INTO `Comments` (`commentId`, `userId`, `songId`, `commentText`, `commentDate`) VALUES (0000000006, 1, 2, '123123', '2024-07-21 00:36:07');
INSERT INTO `Comments` (`commentId`, `userId`, `songId`, `commentText`, `commentDate`) VALUES (0000000007, 1, 2, '1231231', '2024-07-21 00:36:10');
INSERT INTO `Comments` (`commentId`, `userId`, `songId`, `commentText`, `commentDate`) VALUES (0000000008, 1, 2, '123123123', '2024-07-21 00:36:14');
INSERT INTO `Comments` (`commentId`, `userId`, `songId`, `commentText`, `commentDate`) VALUES (0000000009, 1, 4, '好听！', '2024-07-23 02:31:35');
INSERT INTO `Comments` (`commentId`, `userId`, `songId`, `commentText`, `commentDate`) VALUES (0000000010, 4, 1, '还不错', '2024-08-25 15:03:38');
INSERT INTO `Comments` (`commentId`, `userId`, `songId`, `commentText`, `commentDate`) VALUES (0000000011, 1, 1, 'buhaoting', '2024-08-26 22:22:00');
COMMIT;

-- ----------------------------
-- Table structure for NewSong
-- ----------------------------
DROP TABLE IF EXISTS `NewSong`;
CREATE TABLE `NewSong` (
  `newSongId` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `songId` int NOT NULL,
  PRIMARY KEY (`newSongId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of NewSong
-- ----------------------------
BEGIN;
INSERT INTO `NewSong` (`newSongId`, `songId`) VALUES (0000000001, 2);
COMMIT;

-- ----------------------------
-- Table structure for NormalUser
-- ----------------------------
DROP TABLE IF EXISTS `NormalUser`;
CREATE TABLE `NormalUser` (
  `userId` int unsigned NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) DEFAULT NULL,
  `userPassword` varchar(255) DEFAULT NULL,
  `userNickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `userSex` int DEFAULT NULL,
  `userEmail` varchar(255) DEFAULT NULL,
  `userAvatar` varchar(255) DEFAULT NULL,
  `userRegisterDate` datetime DEFAULT NULL,
  `userLastDate` datetime DEFAULT NULL,
  `userStatus` int DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of NormalUser
-- ----------------------------
BEGIN;
INSERT INTO `NormalUser` (`userId`, `userName`, `userPassword`, `userNickname`, `userSex`, `userEmail`, `userAvatar`, `userRegisterDate`, `userLastDate`, `userStatus`) VALUES (1, 'bobo', 'xxxx', '欧树波', 1, '2430504656@qq.com', '1721673553112.jpg', '2024-07-16 23:46:38', '2024-07-16 23:46:40', 0);
INSERT INTO `NormalUser` (`userId`, `userName`, `userPassword`, `userNickname`, `userSex`, `userEmail`, `userAvatar`, `userRegisterDate`, `userLastDate`, `userStatus`) VALUES (2, 'bobo2', 'bobo2', 'bobo02', 1, '2430504657@qq.com', NULL, '2024-07-17 00:59:48', '2024-07-17 00:59:50', 0);
INSERT INTO `NormalUser` (`userId`, `userName`, `userPassword`, `userNickname`, `userSex`, `userEmail`, `userAvatar`, `userRegisterDate`, `userLastDate`, `userStatus`) VALUES (4, 'bobo3', 'bobo3', 'bobo3', 0, '123@qq.com', '1724569371507.png', '2024-08-25 15:02:23', '2024-08-25 15:02:23', 0);
COMMIT;

-- ----------------------------
-- Table structure for NormalUserAlbum
-- ----------------------------
DROP TABLE IF EXISTS `NormalUserAlbum`;
CREATE TABLE `NormalUserAlbum` (
  `userId` int NOT NULL,
  `albumId` int NOT NULL,
  PRIMARY KEY (`userId`,`albumId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of NormalUserAlbum
-- ----------------------------
BEGIN;
INSERT INTO `NormalUserAlbum` (`userId`, `albumId`) VALUES (1, 1);
INSERT INTO `NormalUserAlbum` (`userId`, `albumId`) VALUES (1, 2);
COMMIT;

-- ----------------------------
-- Table structure for NormalUserSinger
-- ----------------------------
DROP TABLE IF EXISTS `NormalUserSinger`;
CREATE TABLE `NormalUserSinger` (
  `userId` int NOT NULL,
  `singerId` int NOT NULL,
  PRIMARY KEY (`userId`,`singerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of NormalUserSinger
-- ----------------------------
BEGIN;
INSERT INTO `NormalUserSinger` (`userId`, `singerId`) VALUES (1, 1);
INSERT INTO `NormalUserSinger` (`userId`, `singerId`) VALUES (1, 3);
INSERT INTO `NormalUserSinger` (`userId`, `singerId`) VALUES (4, 1);
COMMIT;

-- ----------------------------
-- Table structure for NormalUserSong
-- ----------------------------
DROP TABLE IF EXISTS `NormalUserSong`;
CREATE TABLE `NormalUserSong` (
  `userId` int NOT NULL,
  `songId` int NOT NULL,
  PRIMARY KEY (`userId`,`songId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of NormalUserSong
-- ----------------------------
BEGIN;
INSERT INTO `NormalUserSong` (`userId`, `songId`) VALUES (1, 2);
INSERT INTO `NormalUserSong` (`userId`, `songId`) VALUES (1, 4);
INSERT INTO `NormalUserSong` (`userId`, `songId`) VALUES (4, 1);
COMMIT;

-- ----------------------------
-- Table structure for Recommend
-- ----------------------------
DROP TABLE IF EXISTS `Recommend`;
CREATE TABLE `Recommend` (
  `recmdId` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `songId` int NOT NULL,
  PRIMARY KEY (`recmdId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of Recommend
-- ----------------------------
BEGIN;
INSERT INTO `Recommend` (`recmdId`, `songId`) VALUES (0000000001, 1);
INSERT INTO `Recommend` (`recmdId`, `songId`) VALUES (0000000003, 4);
COMMIT;

-- ----------------------------
-- Table structure for Singer
-- ----------------------------
DROP TABLE IF EXISTS `Singer`;
CREATE TABLE `Singer` (
  `singerId` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `singerName` varchar(255) DEFAULT NULL,
  `singerSex` int DEFAULT NULL,
  `singerThumbnail` varchar(255) DEFAULT NULL,
  `singerIntroduction` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`singerId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of Singer
-- ----------------------------
BEGIN;
INSERT INTO `Singer` (`singerId`, `singerName`, `singerSex`, `singerThumbnail`, `singerIntroduction`) VALUES (0000000001, '陈柏宇 ', 1, 'chenbaiyu.jpg', '粤语男歌手');
INSERT INTO `Singer` (`singerId`, `singerName`, `singerSex`, `singerThumbnail`, `singerIntroduction`) VALUES (0000000002, '岑宁儿', 2, 'chenninger.jpg', '女歌手');
INSERT INTO `Singer` (`singerId`, `singerName`, `singerSex`, `singerThumbnail`, `singerIntroduction`) VALUES (0000000003, '梁静茹', 2, '1721640930002.jpg', '华语流行乐女歌手、影视演员\r\n梁静茹（Fish Leong），原名梁翠萍，1978年6月16日出生于马来西亚森美兰州瓜拉庇劳县，祖籍广东省佛山市顺德区，华语流行乐女歌手、影视演员。1997年，因参加歌唱比赛而被李宗盛发掘。1999年，发行个人首张音乐专辑《一夜长大》正式出道。2000年，凭借歌曲《勇气》获得关注。2002年，发行个人第四张音乐专辑《Sunrise，我喜欢》。2004年，凭借专辑《美丽人生》入围第15届台湾金曲奖 .');
INSERT INTO `Singer` (`singerId`, `singerName`, `singerSex`, `singerThumbnail`, `singerIntroduction`) VALUES (0000000005, '陈粒', 2, '1724680368183.jpg', '中国内地唱作音乐人、音乐制作人\r\n陈粒，1990年7月26日出生于贵州省贵阳市，中国内地唱作音乐人、音乐制作人，毕业于上海对外经贸大学。2014年，随空想家乐队推出EP专辑《万象》；同年，其演唱的歌曲《奇妙能力歌》入围“第四届阿比鹿音乐奖”年度民谣单曲。2015年2月，首张个人音乐专辑《如也》上线；');
COMMIT;

-- ----------------------------
-- Table structure for Song
-- ----------------------------
DROP TABLE IF EXISTS `Song`;
CREATE TABLE `Song` (
  `songId` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `singerId` int DEFAULT NULL,
  `albumId` int DEFAULT NULL,
  `songTitle` varchar(255) DEFAULT NULL,
  `songPlaytimes` int DEFAULT NULL,
  `songDldtimes` int DEFAULT NULL,
  `songFile` varchar(255) DEFAULT NULL,
  `songLyric` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`songId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of Song
-- ----------------------------
BEGIN;
INSERT INTO `Song` (`songId`, `singerId`, `albumId`, `songTitle`, `songPlaytimes`, `songDldtimes`, `songFile`, `songLyric`) VALUES (0000000001, 1, 1, '你瞒我瞒', 0, 14, '陈柏宇 - 你瞒我瞒.flac', '陈柏宇 - 你瞒我瞒.js');
INSERT INTO `Song` (`songId`, `singerId`, `albumId`, `songTitle`, `songPlaytimes`, `songDldtimes`, `songFile`, `songLyric`) VALUES (0000000002, 2, 2, '追光者', 0, 23, '岑宁儿 - 追光者.flac', '追光者.js');
INSERT INTO `Song` (`songId`, `singerId`, `albumId`, `songTitle`, `songPlaytimes`, `songDldtimes`, `songFile`, `songLyric`) VALUES (0000000003, 2, 3, '勿念', 0, 17, '岑宁儿 - 勿念.flac', '勿念.js');
INSERT INTO `Song` (`songId`, `singerId`, `albumId`, `songTitle`, `songPlaytimes`, `songDldtimes`, `songFile`, `songLyric`) VALUES (0000000004, 3, 4, '崇拜', 0, 1, '1721672703288.flac', '1721672703288.js');
INSERT INTO `Song` (`songId`, `singerId`, `albumId`, `songTitle`, `songPlaytimes`, `songDldtimes`, `songFile`, `songLyric`) VALUES (0000000005, 5, 5, '光', 0, 0, '1724681469714.flac', '1724681469714.js');
COMMIT;

-- ----------------------------
-- Table structure for Task
-- ----------------------------
DROP TABLE IF EXISTS `Task`;
CREATE TABLE `Task` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `isFav` tinyint DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of Task
-- ----------------------------
BEGIN;
INSERT INTO `Task` (`id`, `title`, `isFav`) VALUES (0000000003, 'by some milk', 0);
INSERT INTO `Task` (`id`, `title`, `isFav`) VALUES (0000000004, 'eat lunch', 0);
INSERT INTO `Task` (`id`, `title`, `isFav`) VALUES (0000000005, 'play lol', 0);
INSERT INTO `Task` (`id`, `title`, `isFav`) VALUES (0000000012, 'buy a supreme t', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
