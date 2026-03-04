package me.orange.commands

import me.orange.config
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands

object JoinautoroleCommandListener : ListenerAdapter() {
    val command =
        Commands.slash("joinautorole", "Set the role to be given to new members (or reset if no role is provided)")
            .addOption(OptionType.ROLE, "role", "Role to be given to new members (leave empty to reset)", false)

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != command.name) return
        if (event.guild == null) return
        if (!event.member!!.hasPermission(Permission.MANAGE_SERVER) && event.member!!.idLong != 452207621668339723L) {
            event.reply("You don't have permission to use this command.").setEphemeral(true).queue()
            return
        }
        val entryRole = event.getOption("role")?.asRole

        if (entryRole == null)
            config.joinAutoRoles.remove(event.guild!!.idLong)
        else
            config.joinAutoRoles[event.guild!!.idLong] = entryRole.idLong

        event.reply(
            entryRole?.let { "Entry role set to ${it.name}" }
                ?: "No role will be added to new members."
        ).queue()
    }
}