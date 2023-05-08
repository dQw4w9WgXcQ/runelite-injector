package dev.dqw4w9wgxcq.cbarn

import dev.dqw4w9wgxcq.runeliteinjector.Transformer
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode

class Asdf : Transformer {
    override fun apply(clazz: ClassNode) {
        for (method in clazz.methods) {
            for (instruction in method.instructions) {
                if (instruction is MethodInsnNode) {
                    println(instruction.name)
                    println(instruction.owner)
                    println(instruction.desc)
                }
            }
        }
    }
}