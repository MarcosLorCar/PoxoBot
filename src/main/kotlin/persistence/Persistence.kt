package me.orange.persistence

import kotlinx.serialization.json.Json
import me.orange.Config
import me.orange.config
import java.io.File

object Persistence {
    private val file = File("data/state.json")
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun loadConfig() {
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
            config = Config()
        }
        config = try {
            json.decodeFromString<Config>(file.readText())
        } catch (e: Exception) {
            println("Error loading state, starting fresh: ${e.message}")
            Config()
        }
    }

    fun saveConfig() {
        val content = json.encodeToString(config)
        file.writeText(content)
    }
}