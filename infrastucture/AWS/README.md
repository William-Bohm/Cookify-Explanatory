# AWS Infrastructure Directory

This directory houses the configuration and scripts used to set up and manage our AWS EKS Cluster. This document aims to explain the reasoning behind each choice made in the infrastructure setup.

## AWS EKS Cluster

We use Amazon EKS (Elastic Kubernetes Service) as our primary compute and orchestration engine. EKS enables us to leverage the power and flexibility of Kubernetes without the operational overhead of maintaining our own cluster.

## Roles and Policies

Two primary roles have been attached to the EKS cluster:

1. **Automatic Backup Policy**: This policy triggers backups for selected resources at set intervals. We currently have the backups configured to occur every day.
2. **EBS CSI Driver**: The EBS Container Storage Interface (CSI) driver enables the EKS cluster to provision persistent volume claims with EBS backend storage. Specifically, we are using gp2 with the ext4 filesystem type. This also enables us to manage the lifecycle of the EBS volumes, such as creation, removal, reclaiming, and backup.

## Managed Node Groups

We created five managed node groups for different purposes:

1. General
2. Databases
3. ELK
4. Vault
5. Consul

By segregating these node groups based on their function, we can attribute EC2 instances more efficiently. Currently, we are mainly using t3.medium instances, with a minimum of one and a maximum of five per node group. This setup balances cost efficiency and resource availability. Please note that in a production environment, more instances should be allocated.

## Security Groups

Kubernetes components are currently configured to communicate with all nodes on all ports. While this ensures effective communication within the Kubernetes system, it is not strictly necessary and could reduce security. Communication between nodes is restricted to nodes within the same security group, providing a balance between inter-node communication and security.

## VPC Configuration

Our VPC is configured with a large IP range, providing 65,536 IP addresses. This sizeable pool allows us flexibility in expanding our services as needed.

## Important Security Note

Please note that this cluster currently has SSH on port 22 and kubectl control on port 6443 enabled. This configuration presents a significant security risk and is only in place for ongoing development and debugging purposes. In a production environment, access to these ports should be tightly controlled or disabled to maintain cluster security.
