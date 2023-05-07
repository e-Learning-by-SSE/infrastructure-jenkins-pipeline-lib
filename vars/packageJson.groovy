def isNewVersion() {
    def changeset = currentBuild.changeSets
    def packageJsonChange = changeset.getAffectedFiles().find { it.getPath().endsWith('package.json') }
    if (packageJsonChange) {
        def diff = packageJsonChange.getDiff().getContent()
        return diff.contains('"version":')
    } else {
        return false
    }
}