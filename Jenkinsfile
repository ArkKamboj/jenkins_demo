pipeline {
    agent any

    environment {
        PATH = "C:\\Users\\Ark\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin;${env.PATH}"
        IMAGE_NAME = "cicdproj:latest"
    }

    stages {
        stage('Checkout') {
            steps {
                echo '=== Stage 1: Checkout Source Code ==='
                checkout scm
            }
        }

        stage('Docker Build') {
            steps {
                echo '=== Stage 2: Building Docker Image ==='
                bat 'docker build -t %IMAGE_NAME% .'
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo '=== Stage 3: Deploying to Kubernetes ==='
                bat 'kubectl apply -f k8s/'
                bat 'kubectl rollout status deployment/cicdproj-deployment --timeout=120s'
            }
        }

        stage('Verify Deployment') {
            steps {
                echo '=== Stage 4: Verifying Kubernetes Resources ==='
                bat 'kubectl get pods -l app=cicdproj'
                bat 'kubectl get svc cicdproj-service'
            }
        }
    }

    post {
        success {
            echo '========================================='
            echo ' CI/CD Pipeline Completed Successfully!  '
            echo ' App URL: http://localhost:30081/api/demo/hello '
            echo '========================================='
        }
        failure {
            echo 'Pipeline failed. Check stage logs for details.'
        }
    }
}
