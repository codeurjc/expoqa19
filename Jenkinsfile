node {
    elastest(tss: ['EUS'], surefireReportsPattern: '**/target/surefire-reports/TEST-*.xml', monitoring: true, project: 'ExpoQA19') {
        withKubeConfig([credentialsId: 'k8s-api-token', serverUrl: '${K8S_URL}']) {
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
                    checkPodStatus(1, "${ET_SUT_CONTAINER_NAME}-web")
                }
                stage("Test") {
                    def sutIp = getPodIp('web')
                    waitForService(5, "http://" + sutIp + ":8080")
                    withEnv(['ET_SUT_HOST=' + sutIp]) {
                        echo "Running test"
                        sh "mvn test"
                    }
                }
            
            } finally {
                sh 'kubectl delete -f k8s/'
                junit "target/*-reports/TEST-*.xml"
            }
        }
    }
}

def getPodIp(podName) {
    echo "Retrive pod ip"
    def podIp = sh (
        script: "kubectl get pod -o wide | grep " + podName + " | awk '{print \$6}'",
        returnStdout: true
    ).split( '\n' ).first()

    echo podName+" IP = " + podIp;
    return podIp;
}

def waitForService(time, serviceUrl) {
    timeout(time) {
        waitUntil {
            echo "Waiting for the service " + serviceUrl + " is ready"
            script {
                def r = sh script: 'wget -q ' + serviceUrl + ' -O /dev/null', returnStatus: true
                return (r == 0);
           }
        }
    }
}

def checkPodStatus(time, podName) {
    timeout(time) {
        waitUntil {
            echo "Waiting for the pod " + podName + " is created"
            script {
                def r = sh script: 'kubectl get pods -l io.elastest.service=' + podName.replace('_','-') + " | awk '{print \$3}'", returnStdout: true
                def status = r.split("\n").last()
                echo "Pod ${podName} current status: " + status
                return ( status == 'Running');
           }
        }
    }
}