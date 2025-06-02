-- DB가 없으면 생성
CREATE DATABASE IF NOT EXISTS routinenyang;

-- 유저가 없으면 생성
CREATE USER IF NOT EXISTS 'appuser'@'%' IDENTIFIED BY 'secure_pass_2025';

-- 권한 부여
GRANT ALL PRIVILEGES ON routinenyang.* TO 'appuser'@'%';

-- 권한 갱신
FLUSH PRIVILEGES;