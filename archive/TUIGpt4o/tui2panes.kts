#!/usr/bin/env kscript

fun clearScreen() {
    print("\u001B[2J") // Ryd hele skærmen
    print("\u001B[H")  // Flyt cursoren til (1,1)
}

fun drawTwoSections() {
    clearScreen()
    val height = 20 // Eksempel på terminalhøjde
    val width = 50  // Eksempel på terminalbredde

    // Øverste sektion
    print("\u001B[1;1H") // Flyt til øverste venstre hjørne
    println("-".repeat(width))
    for (i in 2 until height / 2) {
        println("|${" ".repeat(width - 2)}|")
    }
    println("-".repeat(width))

    // Nederste sektion
    print("\u001B[${height / 2 + 1};1H") // Flyt til midten
    println("-".repeat(width))
    for (i in (height / 2 + 2) until height) {
        println("|${" ".repeat(width - 2)}|")
    }
    println("-".repeat(width))
}

drawTwoSections()
