package dev.dqw4w9wgxcq.runeliteinjector.transformers

import dev.dqw4w9wgxcq.runeliteinjector.Transformer
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

class FakeClick : Transformer {
    override fun apply(clazz: ClassNode) {
        if (clazz.name != "client") return

        println("found client")

        val doCycleLoggedIn = clazz.methods
            .firstOrNull { it.name == FakeMenu.doCycleLoggedIn && it.desc == FakeMenu.doCycleLoggedInDesc }
            ?: throw Exception("didnt find doCycleLoggedIn method for FakeClick")

        var insertBeforeIconst0: AbstractInsnNode? = null
        var jumpTo: LabelNode? = null
        var inSyncBlock = false
        for (instruction in doCycleLoggedIn.instructions) {
            if (instruction.opcode == Opcodes.MONITORENTER) {
                inSyncBlock = true
            }

            if (!inSyncBlock) continue

            if (insertBeforeIconst0 == null && instruction.opcode == Opcodes.ICONST_0) {
                insertBeforeIconst0 = instruction
            }

            if (instruction.opcode == Opcodes.IF_ICMPNE) {
                jumpTo = (instruction as JumpInsnNode).label
                break
            }
        }

        if (insertBeforeIconst0 == null || jumpTo == null) throw Exception("didnt find ICONST_0 or IF_ICMPNE for FakeClick")

        val insertInsns = InsnList()
        insertInsns.add(FieldInsnNode(Opcodes.GETSTATIC, "rl10", "fakeClick", "Z"))
        insertInsns.add(JumpInsnNode(Opcodes.IFNE, jumpTo))

        doCycleLoggedIn.instructions.insertBefore(insertBeforeIconst0, insertInsns)
    }
}