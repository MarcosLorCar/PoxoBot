package me.orange.commands

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

object VolatileCommandListener : ListenerAdapter() {
    const val NAME = "volatile"
    const val DESCRIPTION = "Enable volatile messages for this chat"

    val volatileChannels = mutableMapOf<Long, VolatileChannelData>()

    val command = Commands.slash(NAME, DESCRIPTION)
        .addSubcommands(
            SubcommandData("start", "Set behavior and automatic cancel timeout")
                .addOptions(
                    OptionData(OptionType.STRING, "mode", "Behaviour of volatile messages.", true)
                        .addChoice("Time 30s", "TIME_SHORT")
                        .addChoice("Time 5m", "TIME_LONG")
                        .addChoice("Await reaction", "REACTION"),
                    OptionData(OptionType.INTEGER, "timeout", "Hours till automatic cancel.", true)
                ),

            SubcommandData("stop", "Cancel volatile messages for this channel")
        )

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != NAME) return
        if (event.subcommandName == "stop") {
            volatileChannels.remove(event.channelIdLong)
            event.reply("Volatile mode stopped for this channel.")
                .setEphemeral(true)
                .queue()
            return
        }

        val startTime = System.currentTimeMillis()
        val type = event.getOption("mode")?.asString?.let { VolatileType.valueOf(it) } ?: return
        val timeout = event.getOption("timeout")?.asLong?.times(3600000) ?: 0
        volatileChannels[event.channelIdLong] = VolatileChannelData(type, timeout + startTime)

        event.reply("Volatile mode set to ${type.name} for ${event.getOption("timeout")?.asLong} hours.")
            .setEphemeral(true)
            .queue()
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.channel.idLong !in volatileChannels) return
        val data = volatileChannels[event.channel.idLong]!!
        if (System.currentTimeMillis() > data.timeout) {
            volatileChannels.remove(event.channel.idLong)
            return
        }
        if (data.type == VolatileType.REACTION) return

        CoroutineScope(Dispatchers.IO).launch {
            delay(data.type.time ?: 0)
            if (event.channel.idLong !in volatileChannels) return@launch
            event.message.delete().queue()
        }
    }

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        if (event.channel.idLong !in volatileChannels) return
        val data = volatileChannels[event.channel.idLong]!!
        if (System.currentTimeMillis() > data.timeout) {
            volatileChannels.remove(event.channel.idLong)
            return
        }
        if (data.type != VolatileType.REACTION) return

        event.retrieveMessage().queue { it.delete().queue() }
    }

    enum class VolatileType(val time: Long? = null) { TIME_SHORT(30 * 1000), TIME_LONG(5 * 60 * 1000), REACTION }

    data class VolatileChannelData(val type: VolatileType, val timeout: Long)
}