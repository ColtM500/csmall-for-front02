/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 5.7.33-log : Database - mall_seckill
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mall_seckill` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `mall_seckill`;

/*Table structure for table `seckill_sku` */

DROP TABLE IF EXISTS `seckill_sku`;

CREATE TABLE `seckill_sku` (
  `id` bigint(20) unsigned NOT NULL COMMENT '记录id',
  `spu_id` bigint(20) unsigned DEFAULT NULL COMMENT 'SPU id',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `bar_code` varchar(255) DEFAULT NULL COMMENT '条型码',
  `attribute_template_id` bigint(20) unsigned DEFAULT NULL COMMENT '属性模版id',
  `specifications` varchar(2500) DEFAULT NULL COMMENT '全部属性，使用JSON格式表示（冗余）',
  `album_id` bigint(20) unsigned DEFAULT NULL COMMENT '相册id',
  `pictures` varchar(500) DEFAULT NULL COMMENT '组图URLs，使用JSON格式表示',
  `price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `seckill_price` decimal(10,2) DEFAULT NULL COMMENT '秒杀价',
  `stock` int(10) unsigned DEFAULT '0' COMMENT '秒杀库存',
  `stock_threshold` int(10) unsigned DEFAULT '0' COMMENT '库存预警阈值',
  `sales` int(10) unsigned DEFAULT '0' COMMENT '销量（冗余）',
  `comment_count` int(10) unsigned DEFAULT '0' COMMENT '买家评论数量总和（冗余）',
  `positive_comment_count` int(10) unsigned DEFAULT '0' COMMENT '买家好评数量总和（冗余）',
  `sort` tinyint(3) unsigned DEFAULT '0' COMMENT '自定义排序序号',
  `gmt_create` timestamp NULL DEFAULT NULL COMMENT '数据创建时间',
  `gmt_modified` timestamp NULL DEFAULT NULL COMMENT '数据最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SKU（Stock Keeping Unit）';

/*Data for the table `seckill_sku` */

insert  into `seckill_sku`(`id`,`spu_id`,`title`,`bar_code`,`attribute_template_id`,`specifications`,`album_id`,`pictures`,`price`,`seckill_price`,`stock`,`stock_threshold`,`sales`,`comment_count`,`positive_comment_count`,`sort`,`gmt_create`,`gmt_modified`) values (1,2,'2021年新款，小米11 Ultra黑色512G，16G超大内存120Hz高刷67w快充',NULL,1,'[{\"id\":1,\"name\":\"屏幕尺寸\",\"value\":\"6.1寸\"},{\"id\":3,\"name\":\"颜色\",\"value\":\"黑色\"},{\"id\":5,\"name\":\"运行内存\",\"value\":\"16GB\"}]',NULL,'//img14.360buyimg.com/n7/jfs/t1/181498/19/30614/69304/6374ea31Ee9e58bbe/24d33218c9b08852.jpg',6999.99,2000.00,100,50,0,0,0,0,'2022-05-08 18:15:05','2022-05-08 18:15:05');
insert  into `seckill_sku`(`id`,`spu_id`,`title`,`bar_code`,`attribute_template_id`,`specifications`,`album_id`,`pictures`,`price`,`seckill_price`,`stock`,`stock_threshold`,`sales`,`comment_count`,`positive_comment_count`,`sort`,`gmt_create`,`gmt_modified`) values (2,2,'2021年新款，小米11 Ultra白色512G，8G超大内存120Hz高刷67w快充',NULL,1,'[{\"id\":1,\"name\":\"屏幕尺寸\",\"value\":\"6.1寸\"},{\"id\":3,\"name\":\"颜色\",\"value\":\"白色\"},{\"id\":5,\"name\":\"运行内存\",\"value\":\"8GB\"}]',NULL,'//img14.360buyimg.com/n7/jfs/t1/181498/19/30614/69304/6374ea31Ee9e58bbe/24d33218c9b08852.jpg',6499.99,2000.00,100,50,0,0,0,0,'2022-05-08 18:15:05','2022-05-08 18:15:05');

/*Table structure for table `seckill_spu` */

DROP TABLE IF EXISTS `seckill_spu`;

CREATE TABLE `seckill_spu` (
  `id` bigint(20) unsigned NOT NULL COMMENT '记录id',
  `name` varchar(50) DEFAULT NULL COMMENT 'SPU名称',
  `type_number` varchar(50) DEFAULT NULL COMMENT 'SPU编号',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `description` varchar(255) DEFAULT NULL COMMENT '简介',
  `list_price` decimal(10,2) DEFAULT NULL COMMENT '显示秒杀价',
  `stock` int(10) unsigned DEFAULT '0' COMMENT '当前库存（冗余）',
  `stock_threshold` int(10) unsigned DEFAULT '0' COMMENT '库存预警阈值（冗余）',
  `unit` varchar(50) DEFAULT NULL COMMENT '计件单位',
  `brand_id` bigint(20) unsigned DEFAULT NULL COMMENT '品牌id',
  `brand_name` varchar(50) DEFAULT NULL COMMENT '品牌名称（冗余）',
  `category_id` bigint(20) unsigned DEFAULT NULL COMMENT '类别id',
  `category_name` varchar(50) DEFAULT NULL COMMENT '类别名称（冗余）',
  `attribute_template_id` bigint(20) unsigned DEFAULT NULL COMMENT '属性模版id',
  `album_id` bigint(20) unsigned DEFAULT NULL COMMENT '相册id',
  `pictures` varchar(500) DEFAULT NULL COMMENT '组图URLs，使用JSON数组表示',
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `gmt_create` timestamp NULL DEFAULT NULL COMMENT '数据创建时间',
  `gmt_modified` timestamp NULL DEFAULT NULL COMMENT '数据最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SPU（Standard Product Unit）';

/*Data for the table `seckill_spu` */

insert  into `seckill_spu`(`id`,`name`,`type_number`,`title`,`description`,`list_price`,`stock`,`stock_threshold`,`unit`,`brand_id`,`brand_name`,`category_id`,`category_name`,`attribute_template_id`,`album_id`,`pictures`,`start_time`,`end_time`,`gmt_create`,`gmt_modified`) values (2,'小米11 Ultra','M112021','小米11 Ultra（M112021）','2021年最新旗舰机',2000.00,100,20,'部',2,'小米',3,'智能手机',1,NULL,'//img13.360buyimg.com/n7/jfs/t1/148953/1/34948/40281/64891e4eF95c6e5a6/c26d682bb4659786.jpg','2022-03-31 00:00:00','2030-12-01 00:00:00','2022-03-31 11:16:37','2022-03-31 15:22:36');

/*Table structure for table `seckill_stock_log` */

DROP TABLE IF EXISTS `seckill_stock_log`;

CREATE TABLE `seckill_stock_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sku_id` bigint(20) DEFAULT NULL,
  `order_sn` varchar(64) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

/*Data for the table `seckill_stock_log` */

/*Table structure for table `seckill_success` */

DROP TABLE IF EXISTS `seckill_success`;

CREATE TABLE `seckill_success` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) DEFAULT NULL COMMENT '用户id',
  `order_sn` varchar(36) DEFAULT NULL,
  `user_phone` varchar(50) DEFAULT NULL COMMENT '手机号码(冗余)',
  `sku_id` bigint(11) DEFAULT NULL COMMENT 'SKU id',
  `title` varchar(255) DEFAULT NULL COMMENT '商品SKU标题（冗余）',
  `bar_code` varchar(255) DEFAULT NULL,
  `data` varchar(2500) DEFAULT NULL,
  `main_picture` varchar(255) DEFAULT NULL COMMENT '商品SKU图片URL（第1张）',
  `seckill_price` decimal(10,2) DEFAULT NULL COMMENT '秒杀商品单价(加入时)',
  `quantity` smallint(3) DEFAULT NULL COMMENT '秒杀商品数量',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '数据最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='秒杀成功表格';

/*Data for the table `seckill_success` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
