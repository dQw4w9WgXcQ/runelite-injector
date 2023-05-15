package dev.dqw4w9wgxcq.runeliteinjector.injectors

import dev.dqw4w9wgxcq.runeliteinjector.Hooks
import dev.dqw4w9wgxcq.runeliteinjector.Injector
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import org.slf4j.LoggerFactory

class DisableInput : Injector {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun apply(clazz: ClassNode) {
        if (clazz.name.length != 2) return

        for (method in clazz.methods) {
            if (method.name == "mouseClicked" || method.name == "mousePressed" || method.name == "mouseReleased" || method.name == "mouseEntered" || method.name == "mouseExited"
                || method.name == "mouseDragged" || method.name == "mouseMoved"
                || method.name == "focusGained" || method.name == "focusLost"
                || method.name == "mouseWheelMoved"
                || method.name == "keyTyped" || method.name == "keyPressed" || method.name == "keyReleased"
            ) {
                log.debug("Found method ${method.name} in class ${clazz.name}")

                val label = LabelNode()
                val insnList = InsnList()
                insnList.add(FieldInsnNode(Opcodes.GETSTATIC, Hooks.get("Mixins"), Hooks.get("disableInput"), "Z"))
                insnList.add(JumpInsnNode(Opcodes.IFEQ, label))
                insnList.add(InsnNode(Opcodes.RETURN))
                insnList.add(label)

                method.instructions.insert(insnList)
            }
        }
    }
}