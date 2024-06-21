#data "aws_iam_policy_document" "aws-fluent-bit-assume-role-policy" {
#  statement {
#    actions = ["sts:AssumeRoleWithWebIdentity"]
#    effect  = "Allow"
#
#    condition {
#      test     = "StringEquals"
#      variable = "${replace(aws_iam_openid_connect_provider.eks.url, "https://", "")}:sub"
#      values   = ["system:serviceaccount:amazon-cloudwatch:fluent-bit"]
#    }
#
#    principals {
#      identifiers = [module.eks.oidc_provider_arn]
#      type        = "Federated"
#    }
#  }
#}
#
#resource "aws_iam_role" "aws-fluent-bit" {
#  assume_role_policy = data.aws_iam_policy_document.aws-fluent-bit-assume-role-policy.json
#  name               = "${var.project}-${var.environment}-aws-fluent-bit"
#}

module "fluentbit_irsa_role" {
  source    = "terraform-aws-modules/iam/aws//modules/iam-role-for-service-accounts-eks"
  role_name = "${local.prefix}-aws-fluent-bit"
  oidc_providers = {
    main = {
      provider_arn               = module.eks.oidc_provider_arn
      namespace_service_accounts = ["amazon-cloudwatch:fluent-bit"]
    }
  }
}

resource "aws_iam_policy" "aws-fluent-bit-policy" {
  policy = file("./fluent-bit-policy.json")
  name   = "${local.prefix}-fluentbitpolicy"
}

resource "aws_iam_role_policy_attachment" "aws-fluent-bit-attach" {
  #  role       = aws_iam_role.aws-fluent-bit.name
  role       = module.fluentbit_irsa_role.iam_role_name
  policy_arn = aws_iam_policy.aws-fluent-bit-policy.arn
}

output "aws-fluent-bit_role_arn" {
  #  value = aws_iam_role.aws-fluent-bit.arn
  value = module.fluentbit_irsa_role.iam_role_arn
}