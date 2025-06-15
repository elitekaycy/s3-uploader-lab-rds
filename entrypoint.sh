#!/bin/sh

export AWS_ACCESS_KEY_ID="${AWS_ACCESS_KEY}"
export AWS_SECRET_ACCESS_KEY="${AWS_SECRET_KEY}"

# Optional logging
echo "Starting app with AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID"
echo "Region: $AWS_REGION"
echo "Bucket: $AWS_S3_BUCKET"

exec java -jar app.jar
