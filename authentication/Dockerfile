FROM openjdk:21
COPY ./target/authentication-0.0.1-SNAPSHOT.jar /usr/src/authentication/
WORKDIR /usr/src/authentication
CMD ["java", "-Duser.timezone=GMT+3", "-jar", "authentication-0.0.1-SNAPSHOT.jar"]