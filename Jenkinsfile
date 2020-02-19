node {
    try {
       stage("Preparation") { 
            git(
                url: 'https://github.com/codeurjc/expoqa19.git',
                branch: "demo1"
            )
       }
       stage("Create jar") {
           sh "docker-compose build"
       }
       stage("Start app") {
           sh "docker-compose -p demo1 up -d"
       }
       stage("Test") {
           sh "mvn test"
       } 
    } finally {
        sh "docker-compose logs > all-logs.txt"
        archive "all-logs.txt"
      
        sh "docker-compose logs web > web-logs.txt"
        archive "web-logs.txt"
      
        sh "docker-compose logs db > db-logs.txt"
        archive "db-logs.txt"

        sh "docker-compose -p demo1 down"
        junit "target/*-reports/TEST-*.xml"
    }
}
