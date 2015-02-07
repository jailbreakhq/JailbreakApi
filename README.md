# JailbreakApi [![Build Status](https://travis-ci.org/jailbreakhq/JailbreakApi.svg?branch=master)](https://travis-ci.org/jailbreakhq/JailbreakApi)
Dropwizard REST API service that handles Teams, Checkins, Users, API Tokens, and authenticating against Facebook.

## Structure
This is a maven project with three submodules: `jailbreak-api`, `jailbreak-client` and `jailbreak-service`.

### `jailbreak-api`
This module contains the Java representations of domain objects and error messages that the API exposes. This means if you are building a Java client against this API you can just maven install this sub modules to have all the required Java objects ready to go.

This module uses [google-protobuf](https://developers.google.com/protocol-buffers/docs/javatutorial) to for objects. Inside the root of this-submodule is a bash script `compile-proto.sh` that recompiles the source `src/main/resources/representations.proto` to `src/main/java/org/.../api/representations/Represenations.java`.

### `jailbreak-client`
This is where we store clients that call other services. It currently contains the simple java client that calls the Facebook `/me` API endpoint.

### `jailbreak-service`
This is the main core of this project. This sub-module depends on the other two submodules of this project: `jailbreak-api` and `jailbreak-client`. This is where the dropwizard service lives. 

It has a simple pattern of objects: resources, that respond to requests from the user; managers, that handle building and managing domain objects (may call the database code, or an external service or merge information together etc); DAO classes that handle making queries to the database; Mappers, which handle mapping SQL results back into objects.

#### Managers
Each manager has both an interface and an implementation, this is because I'm using [guice](https://github.com/google/guice) for dependency injection almost exclusively in this project. The main guice file is `ServiceModule.java`, where you will find what provides what and how objects are bound.

#### Liquidbase 
This project is using [Liquidbase](http://www.liquibase.org/) to manage the database schema (similar to something like django's South project). The core of this is the `src/main/resources/migrations.xml` that holds the steps Liquidbase uses to build the database tables.

## API Docs
The API docs are on [apiary](http://docs.jailbreakapi.apiary.io/#). There is also a mock API server setup over there that returns the response examples in the documenation when called. It can be a handy first step when trying to build against this API without setting up all the dependencies.