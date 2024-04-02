FROM openjdk:21
COPY ./target/alerts-0.0.1-SNAPSHOT.jar /usr/src/alerts/
WORKDIR /usr/src/alerts
EXPOSE 8081
CMD ["java", "-Duser.timezone=GMT+3", "-jar", "alerts-0.0.1-SNAPSHOT.jar"]