def call(dockerTarget, additionalTags) { 
  //LEGACY support
  dockerGithubPublish(target: dockerTarget, additionalTags: additionalTags)
}
