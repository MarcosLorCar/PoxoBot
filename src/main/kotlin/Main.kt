package me.orange

import me.orange.commands.FemboyNamerListener
import me.orange.commands.RegisterCommandListener
import me.orange.commands.VolatileCommandListener
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent

fun main() {
    val token = System.getenv("DISCORD_BOT_TOKEN") ?: error("Missing token!")
    val jda = JDABuilder.createDefault(token)
        .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
        .build()

    jda.addEventListener(RegisterCommandListener)
    jda.addEventListener(VolatileCommandListener)
    jda.addEventListener(FemboyNamerListener)
}