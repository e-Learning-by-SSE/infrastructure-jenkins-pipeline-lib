def call(Map config = [:]) { 
  image = docker.image("${config.target}")
  docker.withRegistry('https://ghcr.io', 'github-ssejenkins') {
    if (config.additionalTags != null) {
      config.additionalTags.each{ tag -> 
        image.push("${tag}")
      }
    }
    image.push() // tagged version from name
  }
}
