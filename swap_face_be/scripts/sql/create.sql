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

create table `swap_face_task_topic`
(
    `id`   bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `en_name` varchar(256) NOT NULL COMMENT '英文名称',
    `cn_name` varchar(256) NOT NULL comment '中文名称',
    `ctime` datetime NOT NULL default now() COMMENT '创建时间',
    `creator` varchar(256) not null comment '创建者',
    `mtime` datetime NOT NULL default now() on update now() COMMENT '更新时间',
    `modifier` varchar(256) not null comment '修改者',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='换脸任务主题'

create table `swap_face_task_topic_base_image`
(
    `id`   bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `topic_id` varchar(256) NOT NULL COMMENT '主题id',
    `image_path` varchar(512) NOT NULL comment '图片地址',
    `ctime` datetime NOT NULL default now() COMMENT '创建时间',
    `creator` varchar(256) not null comment '创建者',
    `mtime` datetime NOT NULL default now() on update now() COMMENT '更新时间',
    `modifier` varchar(256) not null comment '修改者',
    PRIMARY KEY (`id`),
    unique key uniq_topic_image(`topic_id`, `image_path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='换脸任务主题模板信息'


create table `task_processor`
(
    `id`   bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `code` varchar(256) NOT NULL COMMENT '处理器名称',
    `status` int NOT NULL comment '处理器状态',
    `ctime` datetime NOT NULL default now() COMMENT '创建时间',
    `mtime` datetime NOT NULL default now() on update now() COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务处理器信息'
