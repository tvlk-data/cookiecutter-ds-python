#!/usr/bin/env groovy
@Library("porter-jenkins-lib@master") _

// GKE deployment setting for the application
def appName = "{{ cookiecutter._repo_name }}"
def environment = "prod"

def progressDeadlineSeconds = 300

coreJenkinsWorkerNode(
    environment: environment,
    appName: appName,
    slackChannel: "#data-jenkins-hub",
    // The gke cluster where the training will be executed
    // Set to default
    clusterProjectId: "tvlk-data-mlplatform-prod",
    clusterName: "rm-training",
    clusterZone: "asia-southeast1-a",
    jenkinsSlaveServiceAccountCredentialId: "rm-jenkins-bot-prod",
    additionalContainers:[
        containerTemplate(name: 'python', image: 'python:3.6', ttyEnabled: true, command: 'python')
    ]
){
    stage('Checkout') {
        checkout scm
    }

    def commitId = sh(returnStdout: true, script: "git rev-parse HEAD").trim().take(6)

    currentBuild.displayName = "#$BUILD_NUMBER ${commitId}"
    currentBuild.description = "Commit Hash: ${commitId}"

    // UNCOMMENT THIS SECTION IF YOU WANT TO ADD UNIT TEST
    // stage('Test') {
    //     container('python') {
    //         //install the requirement from this app
    //         sh "pip install -r requirements.txt"
    //         sh "pip install -r test-requirements.txt"

    //         // run unit test here, using pytest or unittest
    //         sh "pytest tests"
    //     }
    // }

    stage('Build') {
        withCredentials([
            string(credentialsId: 'rmi-access-token-prod', variable: 'RMI_ACCESS_TOKEN'),
            file(credentialsId: 'rm-jenkins-bot-prod', variable: 'GOOGLE_APPLICATION_CREDENTIALS')
        ]) {
            sh "wget https://storage.googleapis.com/rmi-releases/latest/linux/amd64/rmi.tar.gz"
            sh "tar xzf rmi.tar.gz"
            sh "chmod +x rmi"
            sh "./rmi version"
            sh "./rmi build"
        }
    }
}