FROM eclipse-temurin:21-jdk

WORKDIR /app

# 필수 패키지 설치
RUN apt-get update && apt-get install -y \
    mysql-server \
    mysql-client \
    supervisor

# MySQL 실행에 필요한 디렉토리 생성
RUN mkdir -p /var/run/mysqld && chown -R mysql:mysql /var/run/mysqld

# JAR 복사
COPY ./build/libs/RoutineNyang-0.0.1-SNAPSHOT.jar app.jar

# 설정 파일 복사
COPY ./src/main/resources/application-docker-dev.yml /app/application-docker-dev.yml
COPY ./docker/init-db-user.sql /docker/init-db-user.sql
COPY ./supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# MySQL 환경변수 설정
ENV SPRING_PROFILES_ACTIVE=docker-dev

EXPOSE 8080

CMD ["/usr/bin/supervisord"]