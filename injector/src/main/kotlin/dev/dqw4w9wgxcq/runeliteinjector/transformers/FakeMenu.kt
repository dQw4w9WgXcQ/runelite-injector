package dev.dqw4w9wgxcq.runeliteinjector.transformers

import dev.dqw4w9wgxcq.runeliteinjector.Transformer
import dev.dqw4w9wgxcq.runeliteinjector.toString2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodInsnNode

class FakeMenu : Transformer {
    companion object {
        const val className = "client"
        const val doCycleLoggedIn = "hq"
        const val doCycleLoggedInDesc = "(B)V"
        const val rl10 = "rl10"
        const val menuThingMethodName = "zz"
        const val widgetThingMethodName = "yy"
    }

    override fun apply(clazz: ClassNode) {
        if (clazz.name == className) {
            val doCycleLoggedIn = clazz.methods.first { it.name == doCycleLoggedIn && it.desc == doCycleLoggedInDesc }

            println("found doCycleLoggedIn ${clazz.name} ${doCycleLoggedIn.name}")

            val invokeUpdateRootInterface = doCycleLoggedIn
                .instructions
                .first { it.opcode == Opcodes.INVOKESTATIC && (it as MethodInsnNode).name == "mt" && it.owner == "bj" && it.desc == "(IIIIIIII)V" }

            println("found invoke updateRootInterface: " + invokeUpdateRootInterface.toString2())

            val insnList = InsnList()
            insnList.add(MethodInsnNode(Opcodes.INVOKESTATIC, rl10, menuThingMethodName, "()V"))

            doCycleLoggedIn.instructions.insertBefore(invokeUpdateRootInterface, insnList)

            val insnList2 = InsnList()
            insnList2.add(MethodInsnNode(Opcodes.INVOKESTATIC, rl10, widgetThingMethodName, "()V"))

            var insertBefore = invokeUpdateRootInterface.next
            while (insertBefore.opcode != Opcodes.INVOKESTATIC) {
                insertBefore = insertBefore.next
            }
            doCycleLoggedIn.instructions.insertBefore(insertBefore, insnList2)
        }
    }
}