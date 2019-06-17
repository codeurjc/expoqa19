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
       }
       stage("Test") {
         sh "mvn test" 
       }
     } catch(e){
        echo 'Err: ' + e.toString()
     } finally {          
        sh "docker-compose -p ${env.ET_SUT_CONTAINER_NAME} down"
        junit "target/*-reports/TEST-*.xml"
     }
  }
}
