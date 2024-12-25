#!/usr/bin/env kscript

fun clearScreen() {
    print("\u001B[2J") // Ryd hele skærmen
    print("\u001B[H")  // Flyt cursoren til (1,1)
}

fun drawFourSections() {
    clearScreen()
    val height = 20 // Eksempel på terminalhøjde
    val width = 50  // Eksempel på terminalbredde
    val sectionHeight = height / 2
    val sectionWidth = width / 2

    // Øverste venstre
    for (i in 1..sectionHeight) {
        print("\u001B[${i};1H") // Flyt cursoren til venstre side
        println("|${" ".repeat(sectionWidth - 1)}|")
    }

    // Øverste højre
    for (i in 1..sectionHeight) {
        print("\u001B[${i};${sectionWidth + 1}H") // Flyt cursoren til højre side
        println("|${" ".repeat(sectionWidth - 1)}|")
    }

    // Nederste venstre
    for (i in (sectionHeight + 1)..height) {
        print("\u001B[${i};1H") // Flyt cursoren til venstre side
        println("|${" ".repeat(sectionWidth - 1)}|")
    }

    // Nederste højre
    for (i in (sectionHeight + 1)..height) {
        print("\u001B[${i};${sectionWidth + 1}H") // Flyt cursoren til højre side
        println("|${" ".repeat(sectionWidth - 1)}|")
    }
}

drawFourSections()
