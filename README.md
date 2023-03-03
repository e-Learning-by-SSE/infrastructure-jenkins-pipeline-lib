This repository contains scripts to be used by the SSE Jenkins. 

## About 

Global Pipeline Libraries is a Jenkins feature that allows you to define and reuse custom Groovy libraries across multiple pipelines.

By setting up a global pipeline library, you can write reusable Groovy code and functions that can be shared across multiple Jenkins pipelines, allowing you to modularize your code and avoid duplication. You can define a global pipeline library as a Git repository, which Jenkins can use to retrieve and load the library.

## Jenkins Installation
Configure your Jenkins instance to allow the use of external shared libraries. This can be done by going to `Manage Jenkins > Configure System` and scrolling down to the `Global Pipeline Libraries` section. Here, you can configure the libraries that you want to use and set their default versions.

## Usage
To use any of the following scripts, you must include this library via the `@Library`-annotation in your pipeline scripts:

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

The _ after the library name is important, as it indicates that the default version of the library should be used. If you need to specify a specific version of the library, you can do so by passing the version as an argument to the @Library annotation.

## Available Scripts


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
