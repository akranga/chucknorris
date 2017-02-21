#!/usr/bin/env groovy

def awsRegion
def dockerRegistry
def app = 'chucknorris'
def tag

node('master') {
    awsRegion = env.AWS_REGION
    dockerRegistry = env.PRIVATE_REGISTRY

    stage('Checkout') {
        checkout scm
        tag = sh(script: 'git rev-parse HEAD', returnStdout: true).take(6)
    }
}

podTemplate(
        inheritFrom: 'agilestacks',
        label: 'chucknorris',
        containers: [
                containerTemplate(
                        name: 'java',
                        image: 'openjdk:8',
                        ttyEnabled: true,
                        command: 'cat'
                ),
                containerTemplate(
                        name: 'aws',
                        image: 'agilestacks/aws',
                        ttyEnabled: true,
                        command: 'cat'
                ),
                containerTemplate(
                        name: 'docker',
                        image: 'docker',
                        ttyEnabled: true,
                        command: 'cat'
                ),
                containerTemplate(
                        name: 'kubectl',
                        image: 'agilestacks/kubectl',
                        ttyEnabled: true,
                        command: 'cat'
                )
        ],
        volumes: [
                hostPathVolume(
                        hostPath: '/var/run/docker.sock',
                        mountPath: '/var/run/docker.sock'
                )
        ]
) {
    node('chucknorris') {
        stage('Build') {
            container('java') {
                sh './gradlew clean build allureReport'
                archiveArtifacts 'build/libs/chnorr-*.jar'
                junit 'build/test-results/*.xml'
                publishHTML([
                        reportDir: 'build/allure-report',
                        reportFiles: 'index.html',
                        reportName: 'Allure Report',
                        keepAll: true
                ])
            }
        }
        stage('Publish') {
            def dockerLogin
            container('aws') {
                dockerLogin = sh(
                        script: "aws ecr get-login --region $awsRegion",
                        returnStdout: true
                ).trim()
            }
            container('docker') {
                sh """
                $dockerLogin
                docker build -t $dockerRegistry:latest -t $dockerRegistry:$tag .
                docker push $dockerRegistry
                """
            }
        }
        stage('Deploy') {
            container('kubectl') {
                writeFile file: './deployment.yaml', text: """
apiVersion: v1
kind: Namespace
metadata:
  name: '$app'
---
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: '$app'
  namespace: '$app'
  labels:
    app: '$app'
spec:
  replicas: 1
  selector:
    matchLabels:
      app: '$app'
  template:
    metadata:
      name: '$app'
      labels:
        app: '$app'
        build: $tag
    spec:
      containers:
      - name: '$app'
        image: '$dockerRegistry:$tag'
        ports:
        - name: http
          containerPort: 8080
        livenessProbe:
          httpGet:
            port: http
          initialDelaySeconds: 1
        readinessProbe:
          httpGet:
            port: http
          initialDelaySeconds: 1
---
apiVersion: v1
kind: Service
metadata:
  name: '$app'
  namespace: '$app'
  labels:
    app: '$app'
spec:
  type: LoadBalancer
  ports:
  - port: 80
    targetPort: http
  selector:
    app: '$app'
                """
                sh "kubectl apply -f ./deployment.yaml --namespace '$app'"
            }
        }
    }
}
