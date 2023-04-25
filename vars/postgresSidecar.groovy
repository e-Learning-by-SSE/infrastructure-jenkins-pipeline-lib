def call(image, postgresUser, postgresPassword, postgresDb, postgresPort, Closure closure) {
  def dbImage = 'postgres:14.3-alpine'
  
  sidecar: {
    stage('Sidecar Postgres') {
      docker.image(dbImage).withRun("-e POSTGRES_USER=${postgresUser} -e POSTGRES_PASSWORD=${postgresPassword} -e POSTGRES_DB=${postgresDb} -p ${postgresPort}:5432") { c ->
        docker.image(dbImage).inside("--link ${c.id}:db") {
          sh "sleep 20"
        }
        docker.image(image).inside("--link ${c.id}:db") {
          closure()
        }
      }
    }
  }
}
