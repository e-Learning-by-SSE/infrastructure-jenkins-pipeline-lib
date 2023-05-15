package net.ssehub.customdocker

import org.jenkinsci.plugins.docker.workflow.Docker

class PostgresTestImage {
    String postgresImage
    String postgresUser
    String postgresPassword
    String postgresDb
    String postgresExposedPort // nullable
    Docker docker
    Closure initExecution // nullable
    
    Docker.Container psql

    public PostgresTestImage(Docker docker, String postgresImage, String postgresUser, String postgresPassword, String postgresDb, String postgresExposedPort, Closure initExecution = null) {
        this.postgresImage = postgresImage
        this.postgresUser = postgresUser
        this.postgresPassword = postgresPassword
        this.postgresDb = postgresDb
        this.postgresExposedPort = postgresExposedPort
        this.docker = docker
        this.initExecution = initExecution
    }
    
    public void run(Closure<Docker.Container> closure) {
        docker.image(postgresImage).withRun(getDockerArguments()) { c ->
            closure.call(c)
        }
    }

    public void insideSidecar(String dbImage, String dockerArgs = '', Closure sidecarExecution) {
        run { c ->
            docker.image(postgresImage).inside("--link ${c.id}:db") {
                if (initExecution != null) {
                    this.initExecution()
                }
            }
            docker.image(dbImage).inside("--link ${c.id}:db ${dockerArgs}") {
                sidecarExecution()
            }
        }
    }

    private String getDockerArguments() {
        String dockerName = "jenkins_testing_psql_" + generateRandomNumber(10, 100)
        String args = "--name ${dockerName} -e POSTGRES_USER=${postgresUser} -e POSTGRES_PASSWORD=${postgresPassword} -e POSTGRES_DB=${postgresDb}"   
        if (postgresExposedPort != null) {
            args += " -p ${postgresExposedPort}:5432"
        }
        return args
    }

    public void start() {

    }

    public void stop() {

    }

    private int generateRandomNumber(int min, int max) {
        return (int)(Math.random() * ((max - min) + 1)) + min
    }
}
