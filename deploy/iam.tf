module "allow_eks_access_iam_policy" {
  source  = "terraform-aws-modules/iam/aws//modules/iam-policy"
  version = "5.33.0"
  #  version = "5.3.1"

  name          = "allow-eks-access"
  create_policy = true
  policy = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Effect" : "Allow",
        "Action" : [
          "eks:*"
        ],
        "Resource" : "*"
      },
      {
        "Effect" : "Allow",
        "Action" : "iam:PassRole",
        "Resource" : "*",
        "Condition" : {
          "StringEquals" : {
            "iam:PassedToService" : "eks.amazonaws.com"
          }
        }
      }
    ]
  })
  # policy = jsonencode({
  #   Version = "2012-10-17"
  #   Statement = [
  #     {
  #       Action = [
  #         "eks:*",
  #       ]
  #       Effect   = "Allow"
  #       Resource = "*"
  #     },
  #
  #   ]
  # })
}

module "eks_admins_iam_role" {
  source  = "terraform-aws-modules/iam/aws//modules/iam-assumable-role"
  version = "5.33.0"
  #  version = "5.3.1"

  role_name         = "${local.prefix}-admin"
  create_role       = true
  role_requires_mfa = false

  custom_role_policy_arns = [module.allow_eks_access_iam_policy.arn, "arn:aws:iam::aws:policy/AmazonS3FullAccess"]

  trusted_role_arns = [
    "arn:aws:iam::${module.vpc.vpc_owner_id}:root"
  ]
}

#module "user1_iam_user" {
#  source  = "terraform-aws-modules/iam/aws//modules/iam-user"
#  version = "5.33.0"
#  #  version = "5.3.1"
#
#  name                          = "${lower(terraform.workspace)}-user1"
#  create_iam_access_key         = false
#  create_iam_user_login_profile = false
#
#  force_destroy = true
#}

module "allow_assume_eks_admins_iam_policy" {
  source  = "terraform-aws-modules/iam/aws//modules/iam-policy"
  version = "5.33.0"
  #  version = "5.3.1"

  name          = "allow-assume-eks-admin-iam-role"
  create_policy = true

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "sts:AssumeRole",
        ]
        Effect   = "Allow"
        Resource = [module.eks_admins_iam_role.iam_role_arn, "arn:aws:iam::858728894252:role/siemens/OPS_CloudAdminEngineer"]
      },
    ]
  })
}

#module "eks_admins_iam_group" {
#  source  = "terraform-aws-modules/iam/aws//modules/iam-group-with-policies"
#  version = "5.33.0"
#  #  version = "5.3.1"
#
#  name                              = "${local.prefix}-admin"
#  attach_iam_self_management_policy = false
#  create_group                      = true
#  group_users                       = [module.user1_iam_user.iam_user_name]
#  custom_group_policy_arns          = [module.allow_assume_eks_admins_iam_policy.arn]
#}