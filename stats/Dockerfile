FROM openjdk:21
COPY ./target/stats-0.0.1-SNAPSHOT.jar /usr/src/stats/
WORKDIR /usr/src/stats
CMD ["java", "-Duser.timezone=GMT+3", "-jar", "stats-0.0.1-SNAPSHOT.jar"]