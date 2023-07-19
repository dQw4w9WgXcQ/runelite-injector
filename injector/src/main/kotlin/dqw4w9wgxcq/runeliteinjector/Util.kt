package dqw4w9wgxcq.runeliteinjector

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.util.Textifier
import org.objectweb.asm.util.TraceMethodVisitor
import java.io.PrintWriter
import java.io.StringWriter

private val printer: Textifier = Textifier()
private val printVisitor: MethodVisitor = TraceMethodVisitor(printer)

fun AbstractInsnNode.toString2(): String {
    accept(printVisitor)
    val sw = StringWriter()
    printer.print(PrintWriter(sw))
    printer.getText().clear()
    return sw.toString().trimIndent()
}

fun MethodNode.toString2(): String {
    accept(printVisitor)
    val sw = StringWriter()
    printer.print(PrintWriter(sw))
    printer.getText().clear()
    return sw.toString().trimIndent()
}

fun <T> Iterator<T>.next(predicate: (T) -> Boolean): T {
    while (hasNext()) {
        val next = next()
        if (predicate(next)) return next
    }
    throw NoSuchElementException()
}