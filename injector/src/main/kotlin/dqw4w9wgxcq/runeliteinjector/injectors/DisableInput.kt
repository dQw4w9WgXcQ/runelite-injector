package dqw4w9wgxcq.runeliteinjector.injectors

import dqw4w9wgxcq.runeliteinjector.Injector
import dqw4w9wgxcq.runeliteinjector.MixinHooks
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
import org.slf4j.LoggerFactory

class DisableInput : Injector {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun inject(clazz: ClassNode): Boolean {
        if (clazz.name.length != 2) return false

        var didInject = false

        for (method in clazz.methods) {
            if (method.name == "mouseClicked"
                || method.name == "mousePressed"
                || method.name == "mouseReleased"
                || method.name == "mouseEntered"
                || method.name == "mouseExited"
                || method.name == "mouseDragged"
                || method.name == "mouseMoved"
                || method.name == "focusGained"
                || method.name == "focusLost"
                || method.name == "mouseWheelMoved"
                || method.name == "keyTyped"
                || method.name == "keyPressed"
                || method.name == "keyReleased"
            ) {
                log.debug("Found method ${method.name} in class ${clazz.name}")
                didInject = true

                val label = LabelNode()
                val insnList = InsnList()
                insnList.add(
                    FieldInsnNode(
                        Opcodes.GETSTATIC,
                        MixinHooks.get("Mixins"),
                        MixinHooks.get("disableInput"),
                        "Z",
                    )
                )
                insnList.add(JumpInsnNode(Opcodes.IFEQ, label))
                insnList.add(InsnNode(Opcodes.RETURN))
                insnList.add(label)

                method.instructions.insert(insnList)
            }
        }

        return didInject
    }
}