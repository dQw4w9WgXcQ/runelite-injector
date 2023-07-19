package dqw4w9wgxcq.runeliteinjector.injectors

import dqw4w9wgxcq.runeliteinjector.Hooks
import dqw4w9wgxcq.runeliteinjector.Injector
import dqw4w9wgxcq.runeliteinjector.MixinHooks
import dqw4w9wgxcq.runeliteinjector.next
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import org.slf4j.LoggerFactory

class FakeClick : Injector {
    private val log = LoggerFactory.getLogger(this::class.java)

    private val clientHook = Hooks.getClass("Client")
    private val doCycleLoggedInHook = clientHook.getMethod("doCycleLoggedIn")

    override fun inject(clazz: ClassNode): Boolean {
        if (clientHook.name != clazz.name) return false

        val doCycleLoggedIn = clazz.methods.first {
            it.name == doCycleLoggedInHook.owner && it.desc == doCycleLoggedInHook.descriptor
        }
        val iterator = doCycleLoggedIn.instructions.iterator()

        //force mouse recorder packet send
        iterator.next { it.opcode == Opcodes.MONITORENTER }
        val beforeMouseRecorderCheck = iterator.next { it.opcode == Opcodes.ICONST_0 }
        val mouseRecorderTriggeredJump = (iterator.next { it.opcode == Opcodes.IF_ICMPNE } as JumpInsnNode).label.label
        val insns = InsnList()
        insns.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC, MixinHooks.get("Mixins"), MixinHooks.get("fakeMouseRecorder"), "()Z"
            )
        )
        insns.add(JumpInsnNode(Opcodes.IFNE, LabelNode(mouseRecorderTriggeredJump)))
        doCycleLoggedIn.instructions.insertBefore(beforeMouseRecorderCheck, insns)

        //force mouse click packet send
        iterator.next { it.opcode == Opcodes.MONITOREXIT }
        val beforeMouseClickCheck = iterator.next { it.opcode == Opcodes.ICONST_1 }
        val mouseClickTriggeredJump = (iterator.next { it.opcode == Opcodes.IF_ICMPEQ } as JumpInsnNode).label.label
        val insns2 = InsnList()
        insns2.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC, MixinHooks.get("Mixins"), MixinHooks.get("fakeMouseClick"), "()Z"
            )
        )
        insns2.add(JumpInsnNode(Opcodes.IFNE, LabelNode(mouseClickTriggeredJump)))
        doCycleLoggedIn.instructions.insertBefore(beforeMouseClickCheck, insns2)

        //fake x
        val istoreX = iterator.next { it.opcode == Opcodes.ISTORE } as VarInsnNode
        check(istoreX.`var` == 4)
        val insns3 = InsnList()
        insns3.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC, MixinHooks.get("Mixins"), MixinHooks.get("getFakeX"), "(I)I"
            )
        )
        doCycleLoggedIn.instructions.insertBefore(istoreX, insns3)

        //fake y
        val istoreY = iterator.next {
            it.opcode == Opcodes.ISTORE && it is VarInsnNode && it.`var` != istoreX.`var`
        } as VarInsnNode
        val insns4 = InsnList()
        insns4.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC, MixinHooks.get("Mixins"), MixinHooks.get("getFakeY"), "(I)I"
            )
        )
        doCycleLoggedIn.instructions.insertBefore(istoreY, insns4)

        //fake button
        val ifIcmpne = iterator.next { it.opcode == Opcodes.IF_ICMPNE } as JumpInsnNode
        val insns5 = InsnList()
        insns5.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC, MixinHooks.get("Mixins"), MixinHooks.get("getFakeButton"), "(I)I"
            )
        )
        doCycleLoggedIn.instructions.insertBefore(ifIcmpne, insns5)

        return true
    }
}