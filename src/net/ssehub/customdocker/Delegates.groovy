package net.ssehub.customdocker

class Delegates {
    static def call(obj, Closure cl) {
        def code = cl.rehydrate(obj, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }
}