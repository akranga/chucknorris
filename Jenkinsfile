#!/usr/bin/env groovy

podTemplate(
        inheritFrom: 'agilestacks',
        label: 'java',
        containers: [
                containerTemplate(
                        name: 'java',
                        image: 'java:jdk',
                        ttyEnabled: true,
                        command: 'cat'
                )
        ]
) {
    node('java') {
        stage('Build') {
            container('java') {
                echo "pwd()/gradlew clean jar"

                sh "sleep 300"
                // sh "${pwd()}/gradlew clean jar"
                // sh ""
            }
        }
    }
}