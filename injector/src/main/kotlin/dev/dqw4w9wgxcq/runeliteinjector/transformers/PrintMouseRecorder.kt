package dev.dqw4w9wgxcq.runeliteinjector.transformers

import dev.dqw4w9wgxcq.runeliteinjector.Transformer
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.MethodInsnNode

class PrintMouseRecorder : Transformer {
    private val className = "dl"
    private val methodName = "run"
    private val rl10 = "rl10"

    override fun apply(clazz: ClassNode) {
        if (clazz.name != className) return

        val method = clazz.methods.find { it.name == methodName }
            ?: throw Exception("didnt find run method for MouseRecorder")

//        for (instruction in method.instructions) {
//            println(instruction.toString2())
//        }

        for (instruction in method.instructions) {
            if (instruction.opcode == Opcodes.IASTORE) {
                println("inserting before IASTORE")
                val addInsns = InsnList()
                addInsns.add(InsnNode(Opcodes.DUP))
                addInsns.add(MethodInsnNode(Opcodes.INVOKESTATIC, rl10, "print", "(I)V", false))
                method.instructions.insertBefore(instruction, addInsns)
            }
        }

        for (instruction in method.instructions) {
            if (instruction.opcode == Opcodes.LASTORE) {
                println("inserting before LASTORE")
                val addInsns = InsnList()
                addInsns.add(InsnNode(Opcodes.DUP2))
                addInsns.add(MethodInsnNode(Opcodes.INVOKESTATIC, rl10, "print", "(J)V", false))
                method.instructions.insertBefore(instruction, addInsns)
            }
        }
    }
}