def call(String npmrcId, String npmArgs = '') {
    def versionList
    withNPM(npmrcConfig: npmrcId) {
        if (isAlreadyPublished()) {
          println("Package ${packageJson.getPkgName()} with version ${packageJson.getVersion()} was already published.")
        } else {
            sh "npm publish ${npmArgs}"
        }
    }
}

// package.json must be in workdir for this method
def isAlreadyPublished() {
    return hasRemoteNpmVersion(pkgName: packageJson.getPkgName(), targetVersion: packageJson.getVersion())
}

def hasRemoteNpmVersion(Map config = [:]) {
    def versionList = scriptOut("npm view ${config.pkgName} versions --json 2>&1 && echo || echo")
    if (versionList.contains(config.targetVersion)) {
        return true
    }
    return false
}