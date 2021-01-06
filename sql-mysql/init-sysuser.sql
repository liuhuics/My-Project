--用户表
CREATE TABLE SYS_USER
    (
        ID  int NOT NULL AUTO_INCREMENT comment '主键',
		    ROLE_ID int comment '角色 id',
        USER_NAME VARCHAR(16) comment '用户名',
        USER_PWD VARCHAR(64) comment '用户密码',
        PHONE VARCHAR(11) comment '手机号',
        DEPART_ID int default 1 not null comment '机构id',
        STATUS CHAR(1) default 1 not null comment '用户状态，1-有效，0-无效',
        CREATE_TIME datetime default now() not null comment '创建时间',
		    UPDATE_TIME datetime default now() not null comment '更新时间',
        PRIMARY KEY (ID)
    ) comment '管理台用户表' ENGINE=InnoDB DEFAULT CHARSET=utf8;



--角色表
CREATE TABLE SYS_ROLE
    (
        ID int NOT NULL AUTO_INCREMENT comment '主键',
        ROLE_CODE VARCHAR(32) comment '角色代码，如 admin',
		    ROLE_NAME VARCHAR(64) comment '角色名称',
		    ADMIN  CHAR(1) DEFAULT 0 NOT NULL comment '是否是管理员，1表示是，0表示否',
        REMARK VARCHAR(100) comment '备注',
        CREATE_TIME datetime default now() not null comment '创建时间',
		    UPDATE_TIME datetime default now() not null comment '更新时间',
        PRIMARY KEY (ID)
    ) comment '管理台角色表' ENGINE=InnoDB DEFAULT CHARSET=utf8;



--权限表
CREATE TABLE SYS_PERMISSION
    (
        ID int NOT NULL AUTO_INCREMENT comment '主键',
        PERM_CODE VARCHAR(32) comment '权限编码',
        PERM_NAME VARCHAR(64) comment '权限名称',
        PID int default 0 NOT NULL comment '父权限ID',
        URL VARCHAR(200) comment '权限对应的页面地址',
        REMARK VARCHAR(200) comment '备注',
        CREATE_TIME datetime default now() not null comment '创建时间',
		    UPDATE_TIME datetime default now() not null comment '更新时间',
        PRIMARY KEY (ID)
    ) comment '管理台角色表' ENGINE=InnoDB DEFAULT CHARSET=utf8;



--角色权限表
CREATE TABLE SYS_ROLE_PERMIT
    (
		    ROLE_ID int NOT NULL comment '角色id',
        PERM_ID int NOT NULL comment '权限ID',
        CREATE_TIME datetime default now() not null comment '创建时间',
        PRIMARY KEY (ROLE_ID,PERM_ID)
    ) comment '管理台角色表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

--初始化数据
insert into SYS_USER(role_id,user_name,USER_PWD,status,phone) values
 (1,'admin','9abf4b5eec43407eae7ecaefb7d13895','1','');

INSERT INTO SYS_ROLE (role_code,role_name,ADMIN) VALUES ('admin', '系统管理员', '1');
INSERT INTO SYS_ROLE (role_code,role_name) VALUES ( 'operator', '运营人员');

INSERT INTO SYS_PERMISSION (perm_code,pid,perm_name,url) VALUES ( 'system:view', 0, '系统管理', '');
INSERT INTO SYS_PERMISSION (perm_code,pid,perm_name,url) VALUES ( 'user:view', 1, '用户查看', '/user/userManage');
INSERT INTO SYS_PERMISSION (perm_code,pid,perm_name,url) VALUES ( 'user:edit', 1, '用户管理', '/user/userManage');
INSERT INTO SYS_PERMISSION (perm_code,pid,perm_name,url) VALUES ( 'role:view', 1, '角色查看', '/role/roleManage');
INSERT INTO SYS_PERMISSION (perm_code,pid,perm_name,url) VALUES ( 'role:edit', 1, '角色管理', '/role/roleManage');
INSERT INTO SYS_PERMISSION (perm_code,pid,perm_name,url) VALUES ( 'permit:view', 1, '权限查看', '/permcommentsion/permitManage');
INSERT INTO SYS_PERMISSION (perm_code,pid,perm_name,url) VALUES ( 'permit:edit', 1, '权限管理', '/permcommentsion/permitManage');
INSERT INTO SYS_PERMISSION (perm_code,pid,perm_name,url) VALUES ( 'basic:view', 0, '基本设置', '');
INSERT INTO SYS_PERMISSION (perm_code,pid,perm_name,url) VALUES ( 'pay:view', 8, '支付查看', '/pay/payManage');
INSERT INTO SYS_PERMISSION (perm_code,pid,perm_name,url) VALUES ( 'pay:edit', 8, '支付管理', '/pay/payManage');

-- 系统管理员读写权限
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 2);
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 3);
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 4);
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 5);
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 6);
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 7);
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 9);
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 10);


