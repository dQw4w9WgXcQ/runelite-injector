package dqw4w9wgxcq.runeliteinjector

import com.google.gson.Gson
import runelite.MappedClass
import runelite.MappedField
import runelite.MappedMethod
import java.io.File

object Hooks {
    private val file = File("rlplushooks.json")
    private val arr = Gson().fromJson(file.readText(), Array<MappedClass>::class.java)
    private val mappedClasses = arr.associateBy { it.`class` }
    private val mappedMethods = let {
        val map = mutableMapOf<String, MutableList<MappedMethod>>()
        for (mappedClass in arr) {
            for (mappedMethod in mappedClass.methods) {
                if (mappedMethod.method == null) continue
                println("mappedMethod: $mappedMethod")
                map.getOrPut(mappedMethod.method!!) { mutableListOf() }.add(mappedMethod)
            }
        }

        println("mappedMethods ${map.keys.size}")
        map
    }
    private val mappedFields = let {
        val map = mutableMapOf<String, MappedField>()
        for (mappedClass in arr) {
            for (mappedField in mappedClass.fields) {
                if (mappedField.field == null) continue
                println("mappedField: $mappedField")
                map[mappedField.field] = mappedField
            }
        }

        println("mappedFields ${map.keys.size}")
        map
    }

    fun getClass(name: String): MappedClass {
        return mappedClasses[name] ?: throw Exception("no MappedClass $name")
    }

    fun getMethod(owner: String, name: String): MappedMethod {
        val mappedMethodsList = mappedMethods[name] ?: throw Exception("no MappedMethod $name (owner $owner)")
        for (mappedMethod in mappedMethodsList) {
            if (mappedMethod.owner == owner) {
                return mappedMethod
            }
        }
        throw Exception("no MappedMethod $name with owner $owner")
    }

    fun getMethod(name: String): MappedMethod {
        val mappedMethodsList = mappedMethods[name] ?: throw Exception("no MappedMethod $name")
        if (mappedMethodsList.size > 1) {
            throw Exception("ambiguous MappedMethod $name owners: ${mappedMethodsList.joinToString(", ") { it.owner }}")
        }
        return mappedMethodsList[0]
    }

    fun getField(name: String): MappedField {
        return mappedFields[name] ?: throw Exception("no MappedField $name")
    }
}