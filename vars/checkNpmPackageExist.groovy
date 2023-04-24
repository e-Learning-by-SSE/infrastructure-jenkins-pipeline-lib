def call(groupName, pkgName, version, token, registry='//npm.pkg.github.com/') {
    sh 'rm -f ~/.npmrc'
	sh "echo $groupName:registry=https:$registry >> ~/.npmrc"
    sh "echo $registry:_authToken=$token >> ~/.npmrc"
	
    script {
		def npmName = "$groupName/$pkgName"
	    def versionList = sh(returnStdout: true, script: "npm view $npmName versions --json")
		sh 'rm -f ~/.npmrc'
		
		if (versionList.contains($version)) {
			return true
		} else {
			return false
		}
    }
}