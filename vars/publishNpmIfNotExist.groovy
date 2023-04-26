def call(groupName, pkgName, version, path, readOnlyToken, publishToken, registry='//npm.pkg.github.com/') {   
    if (!checkNpmPackageExist("$groupName", "$pkgName", "$version", "$readOnlyToken", "$registry")) {
        publishNpmPackage("$path", "$publishToken", "$registry")
    }
}