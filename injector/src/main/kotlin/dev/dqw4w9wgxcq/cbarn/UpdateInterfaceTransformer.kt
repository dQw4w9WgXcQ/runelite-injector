package dev.dqw4w9wgxcq.cbarn

import dev.dqw4w9wgxcq.runeliteinjector.Transformer
import dev.dqw4w9wgxcq.runeliteinjector.toString2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

class UpdateInterfaceTransformer : Transformer {
    private val className = "ix"
    private val methodName = "mw"
    override fun apply(clazz: ClassNode) {
        if (clazz.name != className) {
            return
        }

        val method = clazz.methods.first { it.name == methodName }

        method.toString2()//idk why this is needed but label is 0 without it

        for (insnNode in method.instructions) {
            if (insnNode.type == AbstractInsnNode.LABEL) {
                val labelNode: LabelNode = insnNode as LabelNode
                if (labelNode.toString2() != "227L") {
                    continue
                }

                val iload20: AbstractInsnNode = labelNode.next.next
                println("nextInsnNode: ${iload20.toString2()}")

                // Check if the next instruction is ILOAD 20
                if (iload20.opcode != Opcodes.ILOAD || (iload20 as VarInsnNode).`var` != 20) {
                    throw RuntimeException("Could not find ILOAD 20")
                }

                val toInject = InsnList()

                // Add instructions to print the value of menuInBounds
                toInject.add(FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"))
                toInject.add(VarInsnNode(Opcodes.ILOAD, 20))
                toInject.add(MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false))

                // Add instructions to print the value of menuOptionsCount
                toInject.add(FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"))
                toInject.add(FieldInsnNode(Opcodes.GETSTATIC, "client", "ne", "I"))
                toInject.add(MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false))

                // Insert the new instructions right after the ILOAD 20 instruction
                method.instructions.insert(iload20, toInject)
                break
            }
        }
    }
}