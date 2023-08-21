create table `custom_task_detail_info`
(
    `id`   bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `task_type` varchar(256) NOT NULL COMMENT '任务类型',
    `task_info` text comment '任务信息',
    `status` int not null comment '任务状态',
    `result` text comment '任务结果',
    `finish_time` datetime DEFAULT NULL COMMENT '任务结束时间',
    `ctime` datetime NOT NULL COMMENT '创建时间',
    `creator` varchar(256) not null comment '创建者',
    `mtime` datetime NOT NULL COMMENT '更新时间',
    `modifier` varchar(256) not null comment '修改者',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='自定义任务信息'

