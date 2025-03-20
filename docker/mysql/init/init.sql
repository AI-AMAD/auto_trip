-- users table DDL
create table users
(
    user_id           bigint auto_increment
        primary key,
    username          varchar(50)          not null,
    password          varchar(100)         not null,
    nickname          varchar(50)          not null comment 'real user name',
    activated         tinyint(1) default 0 not null,
    profile_image_url varchar(200)         null
)
    comment 'user 테이블';

-- authority table DDL
create table authority
(
    authority_name varchar(50) not null
        primary key
);

-- user_authority table DDL
create table user_authority
(
    user_id        bigint      not null,
    authority_name varchar(50) not null,
    primary key (user_id, authority_name),
    constraint user_authority_ibfk_1
        foreign key (user_id) references users (user_id)
            on delete cascade,
    constraint user_authority_ibfk_2
        foreign key (authority_name) references authority (authority_name)
            on delete cascade
);

create index authority_name
    on user_authority (authority_name);