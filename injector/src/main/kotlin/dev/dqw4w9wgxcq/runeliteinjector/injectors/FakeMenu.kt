package dev.dqw4w9wgxcq.runeliteinjector.injectors

import dev.dqw4w9wgxcq.runeliteinjector.Hooks
import dev.dqw4w9wgxcq.runeliteinjector.Injector
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodInsnNode
import org.slf4j.LoggerFactory

class FakeMenu : Injector {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun apply(clazz: ClassNode) {
        if (clazz.name != "client") return

        val doCycleLoggedIn = clazz.methods.first { it.name == Hooks.get("doCycleLoggedIn") }

        val invokeUpdateRootInterface = doCycleLoggedIn
            .instructions
            .first {
                it.opcode == Opcodes.INVOKESTATIC
                        && (it as MethodInsnNode).name == Hooks.get("updateRootInterface")
                        && it.owner == Hooks.get("updateRootInterfaceOwner")
                        && it.desc == Hooks.get("updateRootInterfaceDesc")
            }

        val insnList = InsnList()
        insnList.add(MethodInsnNode(Opcodes.INVOKESTATIC, Hooks.get("Mixins"), Hooks.get("menuThingMethodName"), "()V"))

        doCycleLoggedIn.instructions.insertBefore(invokeUpdateRootInterface, insnList)

        val insnList2 = InsnList()
        insnList2.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC,
                Hooks.get("Mixins"),
                Hooks.get("widgetThingMethodName"),
                "()V"
            )
        )

        var insertBefore = invokeUpdateRootInterface.next
        while (insertBefore.opcode != Opcodes.INVOKESTATIC) {
            insertBefore = insertBefore.next
        }
        doCycleLoggedIn.instructions.insertBefore(insertBefore, insnList2)
    }
}