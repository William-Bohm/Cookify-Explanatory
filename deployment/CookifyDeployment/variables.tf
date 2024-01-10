# 
# EKS
# 
variable "name" {
  description = "EKS Cluster Name"
  type        = string
  default     = "Cookify"
}

resource "random_string" "suffix" {
  length  = 4
  special = false
  upper   = false
}

locals {
  name = "${var.name}-${random_string.suffix.result}"
}


# 
# EC2
# 
variable "database_ec2_instance_type" {
    description = "EC2 instance to use for databases"
    type = string
    default = "t3.micro"
}
variable "ELK_ec2_instance_type" {
    description = "EC2 instance to use for ELK log aggregate"
    type = string
    default = "t3.micro"
}
variable "Vault_ec2_instance_type" {
    description = "EC2 instance to use for Vault secrets engine"
    type = string
    default = "t3.micro"
}
variable "General_ec2_instance_type" {
    description = "EC2 instance to use for general services"
    type = string
    default = "t3.micro"
}
variable "Consul_ec2_instance_type" {
    description = "EC2 instance to use for consul cluster"
    type = string
    default = "t3.micro"
}


# 
# AWS
# 
variable "vpc_region" {
  type        = string
  description = "The AWS region to create resources in"
  default     = "us-west-2"
}

# 
# Consul
# 
variable "consul_version" {
  type        = string
  description = "The Consul version"
  default     = "v1.14.4"
}

variable "api_gateway_version" {
  type        = string
  description = "The Consul API gateway CRD version to use"
  default     = "0.5.1"
}