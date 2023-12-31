package dqw4w9wgxcq.runeliteinjector.injectors

import dqw4w9wgxcq.runeliteinjector.Hooks
import dqw4w9wgxcq.runeliteinjector.Injector
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.GETSTATIC
import org.objectweb.asm.Opcodes.ILOAD
import org.objectweb.asm.Opcodes.INVOKESTATIC
import org.objectweb.asm.Opcodes.INVOKEVIRTUAL
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.VarInsnNode
import org.slf4j.LoggerFactory

class PrintDoAction : Injector {
    private val log = LoggerFactory.getLogger(this::class.java)

    private val menuActionHook = Hooks.getMethod("menuAction")

    override fun inject(clazz: ClassNode): Boolean {
        if (clazz.name != menuActionHook.owner) return false

        val doAction = clazz.methods.firstOrNull { it.name == menuActionHook.name }
            ?: throw Exception("doAction not found in ClassNode ${clazz.name}")

        println("found doAction ${clazz.name} ${doAction.name}")

        val insnList = InsnList()

        val parameterNames = listOf(
            "arg1", "arg2", "opcode", "identifier", "itemId", "action", "target", "mouseX", "mouseY"
        )

        for (i in 0..<9) {
            //print parameter name
            insnList.add(
                FieldInsnNode(
                    GETSTATIC,
                    "java/lang/System",
                    "out",
                    "Ljava/io/PrintStream;"
                )
            )
            val space = if (i == 0) "" else " "
            insnList.add(LdcInsnNode(space + parameterNames[i] + ": "))
            insnList.add(
                MethodInsnNode(
                    INVOKEVIRTUAL,
                    "java/io/PrintStream",
                    "print",
                    "(Ljava/lang/String;)V",
                    false
                )
            )

            insnList.add(
                FieldInsnNode(
                    GETSTATIC,
                    "java/lang/System",
                    "out",
                    "Ljava/io/PrintStream;"
                )
            )
            when (i) {
                in 0..4, 7, 8 -> {
                    insnList.add(VarInsnNode(ILOAD, i))
                    insnList.add(
                        MethodInsnNode(
                            INVOKESTATIC,
                            "java/lang/Integer",
                            "toString",
                            "(I)Ljava/lang/String;",
                            false
                        )
                    )
                }

                5, 6 -> {
                    insnList.add(VarInsnNode(ALOAD, i))
                }
            }
            insnList.add(
                MethodInsnNode(
                    INVOKEVIRTUAL,
                    "java/io/PrintStream",
                    "print",
                    "(Ljava/lang/String;)V",
                    false
                )
            )
        }

        //add a newline character after printing all parameters
        insnList.add(
            FieldInsnNode(
                GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;"
            )
        )
        insnList.add(LdcInsnNode("\n"))
        insnList.add(
            MethodInsnNode(
                INVOKEVIRTUAL,
                "java/io/PrintStream",
                "print",
                "(Ljava/lang/String;)V",
                false
            )
        )

        doAction.instructions.insertBefore(doAction.instructions.first, insnList)

        return true
    }
}