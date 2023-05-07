def call(String npmrcId, String npmArgs = '') {
    def versionList
    withNPM(npmrcConfig: npmrcId) {
        try {
          sh "npm publish ${npmArgs}"
        } catch (err) {
          echo "Publishing was not possible - does the version already exists in this registry? Error: ${err}"
        }
    }
}
