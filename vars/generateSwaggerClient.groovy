def call(apiPath, version, groupId, artifactId, languages) {
  configFileProvider([configFile(fileId: 'Maven-Open-API-Generator-New', variable: 'MAVEN_POM')]) {
	
	sh "cp -v $MAVEN_POM ./generator-pom.xml"
    languages.each { lang ->
	
      if (lang == 'java') {
        mavenPhase = 'compile install deploy'
        packageName = "${groupId}.${artifactId}"
        modelPackage = packageName + ".model"
        apiPackage = packageName + ".api"
      } else {
        mavenPhase = 'compile'
        packageName = artifactId
        modelPackage = 'model'
        apiPackage = 'api'
      }
      sh "mvn -f generator-pom.xml ${mavenPhase} -Dspec_source=${apiPath} -Dversion=${version} -Dlanguage=${lang} -Dgroup_id=${groupId} -Dartifact_id=${artifactId} -Dpackage=${packageName} -Dmodel=${modelPackage} -Dapi=${apiPackage}"
      
	  sh "zip -r -q ${artifactId}_${lang}.zip target/generated-sources/openapi/"
      archiveArtifacts artifacts: "${artifactId}_${lang}.zip"
    }
	sh "rm ./generator-pom.xml" 
  }
}