node {
    try {
       stage("Preparation") { 
           git 'https://github.com/codeurjc/expoqa19.git'
       }
       stage("Create jar") {
           sh "cd webapp2; docker-compose build"
       }
       stage("Start app") {
           sh "cd webapp2; docker-compose up -d"
       }
       stage("Test") {
           sh "cd webapp2; mvn test"
       } 
    } catch(e){
        echo 'Err: ' + e.toString()
    } finally {
        sh "cd webapp2; docker-compose logs"
          
        sh "cd webapp2; docker-compose logs > all-logs.txt"
        archive "webapp2/all-logs.txt"
          
        sh "cd webapp2; docker-compose logs web > web-logs.txt"
        archive "webapp2/web-logs.txt"
          
        sh "cd webapp2; docker-compose logs db > db-logs.txt"
        archive "webapp2/db-logs.txt"
          
        sh "cd webapp2; docker-compose down"
          
        junit "webapp2/target/*-reports/TEST-*.xml"
    }
}