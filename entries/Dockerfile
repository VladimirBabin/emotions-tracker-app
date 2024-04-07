FROM openjdk:21
COPY ./target/entries-0.0.1-SNAPSHOT.jar /usr/src/entries/
WORKDIR /usr/src/entries
CMD ["java", "-Duser.timezone=GMT+3", "-jar", "entries-0.0.1-SNAPSHOT.jar"]