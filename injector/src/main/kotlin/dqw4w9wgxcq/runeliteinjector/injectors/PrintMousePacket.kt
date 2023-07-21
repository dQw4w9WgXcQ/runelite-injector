package dqw4w9wgxcq.runeliteinjector.injectors

import dqw4w9wgxcq.runeliteinjector.Hooks
import dqw4w9wgxcq.runeliteinjector.Injector
import dqw4w9wgxcq.runeliteinjector.MixinHooks
import dqw4w9wgxcq.runeliteinjector.next
import dqw4w9wgxcq.runeliteinjector.toString2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.IntInsnNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.VarInsnNode
import org.slf4j.LoggerFactory

class PrintMousePacket : Injector {
    private val log = LoggerFactory.getLogger(this::class.java)

    private val doCycleLoggedInHook = Hooks.getMethod("doCycleLoggedIn")
    private val packetWriterHook = Hooks.getField("packetWriter")

    override fun inject(clazz: ClassNode): Boolean {
        if (clazz.name != doCycleLoggedInHook.owner) return false

        val doCycleLoggedIn = clazz.methods.first { it.name == doCycleLoggedInHook.name && it.desc.contains("B)V") }

        doCycleLoggedIn.toString2()

        injectMouseRecorderWrite(doCycleLoggedIn)
        injectMouseRecorderSend(doCycleLoggedIn)
        injectButton1Button0(doCycleLoggedIn)
        injectMouseClickPacket(doCycleLoggedIn)

        return true
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
                MixinHooks.get("Mixins"),
                MixinHooks.get("mouseRecorderWrite"),
                "(IIII)V",
                false
            )
        )
        doCycleLoggedIn.instructions.insertBefore(insertBefore, insert)
    }

    private fun injectMouseRecorderSend(doCycleLoggedIn: MethodNode) {
        val itr = doCycleLoggedIn.instructions.iterator()

        itr.next { it.opcode == Opcodes.MONITORENTER }

        itr.next { it.opcode == Opcodes.LDC && (it as LdcInsnNode).cst == 2560228884295272563L }

        val returnInsn = itr.next { it.opcode == Opcodes.RETURN }

        doCycleLoggedIn.instructions.insertBefore(
            returnInsn.next.next,
            MethodInsnNode(
                Opcodes.INVOKESTATIC,
                MixinHooks.get("Mixins"),
                MixinHooks.get("mouseRecorderSend"),
                "()V"
            )
        )
    }

    private fun injectButton1Button0(doCycleLoggedIn: MethodNode) {
        val itr = doCycleLoggedIn.instructions.iterator()

        itr.next { it.opcode == Opcodes.MONITOREXIT }

        itr.next {
            it.opcode == Opcodes.INVOKESTATIC &&
                    it is MethodInsnNode &&
                    it.owner == "mi" &&
                    it.name == "an"
        }

        val iconst1 = itr.next { it.opcode == Opcodes.ICONST_1 }
        val invokeButton1 = InsnList()
        invokeButton1.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC,
                MixinHooks.get("Mixins"),
                MixinHooks.get("button1"),
                "()V",
                false
            )
        )
        doCycleLoggedIn.instructions.insertBefore(iconst1, invokeButton1)

        val iconst0 = itr.next { it.opcode == Opcodes.ICONST_0 }
        val invokeButton0 = InsnList()
        invokeButton0.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC,
                MixinHooks.get("Mixins"),
                MixinHooks.get("button0"),
                "()V",
                false
            )
        )
        doCycleLoggedIn.instructions.insertBefore(iconst0, invokeButton0)
    }

    private fun injectMouseClickPacket(doCycleLoggedIn: MethodNode) {
        val itr = doCycleLoggedIn.instructions.iterator()

        itr.next { it.opcode == Opcodes.MONITOREXIT }

        itr.next { it.opcode == Opcodes.BIPUSH && it is IntInsnNode && it.operand == -34 }

        val insert = InsnList()
        insert.add(VarInsnNode(Opcodes.ILOAD, 6))//dt
        insert.add(VarInsnNode(Opcodes.ILOAD, 5))//x
        insert.add(VarInsnNode(Opcodes.ILOAD, 4))//y
        insert.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC,
                MixinHooks.get("Mixins"),
                MixinHooks.get("mouseClickPacket"),
                "(III)V",
                false
            )
        )

        doCycleLoggedIn.instructions.insertBefore(
            itr.next {
                it.opcode == Opcodes.GETSTATIC && it is FieldInsnNode && it.name == packetWriterHook.name
            },
            insert
        )
    }
}