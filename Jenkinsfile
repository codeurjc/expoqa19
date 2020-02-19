logstash {
    node {
        try {
           stage("Preparation") { 
                git(
                    url: 'https://github.com/codeurjc/expoqa19.git',
                    branch: "demo2"
                )
           }
           stage("Create jar") {
               sh "docker-compose build"
           }
           stage("Start app") {
               sh "docker-compose -p demo2 up -d"
           }
           stage("Test") {
               sh "mvn test"
           }
        } finally {
            sh "docker-compose -p demo2 down"
            junit "target/*-reports/TEST-*.xml"
        }
    }
}
