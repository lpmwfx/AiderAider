#!/usr/bin/env kotlin

fun clearScreen() {
    print("\u001b[H\u001b[2J")
    System.out.flush()
}

fun moveTo(x: Int, y: Int) {
    print("\u001b[${y};${x}H")
}

fun displayFileTree() {
    moveTo(1, 1)
    println("Files")
    moveTo(1, 2)
    println("├── ExampleFile1.txt")
    moveTo(1, 3)
    println("├── ExampleFile2.txt")
}

fun displayMenu() {
    moveTo(40, 1)
    println("KAider TUI")
    moveTo(40, 2)
    println("-".repeat(20))
    moveTo(40, 3)
    println("1. Show Text")
    moveTo(40, 4)
    println("2. Show Output")
    moveTo(40, 5)
    println("3. Exit")
    moveTo(40, 6)
    print("Choose an option: ")
}

fun updateContent(content: String) {
    moveTo(1, 5)
    println(content.chunked(30).joinToString("\n"))
    moveTo(1, 10)
    println("Press any key to return to menu...")
    readLine()
}

clearScreen()
displayFileTree()
displayMenu()

while (true) {
    moveTo(40, 6)
    print("Choose an option: ")
    val choice = readLine()
    when (choice) {
        "1" -> updateContent("Lorem Ipsum\nDolor sit amet, consectetur adipiscing elit.\nSed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
        "2" -> updateContent("Output\nVelkommen til KAider!\nHvordan kan jeg hjælpe?")
        "3" -> {
            println("Exiting...")
            break
        }
        else -> println("Invalid option, please try again.")
    }
}
