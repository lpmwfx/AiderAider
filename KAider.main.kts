#!/usr/bin/env kotlin

fun main() {
    val workDir = System.getProperty("user.dir")
    
    // Start Aider med minimale features
    ProcessBuilder()
        .command(
            "aider",
            "--version",                   // Vis version info
            "--chat-language", "da",       // Dansk sprog
            "--no-browser",                // Terminal-only mode
            "--chat-history-file", "aider-chat.log",  // Chat log
            "--pretty",                    // Pæn formatering
            "--stream",                    // Stream output
            "--dark-mode",                 // Mørkt tema
            "--fancy-input",               // Bedre input håndtering
            "--show-prompts",              // Vis prompts
            "--verbose",                   // Debug info
            "."                            // Aktuel mappe
        )
        .directory(java.io.File(workDir))
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .redirectInput(ProcessBuilder.Redirect.INHERIT)
        .start()
        .waitFor()
}

main() 