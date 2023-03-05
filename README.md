This repository contains scripts to be used by the SSE Jenkins. 

## About 

Global Pipeline Libraries is a Jenkins feature that allows you to define and reuse custom Groovy libraries across multiple pipelines.

By setting up a global pipeline library, you can write reusable Groovy code and functions that can be shared across multiple Jenkins pipelines, allowing you to modularize your code and avoid duplication. You can define a global pipeline library as a Git repository, which Jenkins can use to retrieve and load the library.


**SecurityNote:**
Currently this library also invokes steps like `sh` and `git` which can be a security issue. You must enable this invocation in the security settings. 

References:
> https://www.jenkins.io/doc/book/pipeline/shared-libraries/

## Usage
There are two common methods to load a shared library

1. The statis more robust approach where the library must be configured globally in jenkins
2. The dynamic approach where the library is loaded and evaluated during pipeline execution - you can load libraries directly from git here

### Static (Recommended)
Declarative Example:
```groovy
@Library('web-service-helper-lib') _
pipeline {
  stage('Deploy') {
      steps {
          stagingDeploy('/restart.sh')
      }
  }
}
```
The _ after the library name is important, as it indicates that the default version of the library should be used. If you need to specify a specific version of the library, you can do so by passing the version as an argument to the @Library annotation.

**Installation:**
Configure your Jenkins instance to allow the use of external shared libraries. This can be done by going to `Manage Jenkins > Configure System` and scrolling down to the `Global Pipeline Libraries` section. Here, you can configure the libraries that you want to use and set their default versions.
This is only necessary once.

**At 05/3/2023 the library is configured globally inside jenkins under the name 'web-service-helper-lib'**

### Dynamic Loading

If you want to load libraries dynamically, you can use the `libraries` declaritive. This *can* lead to strange behaviour because the library is not compiled and checked before the pipleine execution starts. 
Dynamic example, where jenkins loads it directly from github:

```groovy
pipeline {
  agent any
  libraries {
    lib('github.com/e-Learning-by-SSE/nm-jenkins-groovy-helper-lib@main')
  }
}
```

Note: You can also use this approach with the global pipeline name if you configured it in beforehand:
```
pipeline {
  agent any
  libraries {
    lib('web-service-helper-lib')
  }
}
```

## Available Scripts


The follwing groovy scripts are available:

- **generateSwaggerClient(apiPath: string, version: string, groupId: string, artifactId: string, languages: string array)** which generates multiple clients and archive them as zip. Example usage:

```groovy
generateSwaggerClient('target/openapi.json', version, 'net.ssehub', 'nm-facade-service', ['javascript', 'typescript-angular'])
```

Notes:
- TODO available languages
- in case you generate clients **for the language 'java', the client is deployed** into our maven repo https://github.com/e-Learning-by-SSE/maven-packages
- in case you're using java with maven and want to generate clients, you must have run the "package" maven phase

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
