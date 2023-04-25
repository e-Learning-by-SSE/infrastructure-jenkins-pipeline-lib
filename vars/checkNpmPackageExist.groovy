def call(groupName, pkgName, version, token, registry='//npm.pkg.github.com/') {
    sh "echo $groupName:registry=https:$registry > ~/.npmrc"
    sh "echo $registry:_authToken=$token >> ~/.npmrc"

    npmName = "$groupName/$pkgName"
    // Do not abort if package does not exist
    versionList = sh(returnStdout: true, script: "npm view $npmName versions --json 2>&1 && echo || echo")
    sh 'rm -f ~/.npmrc'
    return versionList.contains("$version") ?: (sh "echo $version not found"; false)
}
