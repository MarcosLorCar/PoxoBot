package me.orange

import me.orange.commands.RegisterCommandListener
import me.orange.commands.VolatileCommandListener
import net.dv8tion.jda.api.JDABuilder

fun main() {
    val token = System.getenv("DISCORD_BOT_TOKEN") ?: error("Missing token!")
    val jda = JDABuilder.createDefault(token)
        .build()

    jda.addEventListener(RegisterCommandListener)
    jda.addEventListener(VolatileCommandListener)
}