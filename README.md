# Emotions tracker application



## Description

The current project represents a full stack application in _Java_ and _React_
for tracking emotions based on microservice architecture. The aim of the project 
was to learn patterns of microservice architecture and get a hands-on 
practice with developing resilient and scalable microservices applications. 
In particular, it focuses on such concepts as _**event-driven architecture, 
service discovery, routing, load balancing and traceability**_. 

- [Features](#features)
- [Installation](#installation)
- [Stack description](#Stack description)

## Features

Emotions tracker app allows you to securely track your state and emotions.

Before being able to access the app, you are prompt to log in or register 
if you're a new user. On a successful attempt to log in you're authenticated
and issued a permit to access the contents. Each user can only access their
own account data.

In the app you can log how you're feeling at the moment or how you felt 
at a particular time in the past. After logging some entries you can see
the statistics of your states and emotions throughout the last week. In 
particular the percentage of logged states and 5 top logged emotions.

At some point, the app can detect that your logs might require additional
attention and alert you about that after detection some event or pattern. 
To read more info about the currently available alerts, please refer to the 
[alerts README file](./alerts/README.md). 

## Stack description

### Backend
The backend stack base is Java 21 and SpringBoot 3.2.3. Maven of version 3.9.6
is used for build automation.
Additional technologies used are:
- RabbitMQ as a message broker;
- Consul for implementing gateway, service discovery, load balancing
and externalized configuration patterns;

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

#### Commands for installing npm libraries from emotions-tracker-frontend directory:
- `npm install @mui/x-date-pickers`
- `npm install classnames`
- `npm install dayjs`
- `npm install bootstrap@v5.3.3`

### Authentication

The current branch has JWT authentication feature with symmetric signing 
algorithm with key predefined in the AuthProvider classes.

### With Docker

First build the backend services by running `./mvnw clean package -DskipTests` 
command from the root directory and the frontend service by running the
`npm run build` command from the emotions-tracker-frontend directory.

Then navigate to docker directory and run `docker compose up`. 

Following steps will be triggered:
- Consul image will be downloaded and container with Consul will be
started on port 8500. Additional consul importer process will load 
externalized configuration to Consul KV;
- RabbitMQ server will be started in a container after loading the image;
- Docker images will be built based on Dockerfiles provided in each of 
the services and containers with services will be started. 

  
### Without Docker

To start the app without containers follow these steps:
- Install RabbitMQ and start the RabbitMQ server following the download 
instructions for your OS;
- Install Consul and run it in development mode with 
`consul agent -node=learnmicro -dev`
- Start frontend app from emotions-tracker-frontend directory with `npm start`
- Start each of the backend services from your IDE by starting the application 
or simply run `./mvnw spring-boot:run` from each of the
services directories.

