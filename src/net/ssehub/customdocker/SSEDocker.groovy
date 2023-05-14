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

    def build(Closure buildClosure) {
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
        publicConfig?.execute()
    }

    class BuildConfig {
        private String dockerTarget
        private String dockerfilePath = '.'

        void target(String tg) {
            this.dockerTarget = tg
        }

        void dockerfile(String path) {
            this.dockerfilePath = path
        }

        Image execute() {
            if (dockerTarget == null || dockerFilePath == null) {
                error("You must specify a target to build")
            }
            return docker.build(dockerTarget, dockerfilePath)
        }
    }

    class PublishConfig {
        Image image
        List<String> additionalTags = []

        void additionalTag(String tag) {
            additionalTags << tag
        }

        void imageName(String name) {
            if (image != null) {
                error("Currently It is not possible to build an image and publish a different one")
            }
            this.image = docker.image(name)
        }

        String execute() {
            if (this.image == null) {
                error('You must specify an imageName or build an image in beforehand')
            }
            docker.withRegistry('https://ghcr.io', 'github-ssejenkins') {
                // target contains tag - push this too
                this.image.push()
                additionalTags.each { tag ->
                    this.image.push("${tag}")
                }
            }
        }
    }
}
