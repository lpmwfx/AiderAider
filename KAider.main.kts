#!/usr/bin/env kotlin

@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

import kotlinx.coroutines.*
import kotlin.io.path.*

// Original file handling
class FileHandler {
    private val workDir = Path(System.getProperty("user.dir"))
    private val files = mutableListOf<String>()
    
    fun updateFileList() {
        files.clear()
        workDir.listDirectoryEntries().forEach { path ->
            files += path.name
        }
    }
    
    fun findMatches(prefix: String): List<String> {
        return files.filter { 
            it.startsWith(prefix, ignoreCase = true) 
        }
    }
}

// Original command handling
class CommandHandler {
    fun handleCommand(cmd: String) {
        when {
            cmd.startsWith("/add ") -> handleAdd(cmd.removePrefix("/add "))
            cmd.startsWith("/edit ") -> handleEdit(cmd.removePrefix("/edit "))
            cmd.startsWith("/drop ") -> handleDrop(cmd.removePrefix("/drop "))
            cmd.startsWith("/cat ") -> handleCat(cmd.removePrefix("/cat "))
            cmd.startsWith("@") -> handleFileCompletion(cmd.removePrefix("@"))
        }
    }
    
    private fun handleAdd(file: String) {
        // TODO: Implementer fil tilføjelse
    }
    
    private fun handleEdit(file: String) {
        // TODO: Implementer fil redigering
    }
    
    private fun handleDrop(file: String) {
        // TODO: Implementer fil fjernelse
    }
    
    private fun handleCat(file: String) {
        // TODO: Implementer fil visning
    }
    
    private fun handleFileCompletion(prefix: String) {
        // TODO: Implementer fil completion
    }
}

// Aider process handling
class AiderProcess {
    private val process = ProcessBuilder()
        .command(
            "aider",
            "--chat-language", "da",       // Dansk sprog
            "--no-browser",                // Terminal-only mode
            "--chat-history-file", "aider-chat.log",  // Chat log
            "--pretty",                    // Pæn formatering
            "--stream",                    // Stream output
            "--dark-mode",                 // Mørkt tema
            "."                            // Aktuel mappe
        )
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
        
    fun sendCommand(cmd: String) {
        process.outputStream.writer().use {
            it.write("$cmd\n")
            it.flush()
        }
    }
    
    fun cleanup() {
        process.destroy()
    }
}

fun main() {
    println("KAider v1.0 - Velkommen!")
    val fileHandler = FileHandler()
    val commandHandler = CommandHandler()
    val aiderProcess = AiderProcess()
    
    try {
        while (true) {
            print("KAider> ")
            val input = readLine() ?: break
            
            if (input == "exit") break
            
            commandHandler.handleCommand(input)
            aiderProcess.sendCommand(input)
        }
    } finally {
        aiderProcess.cleanup()
    }
} 