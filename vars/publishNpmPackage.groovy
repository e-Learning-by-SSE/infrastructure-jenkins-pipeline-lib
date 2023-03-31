def call(path, token, registry='//npm.pkg.github.com/') {
  echo "$registry"
  dir("$path") {
	sh 'npm i'
	sh 'rm -f ~/.npmrc'
	sh "echo $registry:_authToken=$NPM_PUBLSH_TOKEN >> ~/.npmrc"
	sh "npm publish --access public --registry=https://${registry}"
  }
}