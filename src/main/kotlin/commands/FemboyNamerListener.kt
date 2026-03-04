package me.orange.commands

import me.orange.config
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object FemboyNamerListener : ListenerAdapter() {
    private val words: List<String> = this::class.java.classLoader
        .getResourceAsStream("names.txt")
        ?.bufferedReader()
        ?.readLines()
        ?.filter { it.isNotBlank() }
        ?: throw IllegalStateException("words.txt not found in resources!")

    init {
        println("Loaded ${words.size} words!")
    }

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val member = event.member
        val guild = event.guild

        // Ignore ElDiosLacteo
        if (member.user.idLong == 565160981949448212L) return

        val entryRole = config.joinAutoRoles[guild.idLong]?.let { guild.getRoleById(it) }

        val femboy = words.random().replaceFirstChar { it.uppercase() }
        member.modifyNickname("Femboy $femboy").queue()
        guild.addRoleToMember(member, entryRole ?: return).queue()
    }
}