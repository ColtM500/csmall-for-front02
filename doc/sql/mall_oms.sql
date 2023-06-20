/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 5.7.33-log : Database - mall_oms
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mall_oms` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `mall_oms`;

/*Table structure for table `oms_order` */

DROP TABLE IF EXISTS `oms_order`;

CREATE TABLE `oms_order` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `sn` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户id',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人姓名（冗余，历史）',
  `mobile_phone` varchar(50) DEFAULT NULL COMMENT '联系电话（冗余，历史）',
  `telephone` varchar(50) DEFAULT NULL COMMENT '固定电话（冗余，历史）',
  `province_code` varchar(50) DEFAULT NULL COMMENT '省-代号（冗余，历史）',
  `province_name` varchar(50) DEFAULT NULL COMMENT '省-名称（冗余，历史）',
  `city_code` varchar(50) DEFAULT NULL COMMENT '市-代号（冗余，历史）',
  `city_name` varchar(50) DEFAULT NULL COMMENT '市-名称（冗余，历史）',
  `district_code` varchar(50) DEFAULT NULL COMMENT '区-代号（冗余，历史）',
  `district_name` varchar(50) DEFAULT NULL COMMENT '区-名称（冗余，历史）',
  `street_code` varchar(50) DEFAULT NULL COMMENT '街道-代号（冗余，历史）',
  `street_name` varchar(50) DEFAULT NULL COMMENT '街道-名称（冗余，历史）',
  `detailed_address` varchar(255) DEFAULT NULL COMMENT '详细地址（冗余，历史）',
  `tag` varchar(50) DEFAULT NULL COMMENT '标签（冗余，历史），例如：家、公司、学校',
  `payment_type` tinyint(3) unsigned DEFAULT NULL COMMENT '支付方式，0=银联，1=微信，2=支付宝',
  `state` tinyint(3) unsigned DEFAULT '0' COMMENT '状态，0=未支付，1=已关闭（超时未支付），2=已取消，3=已支付，4=已签收，5=已拒收，6=退款处理中，7=已退款',
  `reward_point` int(10) unsigned DEFAULT '0' COMMENT '积分',
  `amount_of_original_price` decimal(10,2) DEFAULT NULL COMMENT '商品原总价',
  `amount_of_freight` decimal(10,2) DEFAULT NULL COMMENT '运费总价',
  `amount_of_discount` decimal(10,2) DEFAULT NULL COMMENT '优惠金额',
  `amount_of_actual_pay` decimal(10,2) DEFAULT NULL COMMENT '实际支付',
  `gmt_order` datetime DEFAULT NULL COMMENT '下单时间',
  `gmt_pay` datetime DEFAULT NULL COMMENT '支付时间',
  `gmt_create` datetime DEFAULT NULL COMMENT '数据创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '数据最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sn` (`sn`)
) ENGINE=InnoDB AUTO_INCREMENT=5026 DEFAULT CHARSET=utf8mb4 COMMENT='订单数据表';

/*Data for the table `oms_order` */

insert  into `oms_order`(`id`,`sn`,`user_id`,`contact_name`,`mobile_phone`,`telephone`,`province_code`,`province_name`,`city_code`,`city_name`,`district_code`,`district_name`,`street_code`,`street_name`,`detailed_address`,`tag`,`payment_type`,`state`,`reward_point`,`amount_of_original_price`,`amount_of_freight`,`amount_of_discount`,`amount_of_actual_pay`,`gmt_order`,`gmt_pay`,`gmt_create`,`gmt_modified`) values (5025,'6d940076-e644-4c86-a744-a22c4200fba8',1,'张三','13314566544','13314566544','010','北京市','110100','北京市','110100','海淀区','11000','北三环西路','北京市海淀区北三环西路中鼎大厦B座',NULL,1,6,NULL,38999.94,0.00,0.00,38999.94,NULL,NULL,'2023-05-28 11:52:13','2023-05-28 11:52:13');

/*Table structure for table `oms_order_item` */

DROP TABLE IF EXISTS `oms_order_item`;

CREATE TABLE `oms_order_item` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) unsigned DEFAULT NULL COMMENT '订单id',
  `sku_id` bigint(20) unsigned DEFAULT NULL COMMENT 'SKU id',
  `title` varchar(255) DEFAULT NULL COMMENT '商品SKU标题（冗余，历史）',
  `bar_code` varchar(255) DEFAULT NULL COMMENT '商品SKU商品条型码（冗余）',
  `data` varchar(2500) DEFAULT NULL COMMENT '商品SKU全部属性，使用JSON格式表示（冗余）',
  `main_picture` varchar(255) DEFAULT NULL COMMENT '商品SKU图片URL（第1张）（冗余）',
  `price` decimal(10,2) DEFAULT NULL COMMENT '商品SKU单价（冗余，历史）',
  `quantity` smallint(5) unsigned DEFAULT NULL COMMENT '商品SKU购买数量',
  `gmt_create` datetime DEFAULT NULL COMMENT '数据创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '数据最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5025 DEFAULT CHARSET=utf8mb4 COMMENT='订单商品数据表';

/*Data for the table `oms_order_item` */

insert  into `oms_order_item`(`id`,`order_id`,`sku_id`,`title`,`bar_code`,`data`,`main_picture`,`price`,`quantity`,`gmt_create`,`gmt_modified`) values (5024,5025,2,'2021年新款，小米11 Ultra白色512G，8G超大内存120Hz高刷67w快充','','[{\"id\":1,\"name\":\"屏幕尺寸\",\"value\":\"6.1寸\"},{\"id\":3,\"name\":\"颜色\",\"value\":\"白色\"},{\"id\":5,\"name\":\"运行内存\",\"value\":\"8GB\"}]','//img14.360buyimg.com/n7/jfs/t1/181498/19/30614/69304/6374ea31Ee9e58bbe/24d33218c9b08852.jpg',6499.99,6,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
