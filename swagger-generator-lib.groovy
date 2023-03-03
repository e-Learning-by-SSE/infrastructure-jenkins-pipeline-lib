def generateSwaggerClient(apiPath, version, groupId, artifactId, modelPackage, apiPackage, ...languages)
  def PACKAGE="${ARTIFACT_ID}"
  configFileProvider([configFile(fileId: 'maven-openapi-generator', variable: 'MAVEN_POM')]) {
    strings.each { languages ->
      sh "mvn -f $MAVEN_POM clean compile -Dspec_source= ${API_PATH} -Dversion=${VERSION} -Dlanguage=${LANG} -Dgroup_id=${GROUP_ID} -Dartifact_id=${ARTIFACT_ID} -Dpackage=${PACKAGE} -Dmodel=${MODEL_PACKAGE} -Dapi=${API_PACKAGE}"
    }
  }
  sh "zip -r -q ../../../${ARTIFACT_ID}_${LANG}.zip target/generated_sources/swagger/"
}
