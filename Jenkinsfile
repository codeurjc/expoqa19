node {
    elastest(tss: ['EUS'], surefireReportsPattern: '**/target/surefire-reports/TEST-*.xml', monitoring: true, project: 'ExpoQA19') {
        withKubeConfig([credentialsId: 'minikube_token', serverUrl: 'https://localhost:8443']) {
            try {
                stage("Preparation") { 
                    git(
                        url: 'https://github.com/codeurjc/expoqa19.git',
                        branch: "${BRANCH}"
                        )
                }
                stage("Create jar") {
                    sh "docker build . -t expoqa19/webapp2:v1"
                }
                stage("Start app") {
                    sh "./addSutPrefix.sh"
                    sh "kubectl create -f k8s/"
                }
                stage("Test") {
                    sh "mvn test"
                }
            
            } finally {
                sh 'kubectl delete -f k8s/'
                junit "target/*-reports/TEST-*.xml"
            }
        }
    }
}