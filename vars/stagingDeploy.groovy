def call(updateCommand) {
  sshagent(['STM-SSH-DEMO']) {
      sh "ssh -o StrictHostKeyChecking=no -l elscha staging.sse.uni-hildesheim.de ${updateCommand}"
  }
}