def isNewVersion() {
    def publisher = LastChanges.getLastChangesPublisher "PREVIOUS_REVISION", "SIDE", "LINE", true, true, "", "", "", "", ""
    publisher.publishLastChanges()
    def changes = publisher.getLastChanges()

    for (commit in changes.getCommits()) {
        for (change in commit.getChanges()) {
            println(change)
            //if (change.getPath().endsWith("package.json") && change.getDiff().contains("version")) {
                return true
            //}
        }
    }
    return false
}