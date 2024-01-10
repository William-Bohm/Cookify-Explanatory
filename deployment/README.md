# Deployment

This directory contains infrastructure as code files for deploying Cookify on AWS EKS. The deployment process leverages Terraform and Helm charts to orchestrate the necessary resources and services.

## Overview

Cookify's deployment process is designed to ensure the robustness, security, and scalability of the application. We use AWS EKS as the Kubernetes platform for deploying our services and leverage AWS EBS for persistent storage needs. 

**Please Note:** Some files have been excluded from this repository for security and privacy reasons.

## Deployment Structure

The deployment directory is structured as follows:

- `app/` - Contains Helm charts for deploying the microservices and databases.
- `consul/` - Contains deployment configurations for HashiCorp Consul.
- `ELK/` - Contains deployment configurations for the ELK stack.
- Terraform files at the root level - These are for provisioning and managing the AWS EKS cluster and other necessary AWS resources.

## Interconnections

Each component in the deployment process plays a specific role and interacts with others to provide a cohesive, functional system:

### Microservices

Our Java Spring microservices are containerized and managed as individual services within the AWS EKS cluster. Each microservice has its respective Helm chart under the `app/` directory, defining its deployment, service, and any other Kubernetes resources it needs.

### Databases

The databases (Elasticsearch, MongoDB, MySQL, Neo4j) have their own Helm charts under the `app/` directory as well. We opted to manage our own databases on EBS volumes to reduce costs and have more control over the configurations.

### HashiCorp Consul

Consul is deployed using the configuration files in the `consul/` directory. It provides service discovery and a service mesh for secure, mTLS-based inter-service communication. Consul's service mesh is crucial in enabling secure communication between our microservices.

### HashiCorp Vault

Vault, which provides secrets management, is also deployed via a Helm chart under the `app/` directory. Vault is crucial in managing and securely providing the necessary credentials and secrets to our services.

### ELK Stack

The ELK stack (Elasticsearch, Logstash, Kibana) is used for log aggregation, providing a central place to view and analyze the logs from our services. The ELK stack is deployed using the configuration files in the `ELK/` directory.

### RabbitMQ

RabbitMQ provides messaging capabilities to our microservices, enabling efficient, decoupled communication between them. It's also deployed via a Helm chart under the `app/` directory.

### Terraform

Terraform is used for provisioning and managing our AWS resources, including the EKS cluster, EBS volumes, and any other necessary resources. The Terraform files at the root level define these resources and their configurations.

## Deployment Process

Our deployment process is highly automated to minimize manual intervention and ensure consistency across different stages of the application lifecycle.

1. **Terraform:** The process starts with Terraform scripts, which are responsible for creating and managing the AWS resources including EKS cluster, EBS volumes and more.

2. **Helm:** Once the AWS resources are ready, Helm is used to deploy the microservices, databases and other necessary services like Consul, Vault, and RabbitMQ to the EKS cluster. Each of these deployments is defined in the form of a Helm chart in the `app/` directory. Helm ensures each service is deployed with its required configuration and manages its lifecycle in the cluster.

3. **Consul:** Once deployed, Consul provides service discovery and secure service-to-service communication via its service mesh. All microservices are registered with Consul and use its service mesh for mTLS communication.

4. **Vault:** Vault, once deployed, securely stores and manages secrets that are required by the different services. It's integrated with the application such that secrets are fetched as needed and are never exposed unnecessarily.

5. **ELK Stack:** Deployed using configurations in the `ELK/` directory, it collects and aggregates logs from all services, providing a unified view of the application's operational status.

6. **RabbitMQ:** It provides a messaging queue for microservices, ensuring smooth, asynchronous communication between different services.

The interaction and coordination between these components make for a scalable, robust and secure deployment of Cookify.

## Further Information

Each subdirectory has its own README.md file with more detailed information about that particular component's deployment. Please check these for more specific details about each component's configuration and deployment.

**Note:** Please follow security best practices while handling the Terraform state files and any secret values required by Terraform or stored in Vault. Ensure these are adequately protected and not exposed to any public or unsecured locations.

