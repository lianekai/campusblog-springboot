-- MySQL dump 10.13  Distrib 8.0.17
--
-- Host: localhost    Database: myblog
-- ------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author` int(11) NOT NULL,
  `originalAuthor` varchar(255) NOT NULL,
  `originalUrl` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `content` longtext NOT NULL,
  `tags` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `category` smallint(6) NOT NULL,
  `publishDate` datetime NOT NULL,
  `updateDate` datetime NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `article_id_index` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `article_likes_record`
--

DROP TABLE IF EXISTS `article_likes_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_likes_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `articleId` bigint(20) NOT NULL,
  `userId` int(11) NOT NULL,
  `likeDate` datetime NOT NULL,
  `isRead` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `article_likes_record_articleId_userId_index` (`articleId`,`userId`),
  KEY `article_likes_record_id_index` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `category_id_index` (`id`),
  KEY `category_name_index` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment_likes_record`
--

DROP TABLE IF EXISTS `comment_likes_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_likes_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `comment_likes_record_cid_uid_index` (`cid`,`uid`),
  KEY `comment_likes_record_id_index` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment_record`
--

DROP TABLE IF EXISTS `comment_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_record` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `pId` bigint(20) NOT NULL,
  `articleId` bigint(20) NOT NULL,
  `answererId` int(11) NOT NULL,
  `respondentId` int(11) NOT NULL,
  `createdAt` datetime NOT NULL,
  `content` text NOT NULL,
  `isRead` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `comment_record_articleId_answererId_index` (`articleId`,`answererId`),
  KEY `comment_record_id_index` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `info` varchar(255) DEFAULT NULL,
  `userId` int(11) NOT NULL,
  `createdAt` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `feedback_id_index` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `friend_link`
--

DROP TABLE IF EXISTS `friend_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `friend_link` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blogger` varchar(40) NOT NULL,
  `url` varchar(100) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `friend_link_blogger_index` (`blogger`),
  KEY `friend_link_id_index` (`id`),
  KEY `friend_link_url_index` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `leave_message_likes_record`
--

DROP TABLE IF EXISTS `leave_message_likes_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `leave_message_likes_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pageName` varchar(255) NOT NULL,
  `pId` int(11) NOT NULL,
  `likerId` int(11) NOT NULL,
  `likeDate` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `leave_message_likes_record_id_index` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `leave_message_record`
--

DROP TABLE IF EXISTS `leave_message_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `leave_message_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pageName` varchar(255) NOT NULL,
  `pId` int(255) NOT NULL,
  `answererId` int(11) NOT NULL,
  `respondentId` int(11) NOT NULL,
  `createdAt` datetime NOT NULL,
  `likes` int(11) NOT NULL,
  `content` text NOT NULL,
  `isRead` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `leave_message_record_id_index` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `read_record`
--

DROP TABLE IF EXISTS `read_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `read_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aid` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `duration` double DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `read_record_id_index` (`id`),
  KEY `read_record_aid_uid_index` (`aid`,`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `role_id_index` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `site_info`
--

DROP TABLE IF EXISTS `site_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `siteName` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `site_info_id_index` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `size` int(11) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tags_id_index` (`id`),
  KEY `tags_name_index` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `trueName` varchar(255) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `personalBrief` varchar(255) DEFAULT NULL,
  `recentlyLanded` datetime DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `face` varchar(100) DEFAULT '/img/face/default.jpg',
  `registerTime` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1',
  `type` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_username_uindex` (`username`),
  KEY `user_id_index` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=539 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  KEY `user_role_user_id_role_id_index` (`user_id`,`role_id`),
  KEY `user_role_user_id_index` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visit_record`
--

DROP TABLE IF EXISTS `visit_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(100) DEFAULT NULL,
  `visitor` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `visit_record_id_index` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=439 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-08-07 16:52:35
