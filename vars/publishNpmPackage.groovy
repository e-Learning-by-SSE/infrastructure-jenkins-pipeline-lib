def call(path, token, registry='https://npm.pkg.github.com/') {
  sh '''
    echo "$path"
    echo "${path}"
	cd ${path}
	pwd
	npm i
	npm set ${registry}:_authToken ${token}
	npm publish --access public --registry=${registry}
	cd -
  '''
}