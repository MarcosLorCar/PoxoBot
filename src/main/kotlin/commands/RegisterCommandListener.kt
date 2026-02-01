package me.orange.commands

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object RegisterCommandListener : ListenerAdapter() {
    override fun onGuildJoin(event: GuildJoinEvent) {
        registerCommands(event.guild)
    }

    override fun onGuildReady(event: GuildReadyEvent) {
        println("Guild ready: ${event.guild.name}")
        registerCommands(event.guild)
    }

    private fun registerCommands(guild: Guild) {
        val updateCommands = guild.updateCommands()

        updateCommands.addCommands(VolatileCommandListener.command)

        updateCommands.queue()
    }
}