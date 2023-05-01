def call(String groupName, String pkgName, String version, String npmrcId) {
    def versionList
    withNPM(npmrcConfig: npmrcId) {
        String npmName = "${groupName}/${pkgName}"
        // Do not abort if package does not exist
        versionList = sh(returnStdout: true, script: "npm view ${npmName} versions --json 2>&1 && echo || echo")	
		if (!versionList.contains(version) ?: false) {
			sh "npm publish"
		}
    }
}
