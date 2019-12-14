CREATE TABLE `t_user_01` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `name` varchar(30) NOT NULL,
  `password` varchar(50) NOT NULL,
  `status` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `uuid` varchar(40) DEFAULT NULL,
  `email` varchar(200) NOT NULL,
  `end_time` datetime DEFAULT NULL,
   table_id int(11) not null,
   database_id int(11) not null,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user` (`name`,`password`),
  UNIQUE KEY `uuid` (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

