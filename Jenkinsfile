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
           sh "docker-compose up -d"
       }
       stage("Test") {
           sh "mvn test -Dsel.jup.recording=true"
       }
           
    } finally {
        step([$class: 'JUnitResultArchiver', testDataPublishers: [[$class: 'AttachmentPublisher']], testResults: '**/target/surefire-reports/TEST-*.xml'])
        sh "docker-compose logs"
          
        sh "docker-compose logs > all-logs.txt"
        archive "webapp2/all-logs.txt"
          
        sh "docker-compose logs web > web-logs.txt"
        archive "webapp2/web-logs.txt"
          
        sh "docker-compose logs db > db-logs.txt"
        archive "webapp2/db-logs.txt"
          
        sh "docker-compose down"
          
        //junit "webapp2/target/*-reports/TEST-*.xml"
    }
}
