package runelite

data class MappedMethod(
    var method: String?,
    var owner: String,
    var name: String,
    var access: Int,
    var parameters: List<String>,
    var descriptor: String,
    var garbageValue: String,
//    var lineNumbers: List<Int>,
//    var fieldGets: Map<Field, Int>,
//    var fieldPuts: Map<Field, Int>,
//    var dependencies: Map<String, Int>,
)