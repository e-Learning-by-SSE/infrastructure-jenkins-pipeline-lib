package net.ssehub.customdocker

class Delegates {
    static def call(obj, source, Closure cl) {
        def code = cl.rehydrate(obj, source, source)
        code.resolveStrategy = Closure.DELEGATE_FIRST
        code()
    }
}