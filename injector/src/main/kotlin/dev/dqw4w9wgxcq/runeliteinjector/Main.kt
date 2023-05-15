package dev.dqw4w9wgxcq.runeliteinjector

import dev.dqw4w9wgxcq.runeliteinjector.injectors.FakeClick
import dev.dqw4w9wgxcq.runeliteinjector.injectors.PrintDoAction
import dev.dqw4w9wgxcq.runeliteinjector.injectors.PrintMousePacket
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URLClassLoader
import java.nio.file.Path
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

object Main {
    private val log = LoggerFactory.getLogger(Main::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        log.info(System.getProperty("user.dir"))
        val patched = Path.of(System.getProperty("user.home"), ".runelite", "cache", "patched.cache").toFile()
        if (!patched.exists()) throw Exception("patched.cache not found")

        val runeliteApi = Path.of(
            System.getProperty("user.home"),
            ".runelite",
            "repository2",
            "runelite-api-1.10.0.2-runtime.jar"
        ).toFile()
        if (!runeliteApi.exists()) throw Exception("runelite-api not found")

        val mixins = File(
            System.getProperty("user.home"),
            "/projects/runelite-injector/mixins/build/libs/mixins-1.0-SNAPSHOT.jar"
        )
        if (!mixins.exists()) throw Exception("mixins not found in ${mixins.absolutePath}")

        val outFile = Path.of(System.getProperty("user.home"), "NovaLite", "cache", "patched.cache").toFile()
        if (outFile.exists()) {
            log.info("Deleting old patched.cache")
            outFile.delete()
        }

        val runeliteJar = JarFile(patched)

        //load classes necessary for COMPUTE_FRAMES
        val classLoader = URLClassLoader(
            arrayOf(patched.toURI().toURL(), runeliteApi.toURI().toURL(), mixins.toURI().toURL()),
            this.javaClass.classLoader
        )
        for (entry in runeliteJar.entries()) {
            if (entry.name.endsWith(".class")) {
                Class.forName(entry.name.replace("/", ".").removeSuffix(".class"), false, classLoader)
            }
        }

        val output = JarOutputStream(outFile.outputStream())

        //add mixins
        val mixinsJar = JarFile(mixins)
        for (entry in mixinsJar.entries()) {
            if (!entry.name.endsWith(".class")) continue
            val ins = mixinsJar.getInputStream(entry)
            val bytes = ins.readAllBytes()!!
            output.putNextEntry(JarEntry(entry.name))
            output.write(bytes)
        }

        val injectors = listOf(
            PrintDoAction(),
//            FakeMenu(),
            PrintMousePacket(),
            FakeClick(),
//            DisableInput(),
//            ShuffleMembers(),
        )

        for (entry in runeliteJar.entries()) {
            val ins = runeliteJar.getInputStream(entry)
            val bytes = if (entry.name.endsWith(".class")) {
                var classBytes = ins.readAllBytes()!!
                for (injector in injectors) {
                    val cr = ClassReader(classBytes)
                    val cn = ClassNode()
                    cr.accept(cn, 0)

                    injector.apply(cn)

                    val cw = LoadingClassWriter(classLoader, ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
                    cn.accept(cw)
                    classBytes = cw.toByteArray()
                }
                classBytes
            } else {
                runeliteJar.getInputStream(entry).readAllBytes()
            }!!

            output.putNextEntry(JarEntry(entry.name))
            output.write(bytes)
        }

        runeliteJar.close()
        output.close()
    }
}