
provider "kubernetes" {
  host                   = module.eks.cluster_endpoint
  cluster_ca_certificate = base64decode(module.eks.cluster_certificate_authority_data)

  exec {
    api_version = "client.authentication.k8s.io/v1beta1"
    command     = "aws"
    # This requires the awscli to be installed locally where Terraform is executed
    args = ["eks", "get-token", "--cluster-name", module.eks.cluster_name]
  }
}

module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "19.21.0"
  #    version = "18.29.0"

  cluster_name    = local.prefix
  cluster_version = var.cluster_version

  cluster_endpoint_private_access = true
  cluster_endpoint_public_access  = true

  cluster_addons = {
    coredns = {
      #      most_recent = true
      addon_version = "v1.10.1-eksbuild.6"
    }
    kube-proxy = {
      #      most_recent = true
      addon_version = "v1.28.4-eksbuild.1"
    }
    vpc-cni = {
      #      most_recent = true
      addon_version = "v1.15.4-eksbuild.1"
    }
    aws-ebs-csi-driver = {
      #      most_recent              = true
      addon_version            = "v1.26.0-eksbuild.1"
      service_account_role_arn = module.ebs_csi_irsa_role.iam_role_arn
    }
  }
  cluster_enabled_log_types = ["api", "audit", "authenticator", "controllerManager", "scheduler"]
  # Encryption key
  create_kms_key = true
  cluster_encryption_config = {
    resources = ["secrets"]
  }
  #  cluster_encryption_config = [{
  #    resources = ["secrets"]
  #  }]
  kms_key_deletion_window_in_days = 7
  enable_kms_key_rotation         = true

  vpc_id     = module.vpc.vpc_id
  subnet_ids = module.vpc.private_subnets

  enable_irsa = true

  eks_managed_node_group_defaults = {
    disk_size = 50
  }

  eks_managed_node_groups = {
    "${lower(terraform.workspace)}" = {
      min_size     = 1
      max_size     = 4
      desired_size = 1
      labels = {
        role = "general"
      }

      instance_types = ["${var.node_instance_type}"]
      capacity_type  = "ON_DEMAND"
      iam_role_additional_policies = {
        "cloudwatch"                   = "arn:aws:iam::aws:policy/CloudWatchLogsFullAccess"
        "SSMManagedInstance"           = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
        "SSMManagedEC2InstanceDefault" = "arn:aws:iam::aws:policy/AmazonSSMManagedEC2InstanceDefaultPolicy"
      }
      #      iam_role_additional_policies = [
      #        "arn:aws:iam::aws:policy/CloudWatchLogsFullAccess"
      #      ]
    }
  }

  manage_aws_auth_configmap = true
  aws_auth_roles = [
    {
      rolearn  = module.eks_admins_iam_role.iam_role_arn
      username = module.eks_admins_iam_role.iam_role_name
      groups   = ["system:masters"]
    },
    {
      rolearn  = "arn:aws:iam::${module.vpc.vpc_owner_id}:role/siemens/OPS_CloudAdminEngineer"
      username = "OPS_CloudAdminEngineer"
      groups   = ["system:masters"]
    },
  ]
  cluster_security_group_additional_rules = {
    inress_ec2_tcp = {
      description              = "Access EKS from EC2 instance."
      protocol                 = "tcp"
      from_port                = 443
      to_port                  = 443
      type                     = "ingress"
      source_security_group_id = aws_security_group.bastion.id
    }
  }
  node_security_group_additional_rules = {
    #    ingress_allow_access_from_control_plane = {
    #      type                          = "ingress"
    #      protocol                      = "tcp"
    #      from_port                     = 9443
    #      to_port                       = 9443
    #      source_cluster_security_group = true
    #      description                   = "Allow access from control plane to webhook port of AWS load balancer controller"
    #    }
  }
  cloudwatch_log_group_retention_in_days = var.cloudwatch_log_group_retention_in_days

  tags = local.common_tags
  #tags = {
  #  Environment = var.environment
  #}
}

module "ebs_csi_irsa_role" {
  source = "terraform-aws-modules/iam/aws//modules/iam-role-for-service-accounts-eks"

  role_name             = "${local.prefix}-ebs-csi"
  attach_ebs_csi_policy = true

  oidc_providers = {
    ex = {
      provider_arn               = module.eks.oidc_provider_arn
      namespace_service_accounts = ["kube-system:ebs-csi-controller-sa"]
    }
  }
}

# https://github.com/terraform-aws-modules/terraform-aws-eks/issues/2009


