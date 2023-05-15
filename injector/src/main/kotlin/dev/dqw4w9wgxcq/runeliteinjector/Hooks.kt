package dev.dqw4w9wgxcq.runeliteinjector

object Hooks {
    private val names = mapOf(
        "packetWriter" to "in",
        "doCycleLoggedIn" to "yq",
        "doActionClassName" to "ar",
        "doActionMethodName" to "ke",
        "updateRootInterface" to "mt",
        "updateRootInterfaceOwner" to "bj",
        "updateRootInterfaceDesc" to "(IIIIIIII)V",
        "lastButtonMult" to -1222491879,

        "Mixins" to "rl01",
        "menuThingMethodName" to "mmm",
        "widgetThingMethodName" to "www",
        "disableInput" to "dii",
        "mouseRecorderWrite" to "mrw",
        "mouseRecorderSend" to "mrs",
        "mouseClickPacket" to "mcp",
        "button1" to "bb1",
        "button0" to "bb0",
        "fakeMouseRecorder" to "fmr",
        "fakeMouseClick" to "fmc",
    )

    fun <T> get(name: String): T {
        @Suppress("UNCHECKED_CAST")
        return names[name]!! as T
    }
}
