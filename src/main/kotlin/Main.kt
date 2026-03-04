package me.orange

import me.orange.commands.DiosLacetoListener
import me.orange.persistence.Persistence
import me.orange.commands.FemboyNamerListener
import me.orange.commands.JoinautoroleCommandListener
import me.orange.commands.RegisterCommandListener
import me.orange.commands.VolatileCommandListener
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent

lateinit var config: Config

fun main() {
    Persistence.loadConfig()

    Runtime.getRuntime().addShutdownHook(Thread {
        Persistence.saveConfig()
    })

    val token = System.getenv("DISCORD_BOT_TOKEN") ?: error("Missing token!")
    val jda = JDABuilder.createDefault(token)
        .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
        .build()

    jda.addEventListener(RegisterCommandListener)
    jda.addEventListener(VolatileCommandListener)
    jda.addEventListener(JoinautoroleCommandListener)
    jda.addEventListener(FemboyNamerListener)
    jda.addEventListener(DiosLacetoListener)
}