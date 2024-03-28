FROM openjdk:21
COPY ./target/authentication-0.0.1-SNAPSHOT.jar /usr/src/authentication/
WORKDIR /usr/src/authentication
EXPOSE 8082
CMD ["java", "-jar", "authentication-0.0.1-SNAPSHOT.jar"]