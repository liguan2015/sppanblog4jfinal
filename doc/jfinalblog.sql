-- ----------------------------
-- Table structure for `tb_user`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nickName` VARCHAR(50) NOT NULL,
  `userName` VARCHAR(150) NOT NULL,
  `password` VARCHAR(150) NOT NULL,
  `salt` VARCHAR(150) NOT NULL,
  `status` INT(11) NOT NULL,
  `avatar` VARCHAR(100) NOT NULL,
  `createAt` DATETIME NOT NULL,
  `ip` VARCHAR(100) DEFAULT NULL,
  `description` varchar(500),
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Data for the table `tb_user` */
insert  into `tb_user`(`id`,`nickName`,`userName`,`password`,`salt`,`status`,`avatar`,`createAt`,`ip`,`description`) values (1,'SPPan','whoismy8023@163.com','a1f0917284a75c2c45dfeefd9040ce01144407c1a33d1bc3c45153ceb9d12d72','zmxyyZJkE-N6JjRhujp6U8l4Yu7vuQDZ',1,'0/1.jpg','2017-01-24 09:41:30','127.0.0.1','这是描述描述');

-- ----------------------------
-- Table structure for `tb_session`
-- ----------------------------
DROP TABLE IF EXISTS `tb_session`;
CREATE TABLE `tb_session` (
  `id` VARCHAR(33) NOT NULL,
  `userId` INT(11) NOT NULL,
  `expireAt` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `tb_login_log`
-- ----------------------------
DROP TABLE IF EXISTS `tb_login_log`;
CREATE TABLE `tb_login_log` (
  `userId` INT(11) NOT NULL,
  `loginAt` DATETIME NOT NULL,
  `ip` VARCHAR(100) DEFAULT NULL,
  KEY `userId_index` (`userId`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

/*Table structure for table `tb_category` */

DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `status` int(2) DEFAULT '0',
  `count` int(11) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_tag` */

DROP TABLE IF EXISTS `tb_tag`;
CREATE TABLE `tb_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `status` int(2) DEFAULT '0',
  `count` int(11) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;