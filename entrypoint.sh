#!/bin/sh

export AWS_ACCESS_KEY_ID="${AWS_ACCESS_KEY}"
export AWS_SECRET_ACCESS_KEY="${AWS_SECRET_KEY}"

# Optional logging
echo "Starting app with AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID"
echo "Region: $AWS_REGION"
echo "Bucket: $AWS_S3_BUCKET"

echo "pg = $POSTGRES_DB"
echo "pg_ser = $POSTGRES_USER"
echo "pg_pass = $POSTGRES_PASSWORD"
echo "pg_host = $POSTGRES_HOST"
echo "pg_port = $POSTGRES_PORT"


exec java -jar app.jar
