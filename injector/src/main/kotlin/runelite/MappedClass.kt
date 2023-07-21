package runelite

import com.google.gson.annotations.SerializedName

data class MappedClass(
    @SerializedName("class")
    val `class`: String,
    val name: String,
    @SerializedName("super")
    val `super`: String,
    val access: Int,
    val interfaces: List<String>,
    val fields: List<MappedField>,
    val methods: List<MappedMethod>,
    val constructors: List<MappedMethod>,
) {
    fun getMethod(name: String): MappedMethod {
        return methods.firstOrNull { it.method == name } ?: throw Exception("Method $name not found in class $`class`")
    }

    fun getField(name: String): MappedField {
        return fields.firstOrNull { it.field == name } ?: throw Exception("Field $name not found in class $`class`")
    }
}