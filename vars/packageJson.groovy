def isNewVersion() {
    def publisher = LastChanges.getLastChangesPublisher "PREVIOUS_REVISION", "SIDE", "LINE", true, true, "", "", "", "", ""
    publisher.publishLastChanges()
    def changes = publisher.getLastChanges()

    for (commit in changes.getCommits()) {
        println(commit.getChanges())
        //if (change.getPath().endsWith("package.json") && change.getDiff().contains("version")) {
            return true
        //}
        }
    }
    return false
}