#!/bin/bash

sudo chown -R  $USER *
chmod 775 -R docker/dynamodb

aws dynamodb create-table \
  --table-name BUDGET_SERVICE_SEARCH_TAGS_STAGING \
  --key-schema AttributeName=USER_NAME,KeyType=HASH AttributeName=SEARCH_TAG_KEY,KeyType=RANGE \
  --attribute-definitions AttributeName=USER_NAME,AttributeType=S AttributeName=SEARCH_TAG_KEY,AttributeType=S \
  --billing-mode PAY_PER_REQUEST \
  --endpoint http://localhost:8000 --region us-east-1


aws dynamodb   delete-table --table-name BUDGET_SERVICE_SEARCH_TAGS_STAGING   --endpoint http://localhost:8000 --region us-east-1