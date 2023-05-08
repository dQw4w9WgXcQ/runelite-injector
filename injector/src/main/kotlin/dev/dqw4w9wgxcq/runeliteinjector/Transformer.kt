package dev.dqw4w9wgxcq.runeliteinjector

import org.objectweb.asm.tree.ClassNode

interface Transformer {
    fun apply(clazz: ClassNode)
    val name: String
        get() = this::class.java.simpleName
}