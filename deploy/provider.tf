terraform {
  backend "s3" {
    encrypt = true
  }
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.21.0"
    }
    kubectl = {
      source  = "gavinbunney/kubectl"
      version = ">= 1.14.0"
    }
    helm = {
      source  = "hashicorp/helm"
      version = ">= 2.6.0"
    }
    tls = {
      source  = "hashicorp/tls"
      version = "4.0.4"
    }


  }
}

provider "aws" {

  region = var.region

}

locals {
  prefix = "${lower(var.project)}-${lower(terraform.workspace)}"
  common_tags = {
    Environment = terraform.workspace
    Project     = var.project
    Owner       = var.owner_name
    ExpiryDate  = var.expiry_date
  }
}


data "aws_secretsmanager_secret_version" "creds" {
  secret_id = "docdb_password"
}
locals {
  db_creds = jsondecode(
    data.aws_secretsmanager_secret_version.creds.secret_string
  )
}

output "common_tags" {
  value = local.common_tags
}
output "prefix" {
  value = local.prefix
}
#provider "kubectl" {
#  host                   = data.aws_eks_cluster.default.endpoint
#  cluster_ca_certificate = base64decode(data.aws_eks_cluster.default.certificate_authority[0].data)
#  load_config_file       = false
#
#  exec {
#    api_version = "client.authentication.k8s.io/v1beta1"
#    args        = ["eks", "get-token", "--cluster-name", aws_eks_cluster.cluster.id]
#    command     = "aws"
#  }
#}

