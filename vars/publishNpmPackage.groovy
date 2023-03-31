def call(path, token, registry='//npm.pkg.github.com/') {
  dir("$path") {
	sh 'npm i'
	sh 'rm -f ~/.npmrc'
	sh "echo $registry:_authToken=$token >> ~/.npmrc"
	sh "npm publish --access public --registry=https:${registry}"
	sh 'rm -f ~/.npmrc'
  }
}