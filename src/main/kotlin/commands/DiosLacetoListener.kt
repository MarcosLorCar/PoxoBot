package me.orange.commands

import me.orange.Config
import me.orange.config
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.UserSnowflake
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.pow
import kotlin.random.Random

object DiosLacetoListener : ListenerAdapter() {
    private val activeTimer: AtomicReference<Timer?> = AtomicReference(null)

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val member = event.member
        val guild = event.guild
        if (member.user.idLong != Config.DIOSLACETO_ID) return

        guild.modifyNickname(member, "Femboy Laceto").queue()
        val entryRole = config.joinAutoRoles[guild.idLong]?.let { guild.getRoleById(it) }
        guild.addRoleToMember(member, entryRole ?: return).queue()
        val channel = guild.systemChannel
        config.lacetoJoinCounter++
        channel?.sendMessage("Dios Lacteo se ha unido al servidor ${config.lacetoJoinCounter} ${if (config.lacetoJoinCounter > 1) "veces" else "vez"}")?.queue()

        scheduleKickTask(guild)
    }

    val command = Commands.slash("lacetotimer", "Dios Lacteo")

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != "lacetotimer") return
        if (event.guild == null) return
        if (event.member!!.idLong != Config.ORANGE_ID) return

        scheduleKickTask(event.guild!!)
        event.reply("Se ha activado el timer").queue()
    }

    private fun scheduleKickTask(
        guild: Guild,
    ) {
        activeTimer.getAndSet(null)?.cancel()
        val timer = Timer().also { activeTimer.set(it) }
        val hour = 60L * 60L * 1000L
        timer.scheduleAtFixedRate(
            DiosLacetoKickTask(guild),
            hour,
            hour
        )
    }

    private class DiosLacetoKickTask(
        private val guild: Guild,
    ) : TimerTask() {
        private val channel: TextChannel? = guild.systemChannel
        private var counter = 0

        override fun run() {
            val kicked = Random.nextInt(4)
            if (kicked == 0) {
                guild.kick(UserSnowflake.fromId(Config.DIOSLACETO_ID)).queue()
                channel?.sendMessage("Dios Lacteo fue kickeado.")?.queue()
                activeTimer.getAndSet(null)?.cancel()
            } else {
                counter++
                val percentage = 0.75f.pow(counter) * 100f
                channel?.sendMessage("Dios Lacteo sobrevivió una hora mas. ($percentage%)")?.queue()
                println(kicked)
            }
        }
    }
}