package dev.dqw4w9wgxcq.runeliteinjector.injectors

import dev.dqw4w9wgxcq.runeliteinjector.Hooks
import dev.dqw4w9wgxcq.runeliteinjector.Injector
import dev.dqw4w9wgxcq.runeliteinjector.next
import dev.dqw4w9wgxcq.runeliteinjector.toString2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import org.slf4j.LoggerFactory

class PrintMousePacket : Injector {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun apply(clazz: ClassNode) {
        if (clazz.name != "client") return

        val doCycleLoggedIn = clazz.methods.first { it.name == Hooks.get("doCycleLoggedIn") && it.desc.contains("B)V") }

        doCycleLoggedIn.toString2()

        injectMouseRecorderWrite(doCycleLoggedIn)
        injectMouseRecorderSend(doCycleLoggedIn)
        injectButton1Button0(doCycleLoggedIn)
        injectMouseClickPacket(doCycleLoggedIn)
    }

    private fun injectMouseRecorderWrite(doCycleLoggedIn: MethodNode) {
        val itr = doCycleLoggedIn.instructions.iterator()

        itr.next { it.opcode == Opcodes.MONITORENTER }

        val putStatic = itr.next { it.opcode == Opcodes.PUTSTATIC && (it as FieldInsnNode).name == "ef" }

        val insertBefore = putStatic.next

        val insert = InsnList()
        insert.add(VarInsnNode(Opcodes.ILOAD, 13))//dt
        insert.add(VarInsnNode(Opcodes.ILOAD, 11))//dx
        insert.add(VarInsnNode(Opcodes.ILOAD, 12))//dy
        insert.add(VarInsnNode(Opcodes.ILOAD, 6))//dtRemAccumulation
        insert.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC,
                Hooks.get("Mixins"),
                Hooks.get("mouseRecorderWrite"),
                "(IIII)V",
                false
            )
        )
        doCycleLoggedIn.instructions.insertBefore(insertBefore, insert)
    }

    private fun injectMouseRecorderSend(doCycleLoggedIn: MethodNode) {
        val iterator = doCycleLoggedIn.instructions.iterator()

        iterator.next { it.opcode == Opcodes.MONITORENTER }

        iterator.next { it.opcode == Opcodes.LDC && (it as LdcInsnNode).cst == 2560228884295272563L }

        val returnInsn = iterator.next { it.opcode == Opcodes.RETURN }

        doCycleLoggedIn.instructions.insertBefore(
            returnInsn.next.next,
            MethodInsnNode(Opcodes.INVOKESTATIC, Hooks.get("Mixins"), Hooks.get("mouseRecorderSend"), "()V")
        )
    }

    private fun injectButton1Button0(doCycleLoggedIn: MethodNode) {
        val iterator = doCycleLoggedIn.instructions.iterator()

        iterator.next { it.opcode == Opcodes.MONITOREXIT }

        iterator.next { it.opcode == Opcodes.INVOKESTATIC && it is MethodInsnNode && it.owner == "mi" && it.name == "an" }

        val iconst1 = iterator.next { it.opcode == Opcodes.ICONST_1 }
        val invokeButton1 = InsnList()
        invokeButton1.add(MethodInsnNode(Opcodes.INVOKESTATIC, Hooks.get("Mixins"), Hooks.get("button1"), "()V", false))
        doCycleLoggedIn.instructions.insertBefore(iconst1, invokeButton1)

        val iconst0 = iterator.next { it.opcode == Opcodes.ICONST_0 }
        val invokeButton0 = InsnList()
        invokeButton0.add(MethodInsnNode(Opcodes.INVOKESTATIC, Hooks.get("Mixins"), Hooks.get("button0"), "()V", false))
        doCycleLoggedIn.instructions.insertBefore(iconst0, invokeButton0)
    }

    private fun injectMouseClickPacket(doCycleLoggedIn: MethodNode) {
        val iterator = doCycleLoggedIn.instructions.iterator()

        iterator.next { it.opcode == Opcodes.MONITOREXIT }

        iterator.next { it.opcode == Opcodes.BIPUSH && it is IntInsnNode && it.operand == -34 }

        val insert = InsnList()
        insert.add(VarInsnNode(Opcodes.ILOAD, 6))//dt
        insert.add(VarInsnNode(Opcodes.ILOAD, 5))//x
        insert.add(VarInsnNode(Opcodes.ILOAD, 4))//y
        insert.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC,
                Hooks.get("Mixins"),
                Hooks.get("mouseClickPacket"),
                "(III)V",
                false
            )
        )

        doCycleLoggedIn.instructions.insertBefore(
            iterator.next { it.opcode == Opcodes.GETSTATIC && it is FieldInsnNode && it.name == Hooks.get("packetWriter") },
            insert
        )
    }
}