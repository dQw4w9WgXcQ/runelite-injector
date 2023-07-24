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

    private val doCycleLoggedInHook = Hooks.getMethod("doCycleLoggedIn")
    private val updateRootInterfaceHook = Hooks.getMethod("updateRootInterface")

    override fun inject(clazz: ClassNode): Boolean {
        if (clazz.name != "client") return false

        val doCycleLoggedIn = clazz.methods.first { it.name == doCycleLoggedInHook.name }

        val invokeUpdateRootInterface = doCycleLoggedIn.instructions.first {
            it.opcode == Opcodes.INVOKESTATIC
                    && (it as MethodInsnNode).name == updateRootInterfaceHook.name
                    && it.owner == updateRootInterfaceHook.owner
                    && it.desc == updateRootInterfaceHook.descriptor
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