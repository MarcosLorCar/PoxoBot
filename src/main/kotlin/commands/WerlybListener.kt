package me.orange.commands

import me.orange.Config
import me.orange.config
import me.orange.jda
import net.dv8tion.jda.api.entities.UserSnowflake
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.pow
import kotlin.random.Random

object WerlybListener : ListenerAdapter() {
    private val activeTimer: AtomicReference<Timer?> = AtomicReference(null)
    val command = Commands.slash("werlyb", "Werlyb")

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != "werlyb") return
        event.reply("!").queue()

        val twentyMinutes = 1000L * 60L * 20L

        config.werlybCounter = 0
        Timer().also { activeTimer.set(it) }.schedule(WerlybTask, twentyMinutes, twentyMinutes)
    }

    private object WerlybTask : TimerTask() {
        val malos = jda.getGuildById(Config.MALOS_ID)

        override fun run() {
            malos?.getMemberById(Config.WERLYB_ID) ?: run {
                config.werlybCounter = 0
                activeTimer.getAndSet(null)?.cancel()
            }
            config.werlybCounter++
            if (Random.nextFloat() < config.werlybPercentage) {
                malos?.kick(UserSnowflake.fromId(Config.WERLYB_ID))

                activeTimer.getAndSet(null)?.cancel()
                malos?.systemChannel?.sendMessage("Werlyb fue kickeado. (${(1 - (1 - config.werlybPercentage).pow(config.werlybCounter)).times(100)}%)")?.queue()
            }
        }
    }
}