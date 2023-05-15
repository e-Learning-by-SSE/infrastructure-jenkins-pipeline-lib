import net.ssehub.customdocker.PostgresTestImage

def call(Map args, Closure toRunWith = null) {
   Map defaultArgs = [
    dbImage: 'postgres:latest',
    dbUser: 'myuser',
    dbPassword: 'mypassword',
    dbName: 'mydatabase',
    dbExposedPort: '5432'
  ]
  args = defaultArgs << args
  //args = defaultArgs.withDefault { key -> args[key] }
  def psql = new PostgresTestImage(
    docker,
    args.dbImage,
    args.dbUser,
    args.dbPassword,
    args.dbName,
    args.dbExposedPort,
    {
      sh "until PGPASSWORD=${args.dbPassword} psql -h db -p 5432 -U ${args.dbUser} -d ${args.dbName} -c \"SELECT version();\"; do sleep 5; done"
    }
  )
  if (toRunWith != null) {
     psql.run { c ->
        toRunWith()
     }
  } else {
    return psql
  }     
  return psql
}
