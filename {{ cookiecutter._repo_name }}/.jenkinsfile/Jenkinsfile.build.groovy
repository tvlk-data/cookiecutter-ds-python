#!/usr/bin/env groovy
@Library("porter-jenkins-lib@master") _

// image registry project
def gcrIOProjectId = "{{ cookiecutter._gcr_io_project_id }}"" //hardcoded in rmi source code

// GKE deployment setting for the application
def appName = "{{ cookiecutter._repo_name }}"
def appNamespace = "{{ cookiecutter._namespace }}"
def environment = "stg" //rmi build always in stage

def progressDeadlineSeconds = 180

coreJenkinsWorkerNode(
        environment: environment,
        appName: appName,
        slackChannel: "#data-jenkins-hub",
        //the gke cluster where the application is deployed
        clusterProjectId: "{{ cookiecutter._namespace }}",
        clusterName: "{{ cookiecutter._training_cluster_name }}",
        clusterZone: "{{ cookiecutter._training_cluster_zone }}",

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
            //install the requirement from this python application
            sh "pip install -r requirements.txt"

            // run unit test here
            echo "Run unit test here!"
        }
    }

    stage('Build') {
        sh "wget https://storage.googleapis.com/rmi-releases/latest/linux/amd64/rmi.tar.gz"
        sh "tar xzf rmi.tar.gz"
        sh "chmod +x rmi"
        sh "./rmi build"
    }


}