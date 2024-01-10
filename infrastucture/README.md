# Infrastructure Directory

This directory contains the infrastructure setup for our Cookify app, a robust meal planning application deployed on AWS EKS cluster. We leverage a range of cutting-edge technologies to ensure our microservices run efficiently, securely, and are easy to monitor.

## Subdirectories

### Hashicorp Consul

Consul is a service networking solution to connect and secure services across any runtime platform and public or private cloud. In the context of an EKS cluster, Consul helps in service discovery, configuration, and segmentation.

Using Consul, we can discover and configure services in our infrastructure with a centralized registry, which helps us in maintaining a robust system. Its service mesh provides secure and reliable connectivity between our microservices, all while keeping the communication encrypted with automatic mTLS. This is crucial in a microservice architecture like ours to help in managing services, their communication, and in enforcing security policies.

### ELK Stack

The ELK Stack (Elasticsearch, Logstash, Kibana) is used for log aggregation. As our system is distributed across multiple services, managing and understanding logs can be challenging. The ELK Stack helps to centralize, store, and visualize logs in real-time, which is crucial for monitoring application health and troubleshooting issues quickly.

Elasticsearch is a highly scalable open-source full-text search and analytics engine. Logstash is a server-side data processing pipeline that ingests data from multiple sources simultaneously, transforms it, and then sends it to Elasticsearch. Kibana then visualizes the data in Elasticsearch, allowing us to easily navigate through our data.

### JWT Service (Java Spring)

JWT (JSON Web Tokens) Service is our custom authentication service based on Java Spring Security. It creates, signs, and verifies tokens for users upon successful authentication. The tokens include claims about the user and are sent back to the client. For every subsequent request from the client, this token is included, allowing the server to verify the user's identity and provide access to resources based on user's permissions.

JWT provides a secure way of handling user authentication and authorization. It's a stateless, compact, and self-contained method for securely transmitting information between parties as a JSON object.

### Hashicorp Vault

Vault is a tool for securely accessing secrets. A secret is anything that we want to tightly control access to, such as API keys, passwords, certificates, and more. Vault provides a unified interface to any secret while providing tight access control and recording a detailed audit log. It adds an additional layer of protection by encrypting data before it is stored.

In our infrastructure, Vault is used for secrets management. It tightly controls access to secrets and protects sensitive data like our application properties, connection information, and much more.

## Security Overview

Security is the heart of our infrastructure setup. Our choices in technologies reflect a strong commitment to maintaining the integrity and privacy of user data and service communications.

At a high level, JWT Service provides authentication and authorization; Consul secures and manages service-to-service communication; Vault manages and safeguards secrets; and ELK Stack monitors our system's health and activities.

In detail, once a user is authenticated by JWT Service, it generates a token that is used to verify the user's identity on each subsequent request. This ensures only authenticated users access the system.

Consul service mesh enforces authorization and secure communication within the cluster. It handles service-to-service connections, encrypting the traffic with automatic mTLS. This makes sure the communication between our microservices is secure.

Vault safeguards our sensitive data and secrets. It not only stores secrets securely but also handles leasing, key revocation, key rolling, and auditing.

ELK Stack enhances security by giving us insights into application behavior and spotting any suspicious activity through centralized logging.

By employing these technologies together, we are creating an environment that is secure, easy to manage, monitor, and capable of scaling to meet demands.
