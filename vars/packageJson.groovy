def isNewVersion() {
    def publisher = LastChanges.getLastChangesPublisher "PREVIOUS_REVISION", "SIDE", "LINE", true, true, "", "", "", "", ""
    publisher.publishLastChanges()
    def changes = publisher.getLastChanges()

    for (commit in changes.getCommits()) {
        String gitDiff = commit.getChanges()
        def fileName = "package.json"
        def versionPattern = /(^|\n)\s*"version"\s*:\s*"[^"]*"/

        if (gitDiff.contains("diff --git a/${fileName}")) {
            if (gitDiff.find(versionPattern)) {
                println "The 'version' line in ${fileName} was changed"
                return true
            }
        }
    }
    return false
}