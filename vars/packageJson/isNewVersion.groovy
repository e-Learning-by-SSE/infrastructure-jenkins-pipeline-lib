def call() {
    def changeset = currentBuild.changeSets.getChanges()[0]
    def packageJsonChange = changeset.getAffectedFiles().find { it.getPath().endsWith('package.json') }
    if (packageJsonChange) {
        def diff = packageJsonChange.getDiff().getContent()
        return diff.contains('"version":')
    } else {
        return false
    }
}