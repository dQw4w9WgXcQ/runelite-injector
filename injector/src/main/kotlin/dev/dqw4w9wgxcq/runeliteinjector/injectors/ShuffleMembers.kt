package dev.dqw4w9wgxcq.runeliteinjector.injectors

import dev.dqw4w9wgxcq.runeliteinjector.Injector
import org.objectweb.asm.tree.ClassNode

class ShuffleMembers : Injector {
    override fun apply(clazz: ClassNode) {
        clazz.methods.shuffle()
        clazz.fields.shuffle()
    }
}