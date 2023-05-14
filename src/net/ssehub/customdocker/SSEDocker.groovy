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
        def image = null
        if (buildConfig != null) {
            image = buildConfig.build()
        }
        if (publishConfig != null) {
            publishConfig.publish(image)
        }
    }

    @Canonical
    class BuildConfig {
        private String target
        private String dockerfilePath = '.'

        void target(String target) {
            this.dockerTarget = target
        }

        void dockerfile(String path) {
            this.dockerfilePath = path
        }

        Image build() {
            return docker.build(target, dockerfile)
        }
    }

    class PublishConfig {
        Image image
        List<String> additionalTags = []

        void additionalTag(String tag) {
            additionalTags << tag
        }

        void imageName(String name) {
            this.image = docker.image(name)
        }

        String publish(Image image = null) {
            if (image == null && this.image == null) {
                error('You must specify an imageName or build an image in beforehand')
            }
            docker.withRegistry('https://ghcr.io', 'github-ssejenkins') {
                if (image == null) {
                    image = this.image
                }
                // target contains tag - push this too
                image.push()
                additionalTags.each { tag ->
                    image.push("${tag}")
                }
            }
        }
    }
}
