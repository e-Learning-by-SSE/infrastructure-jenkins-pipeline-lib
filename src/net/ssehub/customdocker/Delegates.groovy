package net.ssehub.customdocker

class Delegates {
    static def call(obj, Closure cl) {
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.delegate = obj
        cl.call()
    }
}