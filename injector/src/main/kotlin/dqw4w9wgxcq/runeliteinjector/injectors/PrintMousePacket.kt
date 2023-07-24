package dqw4w9wgxcq.runeliteinjector.injectors

import dqw4w9wgxcq.runeliteinjector.Hooks
import dqw4w9wgxcq.runeliteinjector.Injector
import dqw4w9wgxcq.runeliteinjector.MixinHooks
import dqw4w9wgxcq.runeliteinjector.next
import dqw4w9wgxcq.runeliteinjector.nextOrNull
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

    override fun inject(clazz: ClassNode): Boolean {
        if (clazz.name != doCycleLoggedInHook.owner) return false

        val doCycleLoggedIn = clazz.methods
            .first { it.name == doCycleLoggedInHook.name && it.desc == doCycleLoggedInHook.descriptor }

        doCycleLoggedIn.toString2()

        injectMouseRecorderWrite(doCycleLoggedIn)
        throw Exception("test")
        injectMouseRecorderSend(doCycleLoggedIn)
        injectButton1Button0(doCycleLoggedIn)
        injectMouseClickPacket(doCycleLoggedIn)

        return true
    }

    private val lastMouseRecorderTimeFieldName = "eo"
    private fun injectMouseRecorderWrite(doCycleLoggedIn: MethodNode) {
        val itr = doCycleLoggedIn.instructions.iterator()

        //https://i.imgur.com/Ghhs6uS.png
        itr.next { it.opcode == Opcodes.MONITORENTER }
        itr.next { it.opcode == Opcodes.BIPUSH && (it as IntInsnNode).operand == 40 }

        val dtVarIdx =
        val dxVarIdx =
        val dyVarIdx =
        val dtRemAccumulationVarIdx =

        log.info("var indicies: dt=$dtVarIdx, dx=$dxVarIdx, dy=$dyVarIdx, dtRemAccumulation=$dtRemAccumulationVarIdx")

        //https://i.imgur.com/ycwW64E.png
        val insertAfter = itr
            .next { it.opcode == Opcodes.PUTSTATIC && (it as FieldInsnNode).name == lastMouseRecorderTimeFieldName }

        val insns = InsnList()
        insns.add(VarInsnNode(Opcodes.ILOAD, dtVarIdx))
        insns.add(VarInsnNode(Opcodes.ILOAD, dxVarIdx))
        insns.add(VarInsnNode(Opcodes.ILOAD, dyVarIdx))
        insns.add(VarInsnNode(Opcodes.ILOAD, dtRemAccumulationVarIdx))
        insns.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC,
                MixinHooks.get("Mixins"),
                MixinHooks.get("mouseRecorderWrite"),
                "(IIII)V",
                false
            )
        )
        doCycleLoggedIn.instructions.insertBefore(insertAfter.next, insns)
    }

    private val someMult = 2560228884295272563L
    private fun injectMouseRecorderSend(doCycleLoggedIn: MethodNode) {
        val itr = doCycleLoggedIn.instructions.iterator()

        itr.next { it.opcode == Opcodes.MONITORENTER }
        itr.next { it.opcode == Opcodes.LDC && (it as LdcInsnNode).cst == someMult }

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

    private val someOwnerMi = "mi"
    private val someNameAn = "an"
    private fun injectButton1Button0(doCycleLoggedIn: MethodNode) {
        val itr = doCycleLoggedIn.instructions.iterator()

        itr.next { it.opcode == Opcodes.MONITOREXIT }

        itr.next {
            it.opcode == Opcodes.INVOKESTATIC
                    && (it as MethodInsnNode).owner == someOwnerMi
                    && it.name == someNameAn
        }

        val iconst1 = itr.next { it.opcode == Opcodes.ICONST_1 }
        val invokeButton1Insns = InsnList()
        invokeButton1Insns.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC,
                MixinHooks.get("Mixins"),
                MixinHooks.get("button1"),
                "()V",
                false
            )
        )
        doCycleLoggedIn.instructions.insertBefore(iconst1, invokeButton1Insns)

        val iconst0 = itr.next { it.opcode == Opcodes.ICONST_0 }
        val invokeButton0Insns = InsnList()
        invokeButton0Insns.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC,
                MixinHooks.get("Mixins"),
                MixinHooks.get("button0"),
                "()V",
                false
            )
        )
        doCycleLoggedIn.instructions.insertBefore(iconst0, invokeButton0Insns)
    }

    private val packetWriterHook = Hooks.getField("packetWriter")
    private fun injectMouseClickPacket(doCycleLoggedIn: MethodNode) {
        val itr = doCycleLoggedIn.instructions.iterator()

        itr.next { it.opcode == Opcodes.MONITOREXIT }

        itr.next { it.opcode == Opcodes.BIPUSH && (it as IntInsnNode).operand == -34 }

        val insns = InsnList()
        insns.add(VarInsnNode(Opcodes.ILOAD, 6))//dt
        insns.add(VarInsnNode(Opcodes.ILOAD, 5))//x
        insns.add(VarInsnNode(Opcodes.ILOAD, 4))//y
        insns.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC,
                MixinHooks.get("Mixins"),
                MixinHooks.get("mouseClickPacket"),
                "(III)V",
                false
            )
        )

        doCycleLoggedIn.instructions.insertBefore(
            itr.next { it.opcode == Opcodes.GETSTATIC && (it as FieldInsnNode).name == packetWriterHook.name },
            insns
        )
    }
}