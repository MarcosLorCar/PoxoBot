package me.orange.commands

import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands

object FemboyNamerListener : ListenerAdapter() {
    private val words: List<String> = this::class.java.classLoader
        .getResourceAsStream("names.txt")
        ?.bufferedReader()
        ?.readLines()
        ?.filter { it.isNotBlank() }
        ?: throw IllegalStateException("words.txt not found in resources!")

    var entryRole: Role? = null

    init {
        println("Loaded ${words.size} words!")
    }

    val command =
        Commands.slash("joinautorole", "Set the role to be given to new members (or reset if no role is provided)")
            .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
            .addOption(OptionType.ROLE, "role", "Role to be given to new members (leave empty to reset)", false)

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val member = event.member
        val guild = event.guild

        val femboy = words.random().replaceFirstChar { it.uppercase() }
        member.modifyNickname("Femboy $femboy").queue()
        guild.addRoleToMember(member, entryRole ?: return).queue()
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != command.name) return
        entryRole = event.getOption("role")?.asRole

        event.reply(
            entryRole?.let { "Entry role set to ${it.name}" }
                ?: "No role will be added to new members."
        ).queue()
    }
}