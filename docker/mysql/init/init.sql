-- auto_trip 데이터베이스 생성 및 선택
CREATE DATABASE IF NOT EXISTS auto_trip;
USE auto_trip;

-- amad 사용자 생성 및 권한 부여
CREATE USER IF NOT EXISTS 'amad'@'%' IDENTIFIED WITH mysql_native_password BY 'amad';
GRANT ALL PRIVILEGES ON auto_trip.* TO 'amad'@'%';

-- root 사용자 권한 설정
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Ljcje0325!';
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'Ljcje0325!';
GRANT ALL PRIVILEGES ON auto_trip.* TO 'root'@'%';

-- 권한 적용
FLUSH PRIVILEGES;

-- 1. users 테이블 (의존성 없음, username에 UNIQUE 제약 조건 추가)
CREATE TABLE users
(
    user_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    username          VARCHAR(50)          NOT NULL,
    password          VARCHAR(100)         NOT NULL,
    nickname          VARCHAR(50)          NOT NULL COMMENT 'real user name',
    activated         TINYINT(1) DEFAULT 0 NOT NULL,
    profile_image_url VARCHAR(200)         NULL,
    CONSTRAINT users_username_unique UNIQUE (username)
)
    COMMENT 'user 테이블';

-- 2. authority 테이블 (의존성 없음)
CREATE TABLE authority
(
    authority_name VARCHAR(50) NOT NULL PRIMARY KEY
)
    COMMENT '권한 테이블';

-- 3. user_authority 테이블 (users, authority 참조)
CREATE TABLE user_authority
(
    user_id        BIGINT      NOT NULL,
    authority_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, authority_name),
    CONSTRAINT user_authority_ibfk_1
        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT user_authority_ibfk_2
        FOREIGN KEY (authority_name) REFERENCES authority (authority_name) ON DELETE CASCADE
)
    COMMENT '사용자-권한 매핑 테이블';

CREATE INDEX authority_name ON user_authority (authority_name);

-- 4. trip_plan 테이블 (의존성 없음, username에 UNIQUE 제약 조건)
CREATE TABLE trip_plan
(
    trip_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(100) NOT NULL,
    place     VARCHAR(200) NOT NULL,
    start_ymd VARCHAR(100) NOT NULL,
    end_ymd   VARCHAR(100) NOT NULL,
    settings  VARCHAR(200) NOT NULL COMMENT '유저 세팅 값',
    final_yn  TINYINT(1)   NULL,
    CONSTRAINT trip_plan_pk UNIQUE (username)
)
    COMMENT '여행 계획';

-- 5. trip_schedule 테이블 (trip_plan 참조)
CREATE TABLE trip_schedule
(
    schedule_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    trip_id            BIGINT       NOT NULL,
    start_ymd          VARCHAR(100) NULL,
    end_ymd            VARCHAR(100) NULL,
    activity_order     INT          NOT NULL,
    activity_type      VARCHAR(100) NOT NULL,
    activity_name      VARCHAR(100) NOT NULL,
    activity_address   VARCHAR(200) NULL,
    activity_image_url VARCHAR(500) NULL,
    CONSTRAINT trip_schedule_trip_plan_trip_id_fk
        FOREIGN KEY (trip_id) REFERENCES trip_plan (trip_id)
)
    COMMENT '여행 일정';

-- 6. travel_place 테이블 (users 참조)
CREATE TABLE travel_place
(
    username VARCHAR(200) NOT NULL PRIMARY KEY,
    place    VARCHAR(300) NULL COMMENT '장소',
    CONSTRAINT travel_place_users_username_fk
        FOREIGN KEY (username) REFERENCES users (username)
)
    COMMENT '여행 장소';

-- 7. setting 테이블 (users 참조)
CREATE TABLE setting
(
    username  VARCHAR(200) NOT NULL PRIMARY KEY,
    start_ymd VARCHAR(100) NULL COMMENT '여행 시작 일',
    end_ymd   VARCHAR(100) NULL COMMENT '여행 종료 일',
    activity  TINYINT(1)   NULL COMMENT '뭐하지 - 활동적인',
    museum    TINYINT(1)   NULL COMMENT '뭐하지 - 박물관',
    cafe      TINYINT(1)   NULL COMMENT '뭐하지 - 분위기 좋은 카페',
    tour_att  TINYINT(1)   NULL COMMENT '뭐하지 - 관광명소',
    CONSTRAINT setting_users_username_fk
        FOREIGN KEY (username) REFERENCES users (username)
)
    COMMENT '여행 세부 설정';

-- 8. accommodation 테이블 (trip_plan 참조)
CREATE TABLE accommodation
(
    username  VARCHAR(200) NOT NULL,
    date      VARCHAR(100) NOT NULL,
    name      VARCHAR(200) NULL,
    address   VARCHAR(200) NULL,
    image_url VARCHAR(300) NULL,
    PRIMARY KEY (username, date),
    CONSTRAINT accommodation_trip_plan_username_fk
        FOREIGN KEY (username) REFERENCES trip_plan (username)
)
    COMMENT '숙소';
