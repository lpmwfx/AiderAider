#!/usr/bin/env kscript

fun clearScreen() {
    print("\u001B[2J") // Ryd hele skærmen
    print("\u001B[H")  // Flyt cursoren til (1,1)
}

fun drawThreeSections() {
    clearScreen()
    val height = 20 // Eksempel på terminalhøjde
    val width = 50  // Eksempel på terminalbredde
    val sectionHeight = height / 3

    // Top sektion
    print("\u001B[1;1H") // Flyt til øverste venstre hjørne
    println("-".repeat(width))
    for (i in 2 until sectionHeight) {
        println("|${" ".repeat(width - 2)}|")
    }
    println("-".repeat(width))

    // Midt sektion
    print("\u001B[${sectionHeight + 1};1H") // Flyt til starten af midten
    println("-".repeat(width))
    for (i in (sectionHeight + 2) until sectionHeight * 2) {
        println("|${" ".repeat(width - 2)}|")
    }
    println("-".repeat(width))

    // Bund sektion
    print("\u001B[${sectionHeight * 2 + 1};1H") // Flyt til starten af bunden
    println("-".repeat(width))
    for (i in (sectionHeight * 2 + 2) until height) {
        println("|${" ".repeat(width - 2)}|")
    }
    println("-".repeat(width))
}

drawThreeSections()
