package dev.dqw4w9wgxcq.runeliteinjector.injectors

import dev.dqw4w9wgxcq.runeliteinjector.Hooks
import dev.dqw4w9wgxcq.runeliteinjector.Injector
import dev.dqw4w9wgxcq.runeliteinjector.next
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import org.slf4j.LoggerFactory

class FakeClick : Injector {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun apply(clazz: ClassNode) {
        if (clazz.name != "client") return

        val doCycleLoggedIn = clazz.methods.first { it.name == Hooks.get("doCycleLoggedIn") && it.desc.endsWith("B)V") }
        val iterator = doCycleLoggedIn.instructions.iterator()

        //force mouse recorder packet send
        iterator.next { it.opcode == Opcodes.MONITORENTER }
        val beforeMouseRecorderCheck = iterator.next { it.opcode == Opcodes.ICONST_0 }
        val mouseRecorderTriggeredJump = (iterator.next { it.opcode == Opcodes.IF_ICMPNE } as JumpInsnNode).label.label

        val insns = InsnList()
        insns.add(MethodInsnNode(Opcodes.INVOKESTATIC, Hooks.get("Mixins"), Hooks.get("fakeMouseRecorder"), "()Z"))
        insns.add(JumpInsnNode(Opcodes.IFNE, LabelNode(mouseRecorderTriggeredJump)))

        doCycleLoggedIn.instructions.insertBefore(beforeMouseRecorderCheck, insns)

        //force mouse click packet send
        iterator.next { it.opcode == Opcodes.MONITOREXIT }
        val beforeMouseClickCheck = iterator.next { it.opcode == Opcodes.ICONST_1 }
        val mouseClickTriggeredJump = (iterator.next { it.opcode == Opcodes.IF_ICMPEQ } as JumpInsnNode).label.label

        val insns2 = InsnList()
        insns2.add(MethodInsnNode(Opcodes.INVOKESTATIC, Hooks.get("Mixins"), Hooks.get("fakeMouseClick"), "()Z"))
        insns2.add(JumpInsnNode(Opcodes.IFNE, LabelNode(mouseClickTriggeredJump)))

        doCycleLoggedIn.instructions.insertBefore(beforeMouseClickCheck, insns2)
    }
}