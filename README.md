This repository contains scripts to be used by the SSE Jenkins

## Table of content
- [About](#about)
- [Usage](#usage)
  - [Static (Recommended)](#static-recommended)
    - [Installation](#installation)
  - [Dynamic Loading](#dynamic-loading)
- [Available Scripts](#available-scripts)
  - [Swagger Client Generation](#swagger-client-generation)
  - [Maven Version](#maven-version)
  - [Run SSH Command on Staging-Server](#run-ssh-command-on-staging-server)
  - [Publish Docker Images](#publish-docker-images)


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

### Static Loading (Recommended)
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
    lib('github.com/e-Learning-by-SSE/infrastructure-jenkins-pipeline-lib@main')
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

### Swagger Client Generation
```
generateSwaggerClient(apiPath: string, version: string, groupId: string, artifactId: string, languages: string array)
```
Description:

- Generates multiple clients via swagger-codegen
- Automatically archives all generated zips as artifact
- in case you generate clients **for the language 'java', the client is deployed** into our maven repo https://github.com/e-Learning-by-SSE/maven-packages
- in case you're using java with maven and want to generate clients, you must have run the "package" maven phase
- TODO available languages

Example: 
```groovy
generateSwaggerClient('target/openapi.json', version, 'net.ssehub', 'nm-facade-service', ['javascript', 'typescript-angular'])
```

### Maven Version

```
maven.getVersion()
```
Description: 

- returns the version of the project defined in a pom.xml
- must run alongside the pom.xml

Example: 
```groovy
script {
  dir(PATH_TO_POM) {
    echo maven.getVersion
  }
}
```
### JS PackageJson Tools

#### getVersion
This will return the version inside a package.json. Will throw a warning if the JSON is invalid but still tries to continue to extract the version. 

```groovy
dir (PATH_TO_JSON) {
  script {
    def version = packageJson.getVersion()
    echo version
  }
}
```

#### package.json Version Expression
This will check the contents of the last commit (the changes of all files the jenkins master receives from SCM) for a version change inside the package.json. Returns true if this is the case, so this can be used as expression. 

```groovy
stage('Deploy') {
  when {
    expression {
      // Check if the package.json file was changed to a new version
      return PackageJson.isNewVersion()
    }
  }
  steps {
    // Deploy stage code
  }
}
```


### Run SSH Command on Staging-Servers

#### Staging01
```
staging01ssh("echo ok")
```

This runs the command "echo ok" on the staging01 server in the default login shell. It will use the user "elscha.
In order to userequires a docker agent (it runs ssh-agent inside a docker image)

Example: 

```groovy
stage('Deploy') {
    steps {
        staging01ssh('/staging/update-compose-project.sh nm-competence-repository')
    }
}
```

#### Staging02
To run commands on the second staging server use this:

```
staging02ssh("echo ok")
```

Same as above, but runs commands with the "jenkins" user. It has the permission to use the docker-cli.

### Built and Publish Docker Images

```groovy
ssedocker {
  create {
    context 'directory/to/dockerfile/'
    target 'org/name:latest'
  }
  publish {
    tag '1.0.0'
    tag 'nightly'
  }
} 
```

Parameter: 
- create: builds a docker image 
  - context [optional]: sets the directory for the dockerfile. Default is the current directory '.' 
  - target: specifies the target to built
- publish: publishes a docker image to github with ssejenkins access permissions
  - imageName [optional]: defines the image to be published. Is necessary when no `create` was called in beforehand. e.g. 'ghcr.io/org/name:tag'
  - tag [optional]: defines an additional tag to be published (the default from the target is always published).


Example 2: Publishes a pre-built image:
```groovy
ssedocker {
  publish {
    imageName 'ghcr.io/org/name:latest'
  }
}
```
This publishes an image with the id 'ghcr.io/org/name:latest'. This image must be present on the system. 

Example 3: Built and publish an image
```groovy
ssedocker {
  create {
    target 'ghcr.io/org/name:latest'
  }
  publish {}
}
```
The Dockerfile must be locates inside the current workdir for this because we don't set a context. This will build an Image with the target 'ghcr.io/org/name:latest' and publishes it. 

## Helper Methods

#### ScriptOut
ScriptOut executes a given command and returns the stdOut (trimmed).

```groovy
script {
  def output = scriptOut('echo hi')
  assert output == hi
}
```
