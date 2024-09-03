def isNewVersion(Map config = [:]) {
    def since = config.since ?: 'PREVIOUS_REVISION'
    def publisher = LastChanges.getLastChangesPublisher since, "SIDE", "LINE", true, true, "", "", "", "", ""
    publisher.publishLastChanges()
    def changes = publisher.getLastChanges()


    for (commit in changes.getCommits()) {
        String gitDiff = commit.getChanges()
        def fileName = "package.json"
        def versionPattern = /(^|\n)(\-|\+)\s*"version"\s*:\s*"[^"]*"/

        if (gitDiff.contains("diff --git a/${fileName}")) {
            if (gitDiff.find(versionPattern)) {
                return true
            }
        }
    }
    return false
}

def getVersion() {
    def version
    try {
        version = scriptOut('jq -r \'.version\' package.json')
    } catch (err) {
        println(err)
        println('using grep instead')
        version = scriptOut('grep \'"version":\' package.json | sed -E \'s/.*"version": "([^"]*)".*/\1/\'')
    }
    return version
}

def getPkgName() {
    def name 
    try {
        name = scriptOut('jq -r \'.name\' package.json')
    } catch (err) {
        println(err)
        println('using grep instead')
        name = scriptOut('grep -m1 \'"name":\' package.json | sed -E \'s/.*"name": "([^"]*)".*/\1/\'')
    }
    return name
}
