def isNewVersion() {
    def publisher = LastChanges.getLastChangesPublisher "PREVIOUS_REVISION", "SIDE", "LINE", true, true, "", "", "", "", ""
    publisher.publishLastChanges()
    def changes = publisher.getLastChanges()

    for (commit in changes.getCommits()) {
        println(commit.getChanges)
        for (change in commit.getChanges()) {
            echo(change.getEscapedDiff)
            //if (change.getPath().endsWith("package.json") && change.getDiff().contains("version")) {
                return true
            //}
        }
    }
    return false
}