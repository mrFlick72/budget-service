resource "aws_dynamodb_table" "search_tag_table" {
  name         = var.search_tag_table_name
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "user_name"
  range_key    = "search_tag_key"

  attribute {
    name = "role_name"
    type = "S"
  }

  attribute {
    name = "search_tag_key"
    type = "S"
  }

  tags = {
    Name        = var.search_tag_table_name
    Environment = var.environment
  }
}
