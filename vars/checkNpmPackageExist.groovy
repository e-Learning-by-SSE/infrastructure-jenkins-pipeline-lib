def call(npmName, version, token, registry='//npm.pkg.github.com/') {
    sh 'rm -f ~/.npmrc'
    sh "echo $registry:_authToken=$token >> ~/.npmrc"
	
    script {
	    def versionList = sh(returnStdout: true, script: "npm view $npmName versions --json")
		sh 'rm -f ~/.npmrc'
		
		if (versionList.contains($version)) {
			return true
		} else {
			return false
		}
    }
}