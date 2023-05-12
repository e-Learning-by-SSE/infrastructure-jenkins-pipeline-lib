package net.ssehub.customdocker

class Delegates {
    static def call(obj, Closure cl) {
        def code = cl.rehydrate(obj, this, this)
        code.resolveStrategy = Closure.OWNER_FIRST
        code()
    }
}