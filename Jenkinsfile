#!/usr/bin/env groovy

def awsRegion
def dockerRegistry

node('master') {
    awsRegion = env.AWS_REGION
    dockerRegistry = env.PRIVATE_REGISTRY

    stage('Checkout') {
        checkout scm
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
                sh './gradlew clean jar'
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
                def tag = 'akranga/chucknorris'
                sh """
                $dockerLogin
                docker build -t $tag .
                docker tag $tag $dockerRegistry
                docker push $dockerRegistry
                """
            }
        }
    }
}
