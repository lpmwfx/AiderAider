#!/usr/bin/env kotlin

fun clearScreen() {
    print("\u001b[H\u001b[2J")
    System.out.flush()
}

fun displayMenu() {
    clearScreen()
    println("KAider TUI")
    println("-".repeat(80))
    println("1. Show Files")
    println("2. Show Output")
    println("3. Exit")
    print("Choose an option: ")
}

fun showFiles() {
    println("Files")
    println("├── ExampleFile1.txt")
    println("├── ExampleFile2.txt")
    println("Press any key to return to menu...")
    readLine()
}

fun showOutput() {
    println("Output")
    println("Velkommen til KAider!")
    println("Hvordan kan jeg hjælpe?")
    println("Press any key to return to menu...")
    readLine()
}

while (true) {
    displayMenu()
    val choice = readLine()
    when (choice) {
        "1" -> showFiles()
        "2" -> showOutput()
        "3" -> {
            println("Exiting...")
            break
        }
        else -> println("Invalid option, please try again.")
    }
}
