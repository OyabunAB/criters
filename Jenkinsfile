#!/usr/bin/env groovy
/*---------------------------------------------------------------------------------------------------
 *     .aMMMb  dMMMMb  dMP dMMMMMMP dMMMMMP dMMMMb  .dMMMb
 *    dMP"VMP dMP.dMP amr    dMP   dMP     dMP.dMP dMP" VP
 *   dMP     dMMMM.  dMP    dMP   dMMMP   dMMMMK   VMMMb
 *  dMP.aMP dMP AMF dMP    dMP   dMP     dMP AMF dP .dMP
 *  VMMMP" dMP dMP dMP    dMP   dMMMMMP dMP dMP  VMMMP"
 *---------------------------------------------------------------------------------------------------
 * Criters Jenkins CI/CD configuration
 *---------------------------------------------------------------------------------------------------
 */
pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr:'10'))
    }
    stages {
        stage('Prepare environment') {
            steps {
                milestone(ordinal: 1, label: 'Prepare pipeline environment')
            }
        }
        stage('Build Proctor') {
            steps {
                milestone(ordinal: 2, label: 'Build and test code')
                sh('mvn verify')
            }
        }
    }
    post {
        always {
            deleteDir()
        }
        success {
            mail(to:"daniel.sundberg@oyabun.se",
                 subject:"Jenkins build ${currentBuild.fullDisplayName} succeeded.",
                 body: "Yay, we passed.")
        }
        failure {
            mail(to:"daniel.sundberg@oyabun.se",
                 subject:"Jenkins build ${currentBuild.fullDisplayName} failed.",
                 body: "Boo, we failed.")
        }
    }
}