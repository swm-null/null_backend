# 베이스 이미지 설정
FROM openjdk:17-jdk-alpine3.13

# 작업 디렉토리 설정
WORKDIR /app

# YAML 파일을 도커 이미지 내부로 복사
COPY application.yml /app/application.yml

# 환경 변수 설정
ENV YAML_PATH /app/application.yml

# 애플리케이션 JAR 파일 복사
ARG JAR_FILE=./build/libs/memo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 컨테이너 실행 시 실행할 명령어 설정
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.config.location=${YAML_PATH}"]
