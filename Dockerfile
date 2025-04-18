FROM openjdk:17-jdk-alpine3.13

# 작업 디렉토리 설정
WORKDIR /app

# 애플리케이션 JAR 파일 복사
COPY build/libs/oatnote-0.0.1-SNAPSHOT.jar app.jar

# 컨테이너 실행 시 실행할 명령어 설정
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
