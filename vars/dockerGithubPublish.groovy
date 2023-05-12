def call(image, additionalTags = [:]) {
    if (!image.exists()) {
      error("Image ${target} must be build in order to publish it")
    }

    docker.withRegistry('https://ghcr.io', 'github-ssejenkins') {
      image.push() // target contains tag - push this too
      config.additionalTags.each{ tag -> 
        image.push("${tag}")
      }
    }
}