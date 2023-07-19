package dqw4w9wgxcq.runeliteinjector

import org.objectweb.asm.tree.ClassNode

interface Injector {
    fun inject(clazz: ClassNode): Boolean

    val name: String
        get() = this::class.java.simpleName
}