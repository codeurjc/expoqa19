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
                stage("Add SUT prefix"){
                    sh "./addSutPrefix.sh"                    
                }
                stage("Create jar") {
                    sh "docker build . -t expoqa19/webapp2:v1"
                }
                stage("Start app") {
                    sh "cd k8s; kubectl create -f ."
                }
                stage("Test") {
                    sh "mvn test"
                }
            } catch(e){
                echo 'Err: ' + e.toString()
            } finally {
                sh 'cd k8s; kubectl delete -f .'
                junit "target/*-reports/TEST-*.xml"
            }
        }
    }
}