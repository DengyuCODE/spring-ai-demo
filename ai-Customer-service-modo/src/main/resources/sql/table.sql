#用户表
CREATE TABLE `user` (
                        `id_` bigint(20) NOT NULL,
                        `account_` varchar(30) NOT NULL COMMENT '账户',
                        `user_name` varchar(30) NOT NULL COMMENT '用户名称',
                        `phone_` varchar(20) DEFAULT NULL COMMENT '手机号',
                        `remark_` varchar(100) DEFAULT '' COMMENT '备注',
                        `enable_` tinyint(1) DEFAULT '1',
                        `create_by` bigint(20) NOT NULL DEFAULT '0',
                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `update_by` bigint(20) NOT NULL DEFAULT '0',
                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (`id_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
#预定信息表
CREATE TABLE `booking` (
                            `id_` bigint(20) NOT NULL,
                            `user_id` bigint(20) NOT NULL COMMENT '用户id',
                            `booking_num` varchar(30) NOT NULL COMMENT '预定号',
                            `booking_date`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '预定班次时间',
                            `from_` varchar(30) NOT NULL COMMENT '出发地',
                            `to_` varchar(30) NOT NULL COMMENT '目的地',
                            `status_` tinyint(2) DEFAULT 1 COMMENT '状态',
                            `level_` tinyint(1) DEFAULT '1' COMMENT '舱位等级',
                            `remark_` varchar(100) DEFAULT '' COMMENT '备注',
                            `enable_` tinyint(1) DEFAULT '1',
                            `create_by` bigint(20) NOT NULL DEFAULT '0',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `update_by` bigint(20) NOT NULL DEFAULT '0',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预定信息表';