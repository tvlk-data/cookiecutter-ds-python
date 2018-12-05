#!/usr/bin/env groovy
@Library("porter-jenkins-lib@master") _

// GKE deployment setting for the application
def appName = "{{ cookiecutter._repo_name }}"
def environment = "test"

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

  //additional container that you need to perform the test
  additionalContainers:[
    containerTemplate(name: 'python', image: 'python:3.6', ttyEnabled: true, command: 'python')
  ]
){

  stage('Checkout') {
    checkout scm
    sh 'printenv'
  }

  def commitId = sh(returnStdout: true, script: "git rev-parse HEAD").trim().take(6)

  currentBuild.displayName = "#$BUILD_NUMBER ${commitId}"
  currentBuild.description = "Commit Hash: ${commitId}"

  stage('Test') {
    container('python') {
      //install the requirement and run UT
      sh "pip install -r requirements.txt && pip install pytest==3.5.* && pytest tests"
    }
  }
}