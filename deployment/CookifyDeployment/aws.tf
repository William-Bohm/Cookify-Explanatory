# 
# fetch all availability zones
# 
data "aws_availability_zones" "available" {
  state = "available"

  filter {
    name   = "opt-in-status"
    values = ["opt-in-not-required"]
  }
}

# 
# EKS Backups
# 
resource "aws_iam_role" "backup_role" {
  name               = "${local.name}-backup-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Action = "sts:AssumeRole",
      Effect = "Allow",
      Principal = {
        Service = "backup.amazonaws.com"
      }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "aws_backup_policy" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSBackupServiceRolePolicyForBackup"
  role       = aws_iam_role.backup_role.name
}

resource "aws_iam_role_policy_attachment" "aws_backup_policy_for_restores" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSBackupServiceRolePolicyForRestores"
  role       = aws_iam_role.backup_role.name
}

resource "aws_backup_vault" "cookify_backup_vault" {
  name = "cookify-backup-vault"
}

resource "aws_backup_plan" "example" {
  name = "${local.name}-backup-plan"

  rule {
    rule_name         = "${local.name}-backup-rule"
    target_vault_name = aws_backup_vault.cookify_backup_vault.name
    schedule          = "cron(0 12 * * ? *)" # Every day at 12:00 UTC
  }
}

# resource "aws_backup_selection" "example" {
#   iam_role_arn = aws_iam_role.backup_role.arn
#   name         = "${local.name}-backup-selection"
#   plan_id      = aws_backup_plan.example.id

#   resources = [
#     # "arn:aws:ec2:${var.vpc_region}:${var.account_id}:vpc/${module.vpc.vpc_id}",
#     # # Add ARNs of other resources (RDS, EBS, DynamoDB) if needed
#   ]
# }




# 
# vpc
# 
module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "3.19.0"

  name = local.name

  cidr = "10.0.0.0/16"
  azs  = slice(data.aws_availability_zones.available.names, 0, 3)

  # Adjusting the subnets to /22 for larger IP address ranges
  private_subnets = ["10.0.0.0/22", "10.0.4.0/22", "10.0.8.0/22"]
  public_subnets  = ["10.0.12.0/22", "10.0.16.0/22", "10.0.20.0/22"]


  enable_nat_gateway   = true
  single_nat_gateway   = true
  enable_dns_hostnames = true

  public_subnet_tags = {
    "kubernetes.io/cluster/${local.name}" = "shared"
    "kubernetes.io/role/elb"              = "1"
  }

  private_subnet_tags = {
    "kubernetes.io/cluster/${local.name}" = "shared"
    "kubernetes.io/role/internal-elb"     = "1"
  }
}



# 
# EKS Security
# 
resource "aws_security_group" "eks_security_group" {
  name        = "${local.name}-eks"
  description = "Security group for EKS cluster"
  vpc_id      = module.vpc.vpc_id

# Kubernetes API server
  ingress {
    from_port   = 6443 
    to_port     = 6443
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }

  # Allow SSH access for testing
  ingress {
    from_port   = 22 
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # Allowing from anywhere, be cautious
  }

# Allow all outbound traffic
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1" 
    cidr_blocks = ["0.0.0.0/0"]
  }
}


# 
# EKS
# 
data "aws_eks_cluster" "cluster" {
  name = module.eks.cluster_name
}

data "aws_eks_cluster_auth" "cluster" {
  name = module.eks.cluster_name
}

module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "19.5.1"

  cluster_name    = local.name
  cluster_version = "1.24"

  vpc_id                         = module.vpc.vpc_id
  subnet_ids                     = concat(module.vpc.private_subnets, module.vpc.public_subnets)
  cluster_endpoint_public_access = true

  eks_managed_node_group_defaults = {
    ami_type = "AL2_x86_64"
  }

  # Define multiple node groups for different workloads
  eks_managed_node_groups = {
    general = {
      name = "general"

      instance_types = [var.General_ec2_instance_type]
      min_size       = 1
      max_size       = 5
      desired_size   = 2

      labels = {
        group = "general"
      }
    },
    databases = {
      name = "databases"

      instance_types = [var.database_ec2_instance_type]
      min_size       = 1
      max_size       = 3
      desired_size   = 2

        labels = {
        group = "databases"
      }
    },
    elk = {
      name = "elk"

      instance_types = [var.ELK_ec2_instance_type]
      min_size       = 1
      max_size       = 2
      desired_size   = 1

        labels = {
        group = "elk"
      }
    },
    vault = {
      name = "vault"

      instance_types = [var.Vault_ec2_instance_type]
      min_size       = 1
      max_size       = 2
      desired_size   = 1

        labels = {
        group = "vault"
      }
    },
    consul = {
      name = "consul"

      instance_types = [var.Consul_ec2_instance_type]
      min_size       = 1
      max_size       = 2
      desired_size   = 1

        labels = {
        group = "consul"
      }
    }
  }

  node_security_group_additional_rules = {
    ingress_self_all = {
      description = "Node to node all ports/protocols"
      protocol    = "-1"
      from_port   = 0
      to_port     = 0
      type        = "ingress"
      self        = true
    }
    ingress_cluster_all = {
      description                   = "Cluster to node all ports/protocols"
      protocol                      = "-1"
      from_port                     = 0
      to_port                       = 0
      type                          = "ingress"
      source_cluster_security_group = true
    }
    egress_all = {
      description      = "Node all egress"
      protocol         = "-1"
      from_port        = 0
      to_port          = 0
      type             = "egress"
      cidr_blocks      = ["0.0.0.0/0"]
      ipv6_cidr_blocks = ["::/0"]
    }
  }
}




# 
# EBS access
# 
resource "kubernetes_storage_class" "database_ebs_storage_class" {
  metadata {
    name = "database-ebs-storage-class"
  }
  storage_provisioner = "ebs.csi.aws.com"
  parameters = {
    type = "gp2"
    fsType = "ext4"
  }

  reclaim_policy = "Retain"
  allow_volume_expansion = true
}

resource "kubernetes_storage_class" "elk_ebs_storage_class" {
  metadata {
    name = "elk-ebs-storage-class"
  }
  storage_provisioner = "ebs.csi.aws.com"
  parameters = {
    type = "gp2"
    fsType = "ext4"
  }

  reclaim_policy = "Retain"
  allow_volume_expansion = true
}


# https://aws.amazon.com/blogs/containers/amazon-ebs-csi-driver-is-now-generally-available-in-amazon-eks-add-ons/ 
data "aws_iam_policy" "ebs_csi_policy" {
arn = "arn:aws:iam::aws:policy/service-role/AmazonEBSCSIDriverPolicy"
}

# create IAM role for EBS CSI driver
module "irsa-ebs-csi" {
source  = "terraform-aws-modules/iam/aws//modules/iam-assumable-role-with-oidc"
version = "4.7.0"

create_role                   = true
role_name                     = "AmazonEKSTFEBSCSIRole-${module.eks.cluster_name}"
provider_url                  = module.eks.oidc_provider
role_policy_arns              = [data.aws_iam_policy.ebs_csi_policy.arn]
oidc_fully_qualified_subjects = ["system:serviceaccount:kube-system:ebs-csi-controller-sa"]
}

# This gives Access to EBS for EKS
resource "aws_eks_addon" "ebs-csi" {
cluster_name             = module.eks.cluster_name
addon_name               = "aws-ebs-csi-driver"
addon_version            = "v1.5.2-eksbuild.1"
service_account_role_arn = module.irsa-ebs-csi.iam_role_arn
tags = {
    "eks_addon" = "ebs-csi"
    "terraform" = "true"
}
}

# 
# Uninstalls consul resources (API Gateway controller, Consul-UI, and AWS ELB, and removes associated AWS resources) on terraform destroy
# 
resource "null_resource" "kubernetes_consul_resources" {
provisioner "local-exec" {
    when    = destroy
    command = "kubectl delete svc/consul-ui --namespace consul && kubectl delete svc/api-gateway --namespace consul"
}
depends_on = [module.eks]
}