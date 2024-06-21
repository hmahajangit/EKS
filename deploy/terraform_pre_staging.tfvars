region       = "eu-central-1"
project      = "nextwork"
environment  = "pre-staging"
cluster_name = "eks-cluster"

#service = ["autoscaling", "ecr.api", "ecr.dkr", "ec2", "ec2messages", "elasticloadbalancing", "sts", "kms", "logs", "ssm", "ssmmessages"]

cluster_version = "1.28"

vpc_cidr = "10.0.0.0/16"
# change eks node size
node_instance_type = "t2.medium"

#variable "private_rt_id" {
#  type = list(string)
#  default = ["rtb-058cad72e88ac730a"]
#  description = "private route table id for s3 endpoint"
#}
cluster_log_retention_period           = 30
cloudwatch_log_group_retention_in_days = 30

#allowed_security_groups = []

#cidr for eks plane
allowed_cidr_blocks = ["0.0.0.0/0"]
enabled             = "true"

public_subnet_cidrs = ["10.0.0.0/18", "10.0.64.0/18"]

private_subnet_cidrs = ["10.0.128.0/18", "10.0.192.0/18"]

##RDS###

#instance_type = "db.t3.large" #"db.m5.large"
#
#allocated_storage = 50
#backup_window     = "18:30-19:00"
#
#
#maintenance_window = "sun:20:30-sun:21:30"
#
#auto_minor_version_upgrade = "true"
#
#final_snapshot_identifier = "postgresql-qa-rds-snapshot"
#
#skip_final_snapshot     = "false"
#db_name                 = "ivl-qa"
#copy_tags_to_snapshot   = "true"
#backup_retention_period = 15
#publicly_accessible     = false
#multi_az                = true

# S3 bucket
infrequent_transition_days = 30

glacier_transition_days = 90

expiration_days = 180

##########  Database details  ##########
#db_username = "nextworkdb"
#//db_password       = "test123$"
#db_instance_class = "db.t2.small"
#db_multi_az       = false
#db_engine_version = 12.14


### DB enable log export ####
docdb_enabled_cloudwatch_logs_exports = ["audit", "profiler"]

##########  Document Database details  ##########
docdb_username = "nextworkdb"
#docdb_instance_class = "db.t4g.medium"
docdb_instance_class = "db.t3.medium"
##[ci skip]

###########  Snapshot Database details  ##########
#nextwork_db_snapshot    = "rds:nextwork-staging-db-2023-12-05-08-10"
nextwork_docdb_snapshot = "rds:nextwork-staging-docdb-2024-02-27-08-13"