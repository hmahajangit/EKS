variable "region" { default = "eu-central-1" }
variable "project" { default = "" }
variable "environment" { default = "" }
variable "cluster_name" {
  default = "eks-cluster"
}
variable "owner_name" {
  type    = string
  default = "ancy.crasta@siemens.com"
}
variable "expiry_date" {
  type    = string
  default = "31/3/2025"
}
#variable "service" {
#  default = ["autoscaling", "ecr.api", "ecr.dkr", "ec2", "ec2messages", "elasticloadbalancing", "sts", "kms", "logs", "ssm", "ssmmessages"]
#}
variable "cluster_version" {
  default = "1.28"
}
variable "vpc_cidr" {
  type = string
}

variable "node_instance_type" {
  type    = string
  default = "t3.micro"
}
#variable "private_rt_id" {
#  type = list(string)
#  default = ["rtb-058cad72e88ac730a"]
#  description = "private route table id for s3 endpoint"
#}
variable "cluster_log_retention_period" {
  type    = number
  default = 30
}

variable "cloudwatch_log_group_retention_in_days" {
  type    = number
  default = 30
}

variable "allowed_security_groups" {
  type        = list(string)
  default     = []
  description = "List of Security Group IDs to be allowed to connect to the EKS cluster"
}

variable "allowed_cidr_blocks" {
  type        = list(string)
  default     = ["0.0.0.0/0"]
  description = "List of CIDR blocks to be allowed to connect to the EKS cluster"
}
variable "enabled" {
  type        = bool
  default     = true
  description = "Set to false to prevent the module from creating any resources"
}
variable "public_subnet_cidrs" {
  type        = list(string)
  description = "Public Subnet CIDR values"
  default     = []
}

variable "private_subnet_cidrs" {
  type        = list(string)
  description = "Private Subnet CIDR values"
  default     = []
}

variable "addons" {
  type = list(object({
    name    = string
    version = string
  }))

  default = [
    {
      name    = "kube-proxy"
      version = "v1.21.2-eksbuild.2"
    },
    {
      name    = "vpc-cni"
      version = "v1.10.1-eksbuild.1"
    },
    {
      name    = "coredns"
      version = "v1.8.4-eksbuild.1"
    },
    {
      name    = "aws-ebs-csi-driver"
      version = "v1.4.0-eksbuild.preview"
    }
  ]
}

#docdb
variable "docdb_engine_version" {
  default = "5.0.0"
}
variable "docdb_port" {
  default = "27017"
}
variable "docdb_instance_count" {
  default = "2"
}
variable "docdb_instance_class" {
  default = "db.t4g.medium"
}
variable "docdb_username" {
}
variable "docdb_password" {
}
variable "nextwork_docdb_snapshot" {
  type    = string
  default = null
}
variable "docdb_enabled_cloudwatch_logs_exports" {
  default = ["profiler"]
}

#RDS
#variable "rds_public_subnets" {
#  default     = []
#  description = "The public subnets for our RDS."
#}
#
#variable "instance_type" {
#  default     = "db.t3.large"
#  type        = string
#  description = "Instance type for database instance"
#}



#variable "allocated_storage" {
#  default     = 50
#  type        = number
#  description = "EBS storage in gb rds"
#}
#
#variable "backup_window" {
#
#  default     = "18:30-19:00"
#  type        = string
#  description = "30 minute time window to reserve for backups"
#}
#
#variable "maintenance_window" {
#
#  default     = "sun:20:30-sun:21:30"
#  type        = string
#  description = "60 minute time window to reserve for maintenance"
#}
#
#variable "auto_minor_version_upgrade" {
#  default     = true
#  type        = bool
#  description = "Minor engine upgrades are applied automatically to the DB instance during the maintenance window"
#}
#
#variable "final_snapshot_identifier" {
#  default     = "postgresql-rds-snapshot"
#  type        = string
#  description = "Identifier for final snapshot if skip_final_snapshot is set to false"
#}
#variable "skip_final_snapshot" {
#  default     = true
#  type        = bool
#  description = "Flag to enable or disable a snapshot if the database instance is terminated"
#}
#
#variable "copy_tags_to_snapshot" {
#  default     = false
#  type        = bool
#  description = "Flag to enable or disable copying instance tags to the final snapshot"
#}
#variable "publicly_accessible" {
#  default     = false
#  type        = bool
#  description = "RDS will be public access if true"
#}
#variable "multi_az" {
#  default     = false
#  type        = bool
#  description = "Multi az will be enable if true"
#}
#variable "backup_retention_period" {
#  default     = 15
#  type        = number
#  description = "Number of days to keep database backups"
#}
#variable "db_name" {
#  default     = "postgres-db"
#  type        = string
#  description = "db name"
#}
#
# S3 bucket
variable "infrequent_transition_days" {
  default     = 30
  type        = number
  description = "infrequent_transition_days"
}

variable "glacier_transition_days" {
  default     = 90
  type        = number
  description = "glacier_transition_days"
}

variable "expiration_days" {
  default     = 180
  type        = number
  description = "expiration_days"
}
#core dns
#v1.10.1-eksbuild.1
#vpc cni 
#
#Version
#v1.12.6-eksbuild.2
#kube proxy 
#
#Version
#v1.27.1-eksbuild.1
