FROM openjdk:21
COPY ./target/gateway-0.0.1-SNAPSHOT.jar /usr/src/gateway/
WORKDIR /usr/src/gateway
EXPOSE 8000
CMD ["java", "-Duser.timezone=GMT+3", "-jar", "gateway-0.0.1-SNAPSHOT.jar"]