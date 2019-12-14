create database if not exists chatroom;
use chatroom;

drop table if exists `t_user`;
drop table if exists `t_room`;
drop table if exists `t_room_statistic`;
drop table if exists `t_room_subscription`;

create table `t_user`(
    `id` bigint primary key auto_increment not null,
    `name` varchar(20) not null,
    `update_time` bigint not null,
    `create_time` bigint not null
)engine=innodb default character set utf8mb4;

create table `t_room`(
    `id` bigint primary key auto_increment not null,
    `creator` bigint not null,
    `description` varchar(100),
    `delete_flag` tinyint,
    `update_time` bigint not null,
    `create_time` bigint not null
)engine=innodb default character set utf8mb4;

-- 房间订阅表
create table `t_room_subscription`(
    `id` bigint unsigned auto_increment not null,
    `user_id` bigint not null,
    `level` int unsigned default 0 not null,
    `delete_flag` tinyint not null,
    `create_time` bigint not null,
    `update_time` bigint not null
);

-- 用户在某个房间的统计数据
create table `t_user_subscription_statistic`(
    `id` bigint unsigned auto_increment not null,
    `user_id` bigint not null,
    `room_id` bigint not null,
    `message_count` bigint default 0 comment '在进入房间和离开房间之间发送的消息条数',
    `last_entry_time` bigint not null,
    `last_exit_time` bigint
);