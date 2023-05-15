package net.ssehub.customdocker

import org.jenkinsci.plugins.docker.workflow.Docker
import org.jenkinsci.plugins.docker.workflow.Docker.Image
import groovy.transform.Canonical

class SSEDocker {
    def buildConfig
    def publishConfig
    Docker docker

    SSEDocker(Docker docker) {
        this.docker = docker
    }

    def buildImage(Closure buildClosure) {
        this.buildConfig = new BuildConfig()
        Delegates.call(buildConfig, buildClosure)
    }

    def publish(Closure publishClosure) {
        this.publishConfig = new PublishConfig()
        Delegates.call(publishConfig, publishClosure)
    }

    def execute() {
        if (buildConfig != null) {
            def image = null
            image = buildConfig.execute()
            publishConfig?.setImage(image)
        }
        publishConfig?.execute()
    }

    def withRegistry(Closure cl) {
        docker.withRegistry('https://ghcr.io', 'github-ssejenkins') {
            cl()
        }
    }

    class BuildConfig {
        private String dockerTarget
        private String dockerfilePath = '.'

        void target(String tg) {
            this.dockerTarget = tg
        }

        void filepath(String path) {
            this.dockerfilePath = path
        }

        Image execute() {
            if (dockerTarget == null || dockerfilePath == null) {
                throw new Exception("You must specify a target to build")
            }
            return docker.build(dockerTarget, dockerfilePath)
        }
    }

    class PublishConfig {
        Image image
        List<String> additionalTags = []

        void tag(String tag) {
            additionalTags << tag
        }

        void imageName(String name) {
            if (image != null) {
                throw new Exception("Currently It is not possible to build an image and publish a different one")
            }
            this.image = docker.image(name)
        }

        String execute() {
            if (this.image == null) {
                throw new Exception('You must specify an imageName or build an image in beforehand')
            }
            withRegistry {
                // target contains tag - push this too
                this.image.push()
                additionalTags.each { tag ->
                    this.image.push("${tag}")
                }
            }
        }
    }
}
