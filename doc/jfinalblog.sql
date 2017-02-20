/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.6.24 : Database - jfinalblog
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`jfinalblog` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `jfinalblog`;

/*Table structure for table `tb_blog` */

DROP TABLE IF EXISTS `tb_blog`;

CREATE TABLE `tb_blog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `authorId` bigint(20) DEFAULT NULL,
  `content` longtext,
  `createAt` datetime DEFAULT NULL,
  `featured` int(11) NOT NULL,
  `category` int(11) DEFAULT NULL,
  `privacy` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `summary` varchar(600) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `title` varchar(64) DEFAULT NULL,
  `views` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_category` */

DROP TABLE IF EXISTS `tb_category`;

CREATE TABLE `tb_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `status` int(2) DEFAULT '0',
  `count` int(11) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `tb_category` */

insert  into `tb_category`(`id`,`name`,`status`,`count`,`description`) values (1,'原创',0,0,'原创'),(2,'转载',0,0,'转载'),(3,'其他',0,0,'其他');

/*Table structure for table `tb_login_log` */

DROP TABLE IF EXISTS `tb_login_log`;

CREATE TABLE `tb_login_log` (
  `userId` int(11) NOT NULL,
  `loginAt` datetime NOT NULL,
  `ip` varchar(100) DEFAULT NULL,
  KEY `userId_index` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*Table structure for table `tb_session` */

DROP TABLE IF EXISTS `tb_session`;

CREATE TABLE `tb_session` (
  `id` varchar(33) NOT NULL,
  `userId` int(11) NOT NULL,
  `expireAt` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `tb_tag` */

DROP TABLE IF EXISTS `tb_tag`;

CREATE TABLE `tb_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `status` int(2) DEFAULT '0',
  `count` int(11) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_user` */

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickName` varchar(50) NOT NULL,
  `userName` varchar(150) NOT NULL,
  `password` varchar(150) NOT NULL,
  `salt` varchar(150) NOT NULL,
  `status` int(11) NOT NULL,
  `avatar` varchar(100) NOT NULL,
  `createAt` datetime NOT NULL,
  `ip` varchar(100) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `tb_user` */

insert  into `tb_user`(`id`,`nickName`,`userName`,`password`,`salt`,`status`,`avatar`,`createAt`,`ip`,`description`) values (1,'SPPan','whoismy8023@163.com','a1f0917284a75c2c45dfeefd9040ce01144407c1a33d1bc3c45153ceb9d12d72','zmxyyZJkE-N6JjRhujp6U8l4Yu7vuQDZ',1,'0/1.jpg','2017-01-24 09:41:30','127.0.0.1','这是描述吧');


/*Table structure for table `tb_options` */

DROP TABLE IF EXISTS `tb_options`;

CREATE TABLE `tb_options` (
  `key` varchar(255) NOT NULL DEFAULT '' COMMENT 'key关键字',
  `value` text NOT NULL COMMENT '值',
  `desc` varchar(255) NOT NULL DEFAULT '' COMMENT '说明',
  PRIMARY KEY (`key`),
  UNIQUE KEY `key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_options` */

insert  into `tb_options`(`key`,`value`,`desc`) values ('siteAboutMe','<p>\r\n    <span style=\"color: rgb(146, 208, 80);\"><strong>jfinalblog</strong></span>\r\n</p>\r\n<p>\r\n    <span style=\"color: rgb(255, 0, 0);\">我的博客系统！</span>\r\n</p>\r\n<p>\r\n    <span style=\"color: rgb(0, 176, 80);\">由SPPan开发，用于记录程序员生涯中遇到的一些问题！</span>\r\n</p>\r\n<p>\r\n    <span style=\"color: rgb(0, 176, 240);\">恩</span>、<span style=\"color: rgb(255, 255, 0);\">没错</span>、<span style=\"color: rgb(112, 48, 160);\">就是这样。。。。</span>\r\n</p>','关于我'),('siteDescription','jfinalblog SPPan博客系统','站点描述'),('siteDomain','http://localhost','网站域名'),('siteName','jfinalblog','网站名称');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
