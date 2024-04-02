FROM openjdk:21
COPY ./target/entrylogging-0.0.1-SNAPSHOT.jar /usr/src/entrylogging/
WORKDIR /usr/src/entrylogging
EXPOSE 8080
CMD ["java", "-Duser.timezone=GMT+3", "-jar", "entrylogging-0.0.1-SNAPSHOT.jar"]