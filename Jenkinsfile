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
                def workdir = pwd()
                echo "Running build to ${workdir}"
                dir('pwd()') {
                    sh './gradlew clean jar'
                }
            }
        }
    }
}