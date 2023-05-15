package dev.dqw4w9wgxcq.runeliteinjector

import org.objectweb.asm.tree.ClassNode

interface Injector {
    fun apply(clazz: ClassNode)
    val name: String
        get() = this::class.java.simpleName
}