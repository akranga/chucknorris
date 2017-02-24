#!/usr/bin/env groovy

@NonCPS
def render(template, bindings = [:]) {
    return new groovy.text.SimpleTemplateEngine()
            .createTemplate(template)
            .make(bindings)
            .toString()
}

def awsRegion
def dockerRegistry
def app = 'chucknorris'
def host
def tag

node('master') {
    awsRegion = env.AWS_REGION
    dockerRegistry = env.PRIVATE_REGISTRY
    host = "${app}.app.${env.ROOT_DOMAIN_NAME}"

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
                ),
                containerTemplate(
                        name: 'curl',
                        image: 'appropriate/curl',
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
                try {
                    sh './gradlew clean build allureReport'
                } finally {
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
        stage('Deploy: test') {
            container('kubectl') {
                def namespace = "$app-test"
                def template = readFile 'deployment.yaml'
                def deployment = render template, [
                        app: app,
                        namespace: namespace,
                        replicas: 1,
                        host: "test.$host",
                        dockerRegistry: dockerRegistry,
                        tag: tag
                ]
                writeFile file: "deployment.${namespace}.yaml", text: deployment
                sh "kubectl apply -f ./deployment.${namespace}.yaml --namespace '$namespace'"
            }
        }
        stage('Test') {
            container('curl') {
                sh """
                for i in `seq 20`; do
                    curl -sSf http://test.$host > response && break
                    sleep 10
                done
                cat response | grep Chuck
                """
            }
        }
        stage('Deploy: prod') {
            container('kubectl') {
                def namespace = "$app-prod"
                def template = readFile 'deployment.yaml'
                def deployment = render template, [
                        app: app,
                        namespace: namespace,
                        replicas: 1,
                        host: host,
                        dockerRegistry: dockerRegistry,
                        tag: tag
                ]
                writeFile file: "deployment.${namespace}.yaml", text: deployment
                sh "kubectl apply -f ./deployment.${namespace}.yaml --namespace '$namespace'"
            }
        }
    }
}
