def call(path, token, registry='https://npm.pkg.github.com/') {
  echo "$path"
  dir("$path") {
    sh 'pwd'
	sh 'npm i'
	sh 'npm set ${registry}:_authToken ${token}'
	sh 'npm publish --access public --registry=${registry}'
  }
}