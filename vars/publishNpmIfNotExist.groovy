def call(groupName, pkgName, version, path, readOnlyToken, publishToken, registry='//npm.pkg.github.com/') {   
    withCredentials([
		string(credentialsId: publishToken, variable: 'Auth'),
		string(credentialsId: readOnlyToken, variable: 'ReadOnly')
	]) {
		if (!checkNpmPackageExist(groupName, pkgName, version, ReadOnly, registry)) {
			publishNpmPackage(path, Auth, registry)
		}
	}
}
