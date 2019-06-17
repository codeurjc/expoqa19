node {
    try {
       stage("Preparation") { 
            git(
                url: 'https://github.com/codeurjc/expoqa19.git',
                branch: "demo0"
            )
       }
       stage("Create jar") {
           sh "docker-compose build"
       }
       stage("Start app") {
           sh "docker-compose up -d"
       }
       stage("Test") {
           sh "mvn test"
       } 
    } finally {
        sh "cd docker-compose down"
        junit "target/*-reports/TEST-*.xml"
    }
}
