def call(path, token, registry='https://npm.pkg.github.com/') {
  echo "$registry"
  dir("$path") {
	sh 'npm i'
	sh "npm set ${registry}:_authToken ${token}"
	sh "npm publish --access public --registry=${registry}"
  }
}