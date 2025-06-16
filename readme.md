# Uploader App

A simple web application that allows users to:

-  Upload files to an S3 bucket
-  View uploaded files with pagination directly from S3

## 🚀 Features

- Upload images or files to a private Amazon S3 bucket
- List and view uploaded items with pagination
- Built with AWS ECS, S3, and ECR
- CI/CD via GitHub Actions

## 🛠️ Tech Stack

- Java (Spring Boot)
- AWS S3 (storage)
- AWS ECS + Fargate (deployment)
- AWS ECR (container image)
- GitHub Actions (CI/CD)

## Endpoints

- `POST /api/s3` – Upload a file
- `GET /api/s3/bucket/all` – Get a paginated list of files from S3

## 🧪 Running Locally

```bash
# Build the project
mvn clean package

# Run with Docker (assuming env vars are configured)
docker build -t uploader-app .
docker run -p 8080:8080 uploader-app with environments

AWS_S3_BUCKET=
AWS_ACCESS_KEY=
AWS_SECRET_KEY=
AWS_REGION=

```
