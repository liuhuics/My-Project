--用户表
CREATE TABLE SYS_USER
    (
        ID NUMBER(9,0) NOT NULL,
		    ROLE_ID NUMBER(9,0),
        USER_NAME VARCHAR2(16),
        USER_PWD VARCHAR2(64),
        PHONE VARCHAR2(11),
        DEPART_ID NUMBER(9,0) default 1 not null,
        STATUS CHAR(1) default 1 not null,
        CREATE_TIME TIMESTAMP(6) default sysdate not null ,
		    UPDATE_TIME TIMESTAMP(6) default sysdate not null ,
        PRIMARY KEY (ID)
    );

COMMENT ON TABLE SYS_USER IS '管理台用户表';
COMMENT ON COLUMN SYS_USER.ID IS '主键';
COMMENT ON COLUMN SYS_USER.ROLE_ID IS '角色 id';
COMMENT ON COLUMN SYS_USER.USER_NAME IS '用户名';
COMMENT ON COLUMN SYS_USER.USER_PWD IS '用户密码';
COMMENT ON COLUMN SYS_USER.PHONE IS '手机号';
COMMENT ON COLUMN SYS_USER.DEPART_ID IS '机构id';
COMMENT ON COLUMN SYS_USER.STATUS IS '用户状态，1-有效，0-无效';
COMMENT ON COLUMN SYS_USER.CREATE_TIME IS '创建时间';
COMMENT ON COLUMN SYS_USER.UPDATE_TIME IS '更新时间';

create sequence SYS_USER_SEQ
    minvalue 1
    maxvalue 99999999
    start with 1
    increment by 1
    cache 20
    order;


--角色表
CREATE TABLE SYS_ROLE
    (
        ID NUMBER(9,0) NOT NULL,
        ROLE_CODE VARCHAR2(32),
		    ROLE_NAME VARCHAR2(64),
		    ADMIN  CHAR(1) DEFAULT 0 NOT NULL,
        REMARK VARCHAR2(100),
        CREATE_TIME TIMESTAMP(6) default sysdate not null,
		    UPDATE_TIME TIMESTAMP(6) default sysdate not null,
        PRIMARY KEY (ID)
    );
COMMENT ON TABLE SYS_ROLE IS '管理台角色表';
COMMENT ON COLUMN SYS_ROLE.ID IS '主键';
COMMENT ON COLUMN SYS_ROLE.ROLE_CODE IS '角色代码，如 admin';
COMMENT ON COLUMN SYS_ROLE.ROLE_NAME IS '角色名称';
COMMENT ON COLUMN SYS_ROLE.ADMIN IS '是否是管理员，1表示是，0表示否';
COMMENT ON COLUMN SYS_ROLE.REMARK IS '备注';
COMMENT ON COLUMN SYS_ROLE.CREATE_TIME IS '创建时间';
COMMENT ON COLUMN SYS_ROLE.UPDATE_TIME IS '更新时间';

create sequence SYS_ROLE_SEQ
    minvalue 1
    maxvalue 99999999
    start with 1
    increment by 1
    cache 20
    order;

--权限表
CREATE TABLE SYS_PERMISSION
    (
        ID NUMBER(9,0) NOT NULL,
        PERM_CODE VARCHAR2(32),
        PERM_NAME VARCHAR2(64),
        PID NUMBER(9,0) default 0 NOT NULL,
        URL VARCHAR2(200),
        REMARK VARCHAR2(200),
        CREATE_TIME TIMESTAMP(6) default sysdate not null,
		    UPDATE_TIME TIMESTAMP(6) default sysdate not null,
        PRIMARY KEY (ID)
    );
COMMENT ON TABLE SYS_PERMISSION IS '管理台角色表';
COMMENT ON COLUMN SYS_PERMISSION.ID IS '主键';
COMMENT ON COLUMN SYS_PERMISSION.PERM_CODE IS '权限编码';
COMMENT ON COLUMN SYS_PERMISSION.PERM_NAME IS '权限名称';
COMMENT ON COLUMN SYS_PERMISSION.PID IS '父权限ID';
COMMENT ON COLUMN SYS_PERMISSION.URL IS '权限对应的页面地址';
COMMENT ON COLUMN SYS_PERMISSION.REMARK IS '备注';
COMMENT ON COLUMN SYS_PERMISSION.CREATE_TIME IS '创建时间';
COMMENT ON COLUMN SYS_PERMISSION.UPDATE_TIME IS '更新时间';

create sequence SYS_PERMISSION_SEQ
    minvalue 1
    maxvalue 99999999
    start with 1
    increment by 1
    cache 20
    order;

--角色权限表
CREATE TABLE SYS_ROLE_PERMIT
    (
		    ROLE_ID NUMBER(9,0) NOT NULL,
        PERM_ID NUMBER(9,0) NOT NULL,
        CREATE_TIME TIMESTAMP(6) default sysdate not null,
        PRIMARY KEY (ROLE_ID,PERM_ID)
    );
COMMENT ON TABLE SYS_ROLE_PERMIT IS '管理台角色表';
COMMENT ON COLUMN SYS_ROLE_PERMIT.ROLE_ID IS '角色id';
COMMENT ON COLUMN SYS_ROLE_PERMIT.PERM_ID IS '权限ID';
COMMENT ON COLUMN SYS_ROLE_PERMIT.CREATE_TIME IS '创建时间';
COMMENT ON COLUMN SYS_ROLE_PERMIT.UPDATE_TIME IS '更新时间';

--初始化数据
insert into SYS_USER(id,role_id,user_name,USER_PWD,status,phone) values
 (SYS_USER_SEQ.nextval,1,'admin','9abf4b5eec43407eae7ecaefb7d13895','1','');

INSERT INTO SYS_ROLE (id,role_code,role_name,ADMIN) VALUES (SYS_ROLE_SEQ.nextval, 'admin', '系统管理员', '1');
INSERT INTO SYS_ROLE (id,role_code,role_name) VALUES (SYS_ROLE_SEQ.nextval, 'operator', '运营人员');

INSERT INTO sys_permission (id,perm_code,pid,perm_name,url) VALUES (sys_permission_seq.nextval, 'system:view', 0, '系统管理', '');
INSERT INTO sys_permission (id,perm_code,pid,perm_name,url) VALUES (sys_permission_seq.nextval, 'user:view', 1, '用户查看', '/user/userManage');
INSERT INTO sys_permission (id,perm_code,pid,perm_name,url) VALUES (sys_permission_seq.nextval, 'user:edit', 1, '用户管理', '/user/userManage');
INSERT INTO sys_permission (id,perm_code,pid,perm_name,url) VALUES (sys_permission_seq.nextval, 'role:view', 1, '角色查看', '/role/roleManage');
INSERT INTO sys_permission (id,perm_code,pid,perm_name,url) VALUES (sys_permission_seq.nextval, 'role:edit', 1, '角色管理', '/role/roleManage');
INSERT INTO sys_permission (id,perm_code,pid,perm_name,url) VALUES (sys_permission_seq.nextval, 'permit:view', 1, '权限查看', '/permission/permitManage');
INSERT INTO sys_permission (id,perm_code,pid,perm_name,url) VALUES (sys_permission_seq.nextval, 'permit:edit', 1, '权限管理', '/permission/permitManage');
INSERT INTO sys_permission (id,perm_code,pid,perm_name,url) VALUES (sys_permission_seq.nextval, 'basic:view', 0, '基本设置', '');
INSERT INTO sys_permission (id,perm_code,pid,perm_name,url) VALUES (sys_permission_seq.nextval, 'pay:view', 8, '支付查看', '/pay/payManage');
INSERT INTO sys_permission (id,perm_code,pid,perm_name,url) VALUES (sys_permission_seq.nextval, 'pay:edit', 8, '支付管理', '/pay/payManage');

-- 系统管理员读写权限
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 2);
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 3);
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 4);
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 5);
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 6);
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 7);
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 9);
INSERT INTO sys_role_permit (role_id,perm_id) VALUES (1, 10);


