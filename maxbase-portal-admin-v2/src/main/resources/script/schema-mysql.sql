
drop table if exists user;
create table user (
  id  int(9) auto_increment primary key,
  username varchar(64) not null comment '用户名',
  password varchar(255) comment '密码',
  created_by int(11) COMMENT '创建人',
  created_date datetime,
  last_modified_by int(11) COMMENT '更新人',
  last_modified_date datetime
  )
  engine = innodb
  default charset = utf8
  comment ='用户表';

drop table if exists  custom_resource;
create table  custom_resource (
  id  int(9) auto_increment primary key,
  key_word varchar(255) not null comment '关键字',
  key_value varchar(255) not null comment '值：如果是图片是url,如果是文字就是text',
	type varchar(50) not null comment '类型：文字和图片',
	description varchar(255) comment '描述：图片size',
  created_by int(11) COMMENT '创建人',
  created_date datetime,
  last_modified_by int(11)  COMMENT '更新人',
  last_modified_date datetime
  )
  engine = innodb
  default charset = utf8
  comment ='自定义资源表';

drop table if exists business_type;
create table business_type (
  id  int(9) auto_increment primary key,
  business_type_name varchar(50) not null comment '名称',
  platform varchar(50) comment '平台,有两种：web,mobile',
  order_no int(9) not null comment '排序',
  created_by int(11) COMMENT '创建人',
  created_date datetime,
  last_modified_by int(11) COMMENT '更新人',
  last_modified_date datetime
)
  engine = innodb
  default charset = utf8
  comment ='业务分类';

drop table if exists web_navigation;
create table web_navigation (
  id  int(9) auto_increment primary key,
  web_navigation_code varchar(50) not null comment '编码',
  web_navigation_name varchar(50) not null comment '名称',
  icon_path varchar(500) not null comment '图标路径',
  description varchar(100) null comment '描述',
  application_code varchar(100) null comment 'application code',
  business_type_id varchar(100) not null comment '业务分类Id',
  web_url varchar(500) null comment 'web url',
  app_download_url  varchar(500) null comment 'app download url',
  navigation_type varchar(100) not null comment '导航类型,有三种:application,navigation,applicationGroup',
  status varchar(100) not null comment '状态,有两种:normal,developing',
  order_no int(9) not null comment '排序',
  created_by int(11) COMMENT '创建人',
  created_date datetime,
  last_modified_by int(11) COMMENT '更新人',
  last_modified_date datetime
)
  engine = innodb
  default charset = utf8
  comment ='Web导航';


drop table if exists application_group_item;
create table application_group_item (
  id  int(9) auto_increment primary key,
  web_navigation_id int(9) not null comment 'Web导航Id',
  application_code varchar(100) null comment 'application code',
  application_group_item_name varchar(50) not null comment '名称',
  icon_path varchar(500) NOT NULL COMMENT '图标路径',
  url varchar(500) null comment 'url',
  order_no int(9) not null comment '排序',
  created_by int(11) COMMENT '创建人',
  created_date datetime,
  last_modified_by int(11) COMMENT '更新人',
  last_modified_date datetime
)
  engine = innodb
  default charset = utf8
  comment ='应用组项目';


drop table if exists mobile_navigation;
create table mobile_navigation (
  id  int(9) auto_increment primary key,
  mobile_navigation_code varchar(50) not null comment '编码',
  mobile_navigation_name varchar(50) not null comment '名称',
  icon_path varchar(500) not null comment '图标路径',
  description varchar(100) null comment '描述',
  application_code varchar(100) null comment 'application code',
  business_type_id varchar(100) not null comment '业务分类Id',
  url varchar(500) null comment '移动版网页地址',
  url_scheme  varchar(500) null comment 'app启动地址',
  order_no int(9) not null comment '排序',
  created_by int(11) COMMENT '创建人',
  created_date datetime,
  last_modified_by int(11) COMMENT '更新人',
  last_modified_date datetime
)
  engine = innodb
  default charset = utf8
  comment ='Mobile导航';

drop table if exists operation_record;
create table operation_record (
  id  int(9) auto_increment primary key,
  url varchar(500) not null comment '存储的url',
  ip varchar(50) comment '远程用户的ip地址',
  created_by int(11) COMMENT '创建人',
  created_date datetime,
  last_modified_by int(11) COMMENT '更新人',
  last_modified_date datetime
)
  engine = innodb
  default charset = utf8
  comment ='业务分类';



INSERT INTO `user` VALUES (1, 'admin', '123456', NULL, '2019-07-26 15:16:59', NULL, '2019-07-30 14:27:23');
INSERT INTO `custom_resource` VALUES (1, 'index_img', 'index_img.jpg', 'image', '系统导览图片', NULL, '2019-07-30 16:43:35', NULL, '2019-07-30 16:43:35');
INSERT INTO `custom_resource` VALUES (2, 'system_name', 'MaxISP', 'text', '系统名称', NULL, '2019-08-01 14:03:16', NULL, '2019-08-01 14:03:16');