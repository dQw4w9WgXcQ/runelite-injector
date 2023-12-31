package dqw4w9wgxcq.runeliteinjector

import dqw4w9wgxcq.runeliteinjector.injectors.FakeClick
import dqw4w9wgxcq.runeliteinjector.injectors.FakeMenu
import dqw4w9wgxcq.runeliteinjector.injectors.PrintDoAction
import dqw4w9wgxcq.runeliteinjector.injectors.PrintMousePacket
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
    private val log = LoggerFactory.getLogger(this::class.java)

    private val projectVersion = System.getProperty("projectVersion") ?: throw Exception("projectVersion not set")
    private val rlVersion = System.getProperty("rlVersion") ?: throw Exception("rlVersion not set")

    @JvmStatic
    fun main(args: Array<String>) {
        log.info("projectVersion: $projectVersion rlVersion: $rlVersion")

        val patchedFile = Path.of(System.getProperty("user.home"), ".runelite", "cache", "patched.cache").toFile()
        if (!patchedFile.exists()) throw Exception("patched.cache not found")

        val runeliteApiFile = Path.of(
            System.getProperty("user.home"),
            ".runelite",
            "repository2",
            "runelite-api-$rlVersion-runtime.jar"
        ).toFile()
        if (!runeliteApiFile.exists()) throw Exception("runelite-api not found")

        val mixinsFile = File(
            File(System.getProperty("user.dir")).parentFile,
            "mixins/build/libs/mixins-$projectVersion.jar"
        )
        if (!mixinsFile.exists()) throw Exception("mixins not found in ${mixinsFile.absolutePath}")

        val outFile = Path.of(System.getProperty("user.home"), "NovaLite", "cache", "patched.cache").toFile()
        if (outFile.exists()) {
            log.info("Deleting old patched.cache")
            outFile.delete()
        }

        val runeliteJar = JarFile(patchedFile)

        //load classes necessary for COMPUTE_FRAMES
        val classLoader = URLClassLoader(
            arrayOf(patchedFile.toURI().toURL(), runeliteApiFile.toURI().toURL(), mixinsFile.toURI().toURL()),
            this.javaClass.classLoader
        )
        for (entry in runeliteJar.entries()) {
            if (entry.name.endsWith(".class")) {
                Class.forName(entry.name.replace("/", ".").removeSuffix(".class"), false, classLoader)
            }
        }

        val out = JarOutputStream(outFile.outputStream())

        //add mixins
        val mixinsJar = JarFile(mixinsFile)
        for (entry in mixinsJar.entries()) {
            if (!entry.name.endsWith(".class")) continue
            val ins = mixinsJar.getInputStream(entry)
            val bytes = ins.readAllBytes()!!
            out.putNextEntry(JarEntry(entry.name))
            out.write(bytes)
        }

        val injectors = listOf(
//            PrintDoAction(),
//            FakeMenu(),
            PrintMousePacket(),
//            FakeClick(),
        )

        for (entry in runeliteJar.entries()) {
            val ins = runeliteJar.getInputStream(entry)

            var bytes = ins.readAllBytes()!!

            if (entry.name.endsWith(".class")) {
                for (injector in injectors) {
                    val cr = ClassReader(bytes)
                    val cn = ClassNode()
                    cr.accept(cn, 0)

                    if (injector.inject(cn)) {
                        val cw = LoadingClassWriter(classLoader, ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
                        cn.accept(cw)
                        bytes = cw.toByteArray()
                    }
                }
            }

            out.putNextEntry(JarEntry(entry.name))
            out.write(bytes)
        }

        runeliteJar.close()
        out.close()
    }
}