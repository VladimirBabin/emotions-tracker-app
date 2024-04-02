FROM openjdk:21
COPY ./target/logs-0.0.1-SNAPSHOT.jar /usr/src/logs/
WORKDIR /usr/src/logs
EXPOSE 8083
CMD ["java", "-Duser.timezone=GMT+3", "-jar", "logs-0.0.1-SNAPSHOT.jar"]