package net.ssehub.customdocker

class Delegates {
    static def call(obj, Closure cl) {
        cl.resolveStrategy = Closure.DELEGATE_ONLY
        cl.delegate = obj
        cl.call()
    }
}