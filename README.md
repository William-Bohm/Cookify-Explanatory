# Cookify

## Overview

Cookify is a production-grade meal planning application, designed with a user-friendly interface backed by a complex, robust backend. The project showcases a highly scalable and secure microservices architecture, deployed on AWS EKS.

The aim was to make the user experience as seamless as possible while challenging ourselves with the backend - creating a sophisticated orchestration of technologies that work together to deliver a powerful application.

**Please Note:** Some files have been excluded from this repository for security and privacy reasons.

## Tech Stack

### Microservices

- FullTextSearch (Java Spring)
- NutritionService (Java Spring)
- PhotoService (Java Spring)
- RecipeService (Java Spring)
- UserService (Java Spring)
- VideoStream Service (Java Spring)
- RecommendationService (Java Spring)

### Databases

- Elasticsearch (Autocomplete search)
- MongoDB (Main storage)
- MySQL (Custom JWT service)
- Neo4j (Recommendation engine)

### Infrastructure

- AWS EKS (Deployment)
- Java Spring (Custom JWT service)
- Hashicorp Consul (Service mesh and ingress)
- Hashicorp Vault (Secrets manager)
- ELK stack (Log aggregation)
- Terraform and Helm (Infrastructure as Code and deployment)

## Repo Structure

The repository is structured as follows:

- `src/` - Contains the source code for all microservices.
- `database/` - Contains schema for Elasticsearch, MongoDB, MySQL and Neo4j.
- `infrastructure/` - Contains infrastructure as code scripts (Terraform, Helm) and configurations for Consul, Vault, ELK stack and RabbitMQ.
- `deployemnt/` - Contains terraform code and helm charts for deployment of the Cluster and the accompaning services. Some files have been removed for security reasons

Each sub-directory has its own README.md that goes further in depth as to why that technology was chosen, what the service/technology provides, and possible ways to improve the service.

## Getting Started

I recommend reading some of the README's and then delving into the code on whatever peaks your interest the most.

# Cookify-Explanatory
