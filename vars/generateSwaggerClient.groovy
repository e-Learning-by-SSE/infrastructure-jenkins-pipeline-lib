def call(apiPath, version, groupId, artifactId, languages) {
  def packageName = artifactId
  def modelPackage = packageName + ".model"
  def apiPackage = packageName + ".api"
  configFileProvider([configFile(fileId: 'maven-openapi-generator', variable: 'MAVEN_POM')]) {
    languages.each { lang ->
      sh "cp -v $MAVEN_POM ./generator-pom.xml" // avoid target/ dir in the tmp jenkins dir
      sh "mvn -f generator-pom.xml compile -Dbasedir=. -Dspec_source=${apiPath} -Dversion=${version} -Dlanguage=${lang} -Dgroup_id=${groupId} -Dartifact_id=${artifactId} -Dpackage=${packageName} -Dmodel=${modelPackage} -Dapi=${apiPackage}"
      sh "zip -r -q ../../../${artifactId}_${lang}.zip target/generated-sources/swagger/"   
      sh "rm ./generator-pom.xml"
   }
  }
}
