@file:DependsOn("com.github.ajalt.mordant:mordant:2.2.0")

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.widgets.*

class KAiderTUI {
    private val terminal = Terminal()
    
    fun start() {
        try {
            drawLayout()
            mainLoop()
        } finally {
            cleanup()
        }
    }
    
    private fun cleanup() {
        terminal.cursor.show()
        terminal.clear()
    }
    
    private fun mainLoop() {
        while (true) {
            drawLayout()
            val input = terminal.readLineOrNull() ?: break
            if (input == "q") break
        }
    }
    
    private fun drawLayout() {
        terminal.clear()
        
        val layout = Panel(
            grid {
                padding = Padding(1)
                
                row {
                    cell(TextColors.brightGreen("Files")) {
                        width = WidthRange(20)
                    }
                    cell(TextColors.brightGreen("Chat")) {
                        width = WidthRange(40)
                    }
                    cell(TextColors.brightGreen("Preview")) {
                        width = WidthRange(20)
                    }
                }
            },
            title = TextColors.green("KAider v1.0"),
            borderStyle = BorderStyle.ROUNDED
        )
        
        terminal.println(layout)
    }
}

fun main() {
    KAiderTUI().start()
} 