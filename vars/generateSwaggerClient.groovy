def call(apiPath, version, groupId, artifactId, languages) {
  configFileProvider([configFile(fileId: 'maven-openapi-generator', variable: 'MAVEN_POM')]) {
	
    languages.each { lang ->
	  // Avoid target/ dir in the tmp jenkins dir
	  if (lang.startsWith('typescript')) {
		// New version, may be incompatible, not tested with all APIs
		sh "cp -v "pom_openapi.xml" ./generator-pom.xml"
      } else {
		// Deprecated consider update
		sh "cp -v $MAVEN_POM ./generator-pom.xml"
	  }
	
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
      
	  // Zip correct folder based on POM file that was used
	  if (lang.startsWith('typescript')) {
		sh "zip -r -q ${artifactId}_${lang}.zip target/generated-sources/openapi/"
      } else {
		sh "zip -r -q ${artifactId}_${lang}.zip target/generated-sources/swagger/"
	  }

      archiveArtifacts artifacts: "${artifactId}_${lang}.zip"
	  sh "rm ./generator-pom.xml" 
    }
  }
}