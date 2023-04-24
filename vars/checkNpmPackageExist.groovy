def call(package, version, token, registry='//npm.pkg.github.com/') {
    sh 'rm -f ~/.npmrc'
    sh "echo $registry:_authToken=$token >> ~/.npmrc"
	
    script {
	    def versionList = sh(returnStdout: true, script: "npm view $package versions --json"
		sh 'rm -f ~/.npmrc'
		
		if (versionList.contains($version)) {
			return true
		} else {
			return false
		}
    }
}