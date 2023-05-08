package dev.dqw4w9wgxcq.runeliteinjector.transformers

import dev.dqw4w9wgxcq.runeliteinjector.Transformer
import dev.dqw4w9wgxcq.runeliteinjector.toString2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

class PrintMouseRecorderSend : Transformer {
    override fun apply(clazz: ClassNode) {
        if (clazz.name != "client") return

        println("found client")

        val doCycleLoggedIn = clazz.methods
            .firstOrNull { it.name == FakeMenu.doCycleLoggedIn && it.desc == FakeMenu.doCycleLoggedInDesc }
            ?: throw Exception("didnt find doCycleLoggedIn method for PrintMouseRecorderSend")

        var insertBefore: AbstractInsnNode? = null
        for (instruction in doCycleLoggedIn.instructions) {
            if (instruction.opcode == Opcodes.PUTSTATIC) {
                if ((instruction as FieldInsnNode).name == "ef") {
                    println(instruction.toString2())
                    insertBefore = instruction.next
                }
            }
        }

        val insertInsns = InsnList()
        insertInsns.add(VarInsnNode(Opcodes.ILOAD, 6))
        insertInsns.add(VarInsnNode(Opcodes.ILOAD, 13))
        insertInsns.add(VarInsnNode(Opcodes.ILOAD, 11))
        insertInsns.add(VarInsnNode(Opcodes.ILOAD, 12))
        insertInsns.add(MethodInsnNode(Opcodes.INVOKESTATIC, "rl10", "printMouseRecorderSend", "(IIII)V", false))

        doCycleLoggedIn.instructions.insertBefore(insertBefore, insertInsns)

        var seenGetStaticMousePacket = false
        var insertAfter: AbstractInsnNode? = null
        var button1: AbstractInsnNode? = null
        var button0: AbstractInsnNode? = null
        var seenKankers = false
        for (instruction in doCycleLoggedIn.instructions) {
            if (!seenGetStaticMousePacket) {
                if (instruction.opcode == Opcodes.GETSTATIC) {
                    val getStatic = instruction as FieldInsnNode
                    if (getStatic.name == "ai" && getStatic.owner == "lv") {
                        println("found getstatic mouse packet insn" + instruction.toString2())
                        seenGetStaticMousePacket = true

                    }
                }

                continue
            }

            if (!seenKankers) {
                if (instruction.opcode == Opcodes.ICONST_1) {
                    button1 = instruction
                } else if (instruction.opcode == Opcodes.ICONST_0) {
                    button0 = instruction
                    seenKankers = true
                }
                continue
            }

            if (instruction.opcode == Opcodes.INVOKESTATIC) {
                val invokeStatic = instruction as MethodInsnNode
                if (invokeStatic.name == "lo" && invokeStatic.owner == "eq") {
                    println("found add packet to buffer method insn" + instruction.toString2())
                    insertAfter = instruction
                    break
                }
            }
        }

        if (button1 == null) throw Exception("didnt find kanker1 for PrintMouseRecorderSend")
        if (button0 == null) throw Exception("didnt find kanker0 for PrintMouseRecorderSend")
        val insertInsnsKanker1 = InsnList()
        insertInsnsKanker1.add(MethodInsnNode(Opcodes.INVOKESTATIC, "rl10", "button1", "()V", false))
        doCycleLoggedIn.instructions.insertBefore(button1.next, insertInsnsKanker1)
        val insertInsnsKanker0 = InsnList()
        insertInsnsKanker0.add(MethodInsnNode(Opcodes.INVOKESTATIC, "rl10", "button0", "()V", false))
        doCycleLoggedIn.instructions.insertBefore(button0.next, insertInsnsKanker0)

        if (insertAfter == null) throw Exception("didnt find add packet to buffer method insn for PrintMouseRecorderSend")

        val insertBefore2 = insertAfter.next

        val insertInsns2 = InsnList()
        insertInsns2.add(VarInsnNode(Opcodes.ILOAD, 6))//dt
        insertInsns2.add(VarInsnNode(Opcodes.ILOAD, 5))//x
        insertInsns2.add(VarInsnNode(Opcodes.ILOAD, 4))//y
        insertInsns2.add(MethodInsnNode(Opcodes.INVOKESTATIC, "rl10", "printMouseClickSend", "(III)V", false))

        doCycleLoggedIn.instructions.insertBefore(insertBefore2, insertInsns2)
    }
}