package dev.dqw4w9wgxcq.runeliteinjector.transformers

import dev.dqw4w9wgxcq.runeliteinjector.Transformer
import org.objectweb.asm.tree.ClassNode

class ShuffleMembers : Transformer {
    override fun apply(clazz: ClassNode) {
        // Randomize method order
        clazz.methods.shuffle()

        // Randomize field order
        clazz.fields.shuffle()
    }
}