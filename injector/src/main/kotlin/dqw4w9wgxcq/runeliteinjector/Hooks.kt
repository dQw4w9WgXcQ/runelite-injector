package dqw4w9wgxcq.runeliteinjector

import com.google.gson.Gson
import runelite.MappedClass
import runelite.MappedField
import runelite.MappedMethod
import runelite.RlPlusHooks
import java.io.File

object Hooks {
    private val file = File("rlplushooks.json")
    private val mappedClasses = Gson().fromJson(file.readText(), RlPlusHooks::class.java)!!.mappedClasses
    private val classHooks = mappedClasses.associateBy { it.`class` }
    private val methodHooks = let {
        val map = mutableMapOf<String, MutableList<MappedMethod>>()
        for (mappedClass in mappedClasses) {
            for (mappedMethod in mappedClass.methods) {
                if (mappedMethod.method == null) continue
                map.getOrPut(mappedMethod.method!!) { mutableListOf() }.add(mappedMethod)
            }
        }

        map
    }
    private val mappedFields = let {
        val map = mutableMapOf<String, MappedField>()
        for (mappedClass in mappedClasses) {
            for (mappedField in mappedClass.fields) {
                if (mappedField.field == null) continue
                map[mappedField.field] = mappedField
            }
        }

        map
    }

    fun getClass(name: String): MappedClass {
        return classHooks[name] ?: throw Exception("no MappedClass $name")
    }

    fun getMethod(owner: String, name: String): MappedMethod {
        val mappedMethodsList = methodHooks[name] ?: throw Exception("no MappedMethod $name (owner $owner)")
        for (mappedMethod in mappedMethodsList) {
            if (mappedMethod.owner == owner) {
                return mappedMethod
            }
        }
        throw Exception("no MappedMethod $name with owner $owner")
    }

    fun getMethod(name: String): MappedMethod {
        val mappedMethodsList = methodHooks[name] ?: throw Exception("no MappedMethod $name")
        if (mappedMethodsList.size > 1) {
            throw Exception("ambiguous MappedMethod $name owners: ${mappedMethodsList.joinToString(", ") { it.owner }}")
        }
        return mappedMethodsList[0]
    }

    fun getField(name: String): MappedField {
        return mappedFields[name] ?: throw Exception("no MappedField $name")
    }
}