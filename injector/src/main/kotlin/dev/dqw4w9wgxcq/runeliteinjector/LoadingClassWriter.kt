package dev.dqw4w9wgxcq.runeliteinjector

import org.objectweb.asm.ClassWriter

class LoadingClassWriter(private val classLoader: ClassLoader, flags: Int) : ClassWriter(flags) {
    override fun getClassLoader(): ClassLoader {
        return classLoader
    }
}