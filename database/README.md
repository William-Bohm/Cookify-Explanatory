# Databases

This directory contains schema definitions and scripts for initializing the databases used in Cookify. All databases are accessed via a Java Spring API interface.

## Overview

Cookify leverages the power of different types of databases to serve different aspects of the application effectively and efficiently:

### Elasticsearch

Elasticsearch is utilized for full-text search functionality. Being a distributed, RESTful search and analytics engine, Elasticsearch excels at searching textual data. It provides powerful, real-time search capabilities with a very high scale ceiling. Its ability to analyze large amounts of data in near real-time makes it an excellent choice for our full-text search service.

### MongoDB

MongoDB is the main database used for data storage in Cookify. As a NoSQL database, MongoDB provides high performance, high availability, and easy scalability. Its document-oriented model is flexible and maps naturally to objects in application code, making it simpler to work with. This flexibility also makes MongoDB suitable for storing the varied types of data that our different microservices handle.

### MySQL

MySQL is used for our custom JWT service. It is a reliable, efficient SQL database that offers high performance, ease-of-use, and robust transactional support. We chose MySQL for the JWT service because we needed a database that excels in handling structured data with clear relations - like user credentials and roles, which are often queried together.

### Neo4j

Neo4j is used for our free recommendation engine. As a graph database, Neo4j is built for speed and offers constant, real-time performance, which is perfect for making recommendations. The graph data model is very expressive and easy to work with, enabling us to model and analyze the complex, interconnected relationships that power our recommendation engine.

## Navigating this Directory

Each database has its own sub-directory where you can find the respective initialization scripts and schemas. Note that due to security considerations, some files may have been excluded from this repository.
