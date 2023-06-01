def call(updateCommand) {
  sshagent(['STM-SSH-DEMO']) {
      sh "ssh -o StrictHostKeyChecking=no -l jenkins 147.172.178.45 ${updateCommand}"
  }
}