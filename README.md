# Emotions tracker application



## Description

The current project represents a full-stack application in _Java_ and _React_
for tracking emotions based on microservice architecture. The aim of the project 
was to study patterns of microservice architecture and get a hands-on 
practice with developing resilient and scalable microservices applications. 
In particular, it focuses on such concepts as _**event-driven architecture, 
service discovery, routing, load balancing, externalized configuration,
logs centralization, and traceability**_. 

- [Features](#features)
- [Stack](#stack)
- [Installation](#installation)
- [Achievements](#achievements)

## Features

The emotions tracker app allows you to track your state and emotions securely.

Before being able to access the app, you are prompted to log in or register 
if you're a new user. On a successful attempt to log in you're authenticated
and issued a permit to access the contents. Each user can only access their
own account data.

In the app, you can log how you're feeling at the moment or how you felt 
at a particular time in the past. After logging some entries you can see
the statistics of your states and emotions throughout the last week. In 
particular, the percentage of logged states and 5 top logged emotions.

At some point, the app can detect that your logs might require additional
attention and alert you about that after detection some event or pattern. 
To read more info about the currently available alerts, please refer to the 
[alerts README file](./alerts/README.md). 

## Stack

### Backend
The backend stack base is Java 21 and SpringBoot 3.2.3. Maven of version 3.9.6
is used for build automation.
Additional technologies used are:
- RabbitMQ as a message broker;
- Consul for implementing service discovery, load balancing
and externalized configuration;

### Frontend
The frontend is created with React framework and includes libraries:
- MUI X Date Pickers (community edition of the date and time picker components);
- Classnames (JavaScript utility for conditionally joining classNames);
- Day.js (JavaScript library for manipulation dates and times);
- Bootstrap 5 (used for CSS styling)

Docker is used for containerization of the whole app.

## Installation

Make sure you have node and npm installed on your local machine by
running `node --version` and `npm --version` and install if necessary.

#### Commands for installing npm libraries from the emotions-tracker-frontend directory:
- `npm install @mui/x-date-pickers`
- `npm install classnames`
- `npm install dayjs`
- `npm install bootstrap@v5.3.3`

### Authentication

The main branch includes a JWT authentication feature with an asymmetric signing 
algorithm. In order to use the app, a pair of public and private keys should be 
generated for the authentication service and the public key should be
copied into the following services: alerts, entries, and stats. 
The path for the key/keys should be `src/main/resources/certs`.

To generate the keys, you can use the open ssl tool:
- From the indicated directory
in the authentication service run `openssl genrsa -out keypair.pem 2048` to
generate the initial key;
- Run `openssl rsa -in keypair.pem -pubout -out public.pem` to generate the
public key;
- Run `openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem`
to generate the private key;
- Delete the initial `keypair.pem` file;
- Copy the `public.pem` key to the corresponding directories in alerts, entries, 
and stats services.

You can also check out the **symmetric-key** branch if you'd like to avoid the
mentioned steps.


### With Docker

First, build the backend services by running `./mvnw clean package -DskipTests` 
command from the root directory and the frontend service by running the
`npm run build` command from the emotions-tracker-frontend directory.

Then navigate to the docker directory and run `docker compose up`. 

The following steps will be triggered:
- Consul image will be downloaded and the container with Consul will be
started on port 8500. Additional consul importer process will load 
externalized configuration to Consul KV;
- The RabbitMQ server will be started in a container after loading the image;
- Docker images will be built based on Dockerfiles provided in each of 
the services and containers with services will be started. 

  
### Without Docker

To start the app without containers follow these steps:
- Install RabbitMQ and start the RabbitMQ server following the download 
instructions for your OS;
- Install Consul and run it in development mode with 
`consul agent -node=learnmicro -dev`
- Start the frontend app from the emotions-tracker-frontend directory with `npm start`
- Start each of the backend services from your IDE by starting the application 
or simply run `./mvnw spring-boot:run` from each of the
services directories.

## Achievements

There were quite a few new take-outs for me. I’ve learned what event-driven 
architecture is and how it’s different from a monolith. In my application, 
the event-driven system built on communication between services via message 
broker has contributed to loose coupling between microservices, the system’s 
scalability, and resilience. 

Together with the benefits of microservices architecture comes higher complexity. 
To manage the system in new circumstances, I’ve also implemented such patterns as: 
- Gateway, allowing the client, or frontend service in my case, to communicate with
the backend system as a whole;
The gateway microservice, responsible for this functionality, opens a port on
localhost:8000 and redirects the calls from the frontend to the relevant service.
Apart from routing configuration, it contains CORS settings, restricting allowed
requests to frontend origin and setting allowed HTTP methods. 
- Service discovery, enabling the services to register and be tracked as part of 
the whole system;
- Load balancing, for balancing the load between the services, so that the system
can become truly scalable;
I've decided to use Consul because of its robustness and high compatibility with
Spring Cloud. As a part of the system, Consul allows server-side service discovery
approach and integrates with Spring Boot by keeping track of services' health status
via the endpoints provided by Spring Boot Actuator.
As for load balancing, I've taken advantage of the Spring Cloud Consul Discovery
library, which allowed me to implement this feature by including the load balancer
configurations in the Gateway microservice properties. 
- Externalised configuration, for keeping the configuration properties in one place
and avoiding duplication;
In the current implementation, Consul takes responsibility for centralised configuration
by importing a JSON file with encoded configuration. This way I was able to add the 
Docker configuration and set the hostname for RabbitMQ and unique instance identifiers
for running services.
- Logs centralisation and distributed tracing, to make tracking of what’s happening
across the system easier and lower complexity;
The app has an additional logs microservice to keep track of logs in one place and
enable distributed tracing. The logs from each of the services are appended by the
log4j logger and sent to the logs RabbitMQ queue, which is then parsed by the logs
service together with span ID and trace ID passed together with MDC
(Mapped Diagnostic Context).
- Containerisation, for running the system in multiple environments and avoiding
going through multiple installation steps.

Although the frontend part wasn’t the focus of the task, I also gained some 
experience in developing a frontend application with React, preserving loose 
coupling between components and services allowing reusability and high maintainability.

