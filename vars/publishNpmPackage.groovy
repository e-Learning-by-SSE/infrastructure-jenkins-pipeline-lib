def call(path, token, registry='https://npm.pkg.github.com/') {
  sh '''
    cd ${path}
	npm i
	npm set ${registry}:_authToken ${token}
	npm publish --access public --registry=${registry}
	cd -
  '''
}