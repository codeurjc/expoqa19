logstash {
	node {
	    try {
            withEnv(['ET_SUT_HOST=MYDNS_ORIP']) {
    	    	stage("Preparation") { 
    				git(
    					url: 'https://github.com/codeurjc/expoqa19.git',
    					branch: "demo3"
    				)
    	    	}
    	    	stage("Create jar") {
    				sh "docker-compose build"
    	    	}
    	    	stage("Start app") {
    				sh "docker-compose up -d"
    	    	}
    	    	stage("Test") {
    				sh "mvn test -Dsel.jup.recording=true -Dsel.jup.output.folder=surefire-reports"
    	    	} 
            }
	    } finally {
	    	sh "docker-compose down"
			
			step([
				$class: 'JUnitResultArchiver', 
				testDataPublishers: [[$class: 'AttachmentPublisher']], 
				testResults: '**/target/surefire-reports/TEST-*.xml'])
	    }
	}
}
