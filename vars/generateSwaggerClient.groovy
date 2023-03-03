def generateSwaggerClient(apiPath, version, groupId, artifactId, ...languages)
  def package=artifactId
  def modelPackage=PACKAGE + ".model"
  def apiPackage=PACKAGE + ".api"
  configFileProvider([configFile(fileId: 'maven-openapi-generator', variable: 'MAVEN_POM')]) {
    strings.each { lang ->
      sh "mvn -f $MAVEN_POM clean compile -Dspec_source= ${apiPath} -Dversion=${version} -Dlanguage=${lang} -Dgroup_id=${groupId} -Dartifact_id=${artifactId} -Dpackage=${package} -Dmodel=${modelPackage} -Dapi=${apiPackage}"
    }
  }
  sh "zip -r -q ../../../${ARTIFACT_ID}_${LANG}.zip target/generated_sources/swagger/"
}
