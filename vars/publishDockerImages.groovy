def call(dockerTarget, additionalTags) { 
  println('DEPRECATION NOTICE: please use dockerGithubPublish DSL in the future')
  dockerGithubPublish(target: dockerTarget, additionalTags: additionalTags)
}
