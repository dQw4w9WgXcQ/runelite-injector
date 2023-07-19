package dqw4w9wgxcq.runeliteinjector

object MixinHooks {
    private val mixinHooks =
        mapOf(
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
            "getFakeX" to "gfx",
            "getFakeY" to "gfy",
            "getFakeButton" to "gfb",
        )

    fun <T> get(name: String): T {
        @Suppress("UNCHECKED_CAST") return mixinHooks[name] as T
    }
}