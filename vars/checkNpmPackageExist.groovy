def call(groupName, pkgName, version, token, registry='//npm.pkg.github.com/') {
    sh 'rm -f ~/.npmrc'
	  sh "echo $groupName:registry=https:$registry >> ~/.npmrc"
		sh "echo $registry:_authToken=$token >> ~/.npmrc"
	
    script {
		def npmName = "$groupName/$pkgName"
	    // Do not abort if package does not exist
		def versionList = sh(returnStdout: true, script: "npm view $npmName versions --json 2>&1 && echo || echo")
		sh 'rm -f ~/.npmrc'
		
		if (versionList.contains("$version")) {
			return true
		} else {
		    sh "echo $version not found"
			return false
		}
    }
}