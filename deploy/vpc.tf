data "aws_availability_zones" "available" {}


locals {
  cluster_name = "${var.project}-${var.environment}-${var.cluster_name}"
}

module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "5.4.0"

  name            = "${local.prefix}-vpc"
  cidr            = var.vpc_cidr
  azs             = data.aws_availability_zones.available.names
  private_subnets = var.private_subnet_cidrs
  public_subnets  = var.public_subnet_cidrs

  enable_ipv6                   = false
  create_database_subnet_group  = false
  manage_default_route_table    = false
  manage_default_network_acl    = false
  manage_default_security_group = false
  enable_dns_hostnames          = true
  enable_dns_support            = true
  #  enable_classiclink_dns_support = false
  enable_nat_gateway                   = true
  single_nat_gateway                   = true
  one_nat_gateway_per_az               = false
  enable_vpn_gateway                   = false
  enable_dhcp_options                  = false
  enable_flow_log                      = true
  create_flow_log_cloudwatch_log_group = true
  create_flow_log_cloudwatch_iam_role  = true
  flow_log_max_aggregation_interval    = 60
  public_subnet_tags = {
    "kubernetes.io/cluster/${local.prefix}" = "owned"
    "kubernetes.io/role/elb"                = "1"
  }

  private_subnet_tags = {
    "kubernetes.io/cluster/${local.prefix}" = "owned"
    "kubernetes.io/role/internal-elb"       = "1"
  }

  igw_tags = {
    "Name" = "${local.prefix}-ig"
  }

  nat_gateway_tags = {
    "Name" = "${local.prefix}-ng"
  }
  tags = local.common_tags
  #tags = {
  #  "Environment" = var.environment
  #  "Project"     = var.project
  #}

}
