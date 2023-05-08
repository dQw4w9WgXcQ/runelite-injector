package dev.dqw4w9wgxcq.cbarn

import dev.dqw4w9wgxcq.runeliteinjector.Transformer
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.VarInsnNode

class InsertMenuItemTransformer : Transformer {
    private val className = "jc"
    private val methodName = "lv"

    override fun apply(clazz: ClassNode) {
        if (clazz.name != className) {
            return
        }

        val doAction = clazz.methods.first { it.name == methodName }

        println("found insertMenuItem ${clazz.name} ${doAction.name}")

        val insnList = InsnList()

        val parameterNames =
            listOf("action", "target", "opcode", "identifier", "arg1", "arg2", "itemId", "shiftClick")
        for (i in 0 until 8) {
            //print parameter name
            insnList.add(FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"))
            val space = if (i == 0) "" else " "
            insnList.add(LdcInsnNode(space + parameterNames[i] + ": "))
            insnList.add(
                MethodInsnNode(
                    Opcodes.INVOKEVIRTUAL,
                    "java/io/PrintStream",
                    "print",
                    "(Ljava/lang/String;)V",
                    false
                )
            )

            insnList.add(FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"))
            when (i) {
                2, 3, 4, 5, 6 -> {
                    insnList.add(VarInsnNode(Opcodes.ILOAD, i))
                    insnList.add(
                        MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            "java/lang/Integer",
                            "toString",
                            "(I)Ljava/lang/String;",
                            false
                        )
                    )
                }

                0, 1 -> {
                    insnList.add(VarInsnNode(Opcodes.ALOAD, i))
                }

                7 -> {
                    insnList.add(VarInsnNode(Opcodes.ILOAD, i))
                    insnList.add(
                        MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            "java/lang/Boolean",
                            "toString",
                            "(Z)Ljava/lang/String;",
                            false
                        )
                    )
                }
            }
            insnList.add(
                MethodInsnNode(
                    Opcodes.INVOKEVIRTUAL,
                    "java/io/PrintStream",
                    "print",
                    "(Ljava/lang/String;)V",
                    false
                )
            )
        }

        //add a newline character after printing all parameters
        insnList.add(FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"))
        insnList.add(LdcInsnNode("\n"))
        insnList.add(
            MethodInsnNode(
                Opcodes.INVOKEVIRTUAL,
                "java/io/PrintStream",
                "print",
                "(Ljava/lang/String;)V",
                false
            )
        )

        doAction.instructions.insertBefore(doAction.instructions.first, insnList)
    }
}