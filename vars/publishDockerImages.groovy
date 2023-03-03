def call(dockerTarget, additionalTags) { 
  image = docker.image("${dockerTarget}")
  docker.withRegistry('https://ghcr.io', 'github-ssejenkins') {
    additionalTags.each{ tag -> 
      image.push("${tag}")
    }
    image.push() // tagged version from name
  }
}
