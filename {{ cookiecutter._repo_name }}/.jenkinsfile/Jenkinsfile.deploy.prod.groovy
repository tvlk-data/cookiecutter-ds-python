#!/usr/bin/env groovy
@Library("porter-jenkins-lib@master") _

// GKE deployment setting for the application
def appName = "{{ cookiecutter._repo_name }}"
def environment = "prod"

coreJenkinsWorkerNode(
    environment: environment,
    appName: appName,
    slackChannel: "#data-jenkins-hub",
    // The gke cluster where the model serving will be deployed
    // Set to default
    clusterProjectId: "tvlk-data-mlplatform-prod",
    clusterName: "rm-deployment",
    clusterZone: "asia-southeast1-a"
){
    stage('Checkout') {
        checkout scm
    }
    def plainTextName = "raring-meerkat-model-builder.json"
    def cipherTextName = "raring-meerkat-model-builder.json.enc"
    def cipherTextLocation = "gs://raring-meerkat-common-kms/${cipherTextName}"

    currentBuild.displayName = "RUN_ID: ${runId}"
    currentBuild.description = "RUN_ID: ${runId}"

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

    stage('Deploy') {
        sh "gsutil cp ${cipherTextLocation} ."
        sh """
            gcloud kms decrypt \
                --project tvlk-data-mlplatform-prod \
                --location global \
                --keyring raring-meerkat-common \
                --key model-builder \
                --ciphertext-file ${cipherTextName} \
                --plaintext-file ${plainTextName}
        """
        sh "wget https://storage.googleapis.com/rmi-releases/master-cd/latest/linux/amd64/rmi.tar.gz"
        sh "tar xzf rmi.tar.gz"
        sh "chmod +x rmi"
        sh "GOOGLE_APPLICATION_CREDENTIALS=${plainTextName} ./rmi version"
        sh "GOOGLE_APPLICATION_CREDENTIALS=${plainTextName} ./rmi build serving ${runId}"
        sh "GOOGLE_APPLICATION_CREDENTIALS=${plainTextName} ./rmi deploy ${runId} --env prod"
    }
}
