def isNewVersion(Map config = [:]) {
    def since = config.since ?: 'LAST_TAG'
    def publisher = LastChanges.getLastChangesPublisher since, "SIDE", "LINE", true, true, "", "", "", "", ""
    publisher.publishLastChanges()
    def changes = publisher.getLastChanges()


    for (commit in changes.getCommits()) {
        String gitDiff = commit.getChanges()
        def fileName = "package.json"
        def versionPattern = /(^|\n)(\-|\+)\s*"version"\s*:\s*"[^"]*"/

        if (gitDiff.contains("diff --git a/${fileName}")) {
            if (gitDiff.find(versionPattern)) {
                println "The 'version' line in ${fileName} was changed"
                return true
            }
        }
    }
    return false
}

def getVersion() {
    def version
    try {
        version = sh(script: 'jq -r \'.version\' package.json', returnStdout: true).trim()
    } catch (err) {
        version = sh(script: 'grep -oP \'(?<="version": ")[^"]*\' package.json', returnStdout: true).trim()
    }
    return version
}
