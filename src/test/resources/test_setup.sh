#!/bin/bash

sudo chown -R  $USER *
chmod 775 -R docker/dynamodb

aws dynamodb create-table --table-name BUDGET_SERVICE_SEARCH_TAGS_STAGING --key-schema -key-schema AttributeName=USER_NAME,KeyType=HASH AttributeName=SongTitle,KeyType=KEY --endpoint http://localhost:8000 --region us-east-1
