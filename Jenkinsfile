node {
  elastest(tss: ['EUS'], surefireReportsPattern: '**/target/surefire-reports/TEST-*.xml', monitoring: true, project: 'ExpoQA19') {
     try {
       stage("Preparation") {         
         git(
             url: 'https://github.com/codeurjc/expoqa19.git',
             branch: "demo4"
         )
         
       }
       stage("Create jar") {
         sh "docker-compose build"
       }
       stage("Start app") {
         sh "docker-compose -p ${env.ET_SUT_CONTAINER_NAME} up -d"
         sh "sleep 5"
       }
       stage("Test") {
         sh "mvn test" 
       }
     } catch(e){
        echo 'Err: ' + e.toString()
     } finally {
        sh "echo finally block"
        sh "docker-compose logs"
          
        sh "docker-compose logs > all-logs.txt"
        archive "all-logs.txt"
          
        sh "docker-compose logs web > web-logs.txt"
        archive "web-logs.txt"
          
        sh "docker-compose logs db > db-logs.txt"
        archive "db-logs.txt"
          
        sh "docker-compose -p ${env.ET_SUT_CONTAINER_NAME} down"
          
        junit "target/*-reports/TEST-*.xml"
     }
  }
}
