#!/usr/bin/env groovy
@Library("porter-jenkins-lib@master") _

// GKE deployment setting for the application
def appName = "{{ cookiecutter._repo_name }}"
def environment = "stg" //rmi build always in stage

def progressDeadlineSeconds = 180

coreJenkinsWorkerNode(
        environment: environment,
        appName: appName,
        slackChannel: "#data-jenkins-hub",
        //the gke cluster where the training will be executed
        //set to default since
        // 1. we're reusing coreJenkinsWorkerNode that also interfaces to Kubernetes cluster
        // 2. we don't want to expose to client (i.e. rm.yaml)
        //we can remove them once we created a new helper function specific to rmi
        clusterProjectId: "tvlk-data-dev-179204",
        clusterName: "tvlk-data-dev",
        clusterZone: "asia-southeast1-a",

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

    stage('Test') {
        container('python') {
            //install the requirement from this app
            sh "pip install -r requirements.txt"
            sh "pip install -r test-requirements.txt"

            // run unit test here, using pytest or unittest
            sh "pytest tests"
        }
    }

    stage('Build') {
        sh "wget https://storage.googleapis.com/rmi-releases/latest/linux/amd64/rmi.tar.gz"
        sh "tar xzf rmi.tar.gz"
        sh "chmod +x rmi"
        sh "./rmi build"
    }
}