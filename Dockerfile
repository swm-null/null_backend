FROM openjdk:17-jdk-alpine3.13
ARG JAR_FILE=./build/libs/memo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
