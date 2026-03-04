package me.orange.commands

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object DiosLacetoListener : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val member = event.member
        val guild = event.guild

        guild.modifyNickname(member, "Femboy Laceto")
        val channel = guild.getTextChannelById(1376936186085511168)
        channel?.sendMessage("Dios Lacteo será kickeado en 24h.")?.queue()

        CoroutineScope(Dispatchers.Default).launch {
            delay(24 * 60 * 60 * 1000)

            member.user.openPrivateChannel().queue {
                it.sendMessage("Has sido automaticamente kickeado de Malos ( ͡❛ ͜ʖ ͡❛)")
            }

            guild.kick(member).queue()
        }
    }
}