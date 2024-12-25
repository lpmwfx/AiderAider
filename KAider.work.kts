// Dette er en arbejdsfil med vores tidligere kode og ideer

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

/* UI mockup fra tidligere:
┌─ KAider v1.0 ────────────────────────────────────────────┐
│                                                          │
│ Files          Chat                      Preview         │
│ ├─ src/        > Hvordan kan jeg hjælpe? #!/usr/bin/env │
│ ├─ KAider.kt   /add KAi                  kotlin         │
│ └─ chat.log    [Matches:]                               │
│                KAider.main.kts                          │
│                                                         │
└─────────────────────────────────────────────────────────┘
AiderAider>                                        ?:help
*/

// TODO: Features vi vil implementere:
// 1. Fil completion med @ og TAB
// 2. Kommando completion med /
// 3. Preview af valgte filer
// 4. Aider integration
// 5. Chat historik
// 6. Status bar med aktiv fil 