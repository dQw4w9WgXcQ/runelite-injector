package dqw4w9wgxcq.runeliteinjector.injectors

import dqw4w9wgxcq.runeliteinjector.Hooks
import dqw4w9wgxcq.runeliteinjector.Injector
import dqw4w9wgxcq.runeliteinjector.MixinHooks
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodInsnNode
import org.slf4j.LoggerFactory

class FakeMenu : Injector {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun inject(clazz: ClassNode): Boolean {
        if (clazz.name != "client") return false

        val doCycleLoggedIn = clazz.methods.first { it.name == MixinHooks.get("doCycleLoggedIn") }

        val invokeUpdateRootInterface = doCycleLoggedIn.instructions.first {
            it as MethodInsnNode

            it.opcode == Opcodes.INVOKESTATIC
                    && it.name == Hooks.getMethod("updateRootInterface").name
                    && it.owner == Hooks.getMethod("updateRootInterfaceClass").name
                    && it.desc == Hooks.getMethod("updateRootInterfaceDesc").name
        }

        val insnList = InsnList()
        insnList.add(
            MethodInsnNode(Opcodes.INVOKESTATIC, MixinHooks.get("Mixins"), MixinHooks.get("menuThingMethodName"), "()V")
        )

        doCycleLoggedIn.instructions.insertBefore(invokeUpdateRootInterface, insnList)

        val insnList2 = InsnList()
        insnList2.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC,
                MixinHooks.get("Mixins"),
                MixinHooks.get("widgetThingMethodName"),
                "()V"
            )
        )

        var insertBefore = invokeUpdateRootInterface.next
        while (insertBefore.opcode != Opcodes.INVOKESTATIC) {
            insertBefore = insertBefore.next
        }
        doCycleLoggedIn.instructions.insertBefore(insertBefore, insnList2)

        return true
    }
}