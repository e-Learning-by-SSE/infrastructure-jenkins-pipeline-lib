This repository contains shared libraries for the jenkins.

## Jenkins Installation
Configure your Jenkins instance to allow the use of external shared libraries. This can be done by going to `Manage Jenkins > Configure System` and scrolling down to the `Global Pipeline Libraries` section. Here, you can configure the libraries that you want to use and set their default versions.

## Available Scripts

To use any of the following scripts, you must include this library in your pipeline scripts:

Declarative Example:
```groovy
@Library('web-service-helper-lib') _
pipeline {
  agent { label 'maven' }
  // ....
  stage('Deploy') {
      steps {
          stagingDeploy('/restart.sh')
      }
  }
}

```

The follwing groovy scripts are available:

- **generateSwaggerClient(apiPath: string, version: string, groupId: string, artifactId: string, languages: string array)** which generates multiple clients and archive them as zip. Example usage:

```groovy
generateSwaggerClient('target/openapi.json', version, 'net.ssehub', 'nm-facade-service', ['javascript', 'typescript-angular'])
```

- **getMvnProjectVersion()** : returns the version of the project defined in a pom.xml

```groovy
script {
  version = getMvnProjectVersion()
}
```

- **stagingDeploy(updateCommand: string)**: runs an update script on the staging server via ssh

```groovy
stage('Deploy') {
    steps {
        stagingDeploy('/staging/update-compose-project.sh nm-competence-repository')
    }
}
```

- **publisDockerImages(dockerTarget: string, additionalTags: string array)**: Publishes a pre-built docker image (first parameter) with out jenkins credentials. The second argument takes an array of additional tags

```groovy
stage('Publish Docker') {
    steps {
        publishDockerImages("${DOCKER_TARGET}", ['1.0.0', 'unstable'])
    }
}
```