#!/usr/bin/env kscript

fun clearScreen() {
    print("\u001B[2J") // Ryd hele skærmen
    print("\u001B[H")  // Flyt cursoren til (1,1)
}

fun moveTo(line: Int, col: Int) {
    print("\u001B[${line};${col}H") // Flyt cursoren til linje og kolonne
}

fun setColor(colorCode: Int) {
    print("\u001B[${colorCode}m") // Skift tekstfarve
}

fun resetColor() {
    print("\u001B[0m") // Nulstil farve til standard
}

fun drawMenu() {
    clearScreen()
    setColor(34) // Blå farve
    moveTo(2, 5)
    println("Velkommen til mit TUI!")
    resetColor()

    moveTo(4, 5)
    println("1. Vis en besked")
    moveTo(5, 5)
    println("2. Afslut")
}

fun mainLoop() {
    var running = true
    while (running) {
        drawMenu()
        moveTo(7, 5)
        print("Vælg en mulighed: ")
        val input = readLine()?.trim()

        when (input) {
            "1" -> {
                clearScreen()
                moveTo(3, 5)
                println("Du valgte at vise en besked!")
                moveTo(5, 5)
                print("Tryk på Enter for at vende tilbage...")
                readLine()
            }
            "2" -> {
                running = false
                clearScreen()
                println("Farvel!")
            }
            else -> {
                moveTo(9, 5)
                println("Ugyldigt valg, prøv igen.")
                Thread.sleep(1000) // Vent 1 sekund
            }
        }
    }
}

clearScreen()
mainLoop()
