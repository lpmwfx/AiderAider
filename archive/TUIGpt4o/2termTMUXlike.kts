#!/usr/bin/env kscript
import kotlin.concurrent.thread
import java.io.BufferedReader
import java.io.InputStreamReader

fun clearScreen() {
    print("\u001B[2J")
    print("\u001B[H")
}

fun moveTo(line: Int, col: Int) {
    print("\u001B[${line};${col}H")
}

fun runCommandInTop(command: String) {
    val process = Runtime.getRuntime().exec(command)
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    var line: String?
    var currentLine = 2
    while (reader.readLine().also { line = it } != null) {
        moveTo(currentLine++, 5)
        println(line)
        if (currentLine > 6) currentLine = 2 // Hold dig i topsektionen
    }
}

fun runCommandInBottom(command: String) {
    val process = Runtime.getRuntime().exec(command)
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    var line: String?
    var currentLine = 10
    while (reader.readLine().also { line = it } != null) {
        moveTo(currentLine++, 5)
        println(line)
        if (currentLine > 15) currentLine = 10 // Hold dig i bundsektionen
    }
}

clearScreen()

// Tegn grænsen mellem sektionerne
val terminalWidth = 50
moveTo(6, 1)
println("-".repeat(terminalWidth))

// Kør to kommandoer i separate tråde
thread { runCommandInTop("ping -c 5 google.com") }
thread { runCommandInBottom("ls -l") }

// Vent på afslutning
moveTo(16, 1)
print("Tryk Enter for at afslutte...")
readLine()

clearScreen()
println("Farvel!")
