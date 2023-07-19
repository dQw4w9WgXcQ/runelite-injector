package runelite

data class MappedField(
    val field: String?,
    val owner: String,
    val name: String,
    val access: Int,
    val descriptor: String,
    val decoder: Number?,
//    val puts: Map<Method, Int>,
//    val gets: Map<Method, Int>,
)