data "aws_ami" "amazon_linux" {
  most_recent = true
  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-2.0.*-x86_64-gp2"]
  }
  owners = ["amazon"]
}

resource "aws_iam_role" "bastion_role" {

  name = "${local.prefix}-bastion-role"

  # Terraform's "jsonencode" function converts a
  # Terraform expression result to valid JSON syntax.
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Sid    = ""
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      },
    ]
  })

  managed_policy_arns = ["arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore", module.allow_assume_eks_admins_iam_policy.arn, module.allow_eks_access_iam_policy.arn]

  tags = { Name = "${local.prefix}-bastion-role" }

}

resource "aws_iam_instance_profile" "bastion_profile" {

  name = "${local.prefix}-bastion-profile"
  role = aws_iam_role.bastion_role.name

  tags = { "Name" = "${local.prefix}-bastion-role-profile" }

}


resource "aws_instance" "bastion" {

  #  ami           = data.aws_ami.amazon_linux.id
  ami           = "ami-08b47394fb4c462b9"
  instance_type = "t2.micro"
  #  subnet_id     = aws_subnet.private_subnets[0].id
  subnet_id = module.vpc.private_subnets[0]

  iam_instance_profile = aws_iam_instance_profile.bastion_profile.name

  vpc_security_group_ids = [
    aws_security_group.bastion.id
  ]

  root_block_device {
    encrypted  = true
    kms_key_id = aws_kms_key.awsebs.arn

  }

  tags = merge(
    local.common_tags,
    { "Name" = "${local.prefix}-bastion" },
  )
  volume_tags = merge(
    local.common_tags,
    { "Name" = "${local.prefix}-bastion" },
  )
}

resource "aws_security_group" "bastion" {
  description = "Control bastion inbound and outbound access"
  name        = "${local.prefix}-bastion"
  vpc_id      = module.vpc.vpc_id
  # Allow all inbound traffic within the VPC
  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = [var.vpc_cidr]
    description = "Allow all inbound traffic within the VPC"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = local.common_tags
}
