create table Suggestion
(
    id               int auto_increment not null primary key,
    article_id int null comment 'id',
    type int not null comment '批注类型 1新增 2修改 3删除',
    `index` int not null comment '批注开始位置',
    length int not null comment '批注长度',
    replace_content nvarchar(1024) null comment '替换内容',
    gmt_created      datetime           not null default CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified     datetime           null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '用户第三方登录授权表' collate = utf8mb4_unicode_ci;