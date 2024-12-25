#!/usr/bin/env kotlin

import java.io.File
import kotlin.io.path.*

// ANSI escape codes
object ANSI {
    const val RESET = "\u001B[0m"
    const val GREEN = "\u001B[32m"
    const val GRAY = "\u001B[90m"
    const val CLEAR = "\u001B[2J"
    const val HOME = "\u001B[H"
    const val HIDE_CURSOR = "\u001B[?25l"
    const val SHOW_CURSOR = "\u001B[?25h"
    const val HIGHLIGHT = "\u001B[7m"  // Invers video for highlight
    fun moveTo(x: Int, y: Int) = "\u001B[${y};${x}H"
}

// Terminal helpers
object Terminal {
    fun getWidth(): Int {
        val process = ProcessBuilder("tput", "cols").inheritIO().start()
        return process.inputStream.bufferedReader().readLine().toInt()
    }
    
    fun getHeight(): Int {
        val process = ProcessBuilder("tput", "lines").inheritIO().start()
        return process.inputStream.bufferedReader().readLine().toInt()
    }
    
    fun clear() = print(ANSI.CLEAR + ANSI.HOME)
    fun hideCursor() = print(ANSI.HIDE_CURSOR)
    fun showCursor() = print(ANSI.SHOW_CURSOR)
    
    // Raw mode for tastatur input
    fun enableRawMode() {
        ProcessBuilder("stty", "raw", "-echo").inheritIO().start().waitFor()
    }
    
    fun disableRawMode() {
        ProcessBuilder("stty", "-raw", "echo").inheritIO().start().waitFor()
    }
    
    fun readChar(): Char {
        return System.`in`.read().toChar()
    }
}

// Fil system model
data class FileNode(
    val name: String,
    val path: Path,
    val isDirectory: Boolean,
    var isExpanded: Boolean = false,
    var depth: Int = 0,
    var children: List<FileNode> = emptyList()
)

data class VisibleNode(
    val node: FileNode,
    val prefix: String,  // Tree characters: "├──", "└──", etc.
    val indent: String   // Spaces for indentation
)

class KAiderTUI {
    private val width = Terminal.getWidth()
    private val height = Terminal.getHeight()
    private val fileTreeWidth = width / 4
    
    private var activePanel = 0  // 0 = filtræ, 1 = KAider output
    private var selectedFileIndex = 0
    private var fileTree = buildFileTree(Path("."), 0)
    private var visibleFiles = mutableListOf<VisibleNode>()
    private var searchMode = false
    private var searchBuffer = StringBuilder()
    private var searchResults = listOf<Path>()
    private var selectedSearchResult = 0
    private var showingSearchResults = false
    private var previewFile: Path? = null
    
    private fun buildFileTree(path: Path, depth: Int): FileNode {
        val file = path.toFile()
        return FileNode(
            name = file.name.ifEmpty { "." },
            path = path,
            isDirectory = file.isDirectory,
            isExpanded = false,
            depth = depth,
            children = if (file.isDirectory) {
                file.listFiles()
                    ?.sortedWith(compareBy({ !it.isDirectory }, { it.name }))
                    ?.map { buildFileTree(it.toPath(), depth + 1) }
                    ?: emptyList()
            } else emptyList()
        )
    }
    
    private fun updateVisibleFiles(node: FileNode, isLast: Boolean = true, parentPrefix: String = "") {
        val indent = "    ".repeat(node.depth)
        val prefix = when {
            node.depth == 0 -> ""
            isLast -> "└──"
            else -> "├──"
        }
        
        visibleFiles.add(VisibleNode(node, prefix, parentPrefix))
        
        if (node.isExpanded && node.isDirectory) {
            node.children.forEachIndexed { index, child ->
                val isChildLast = index == node.children.size - 1
                val newParentPrefix = parentPrefix + if (isLast) "    " else "│   "
                updateVisibleFiles(child, isChildLast, newParentPrefix)
            }
        }
    }
    
    private fun searchFiles(query: String): List<Path> {
        return Path(".").walk()
            .filter { it.nameWithoutExtension.contains(query, ignoreCase = true) }
            .toList()
    }
    
    private fun drawSearchResults() {
        // Overskrift
        print(ANSI.moveTo(fileTreeWidth + 4, 4) + 
              ANSI.GREEN + "Søgeresultater for: " + ANSI.RESET + searchBuffer)
        
        // Resultater
        searchResults.forEachIndexed { index, path ->
            val highlight = if (index == selectedSearchResult) ANSI.HIGHLIGHT else ""
            val icon = if (path.toFile().isDirectory) "📁" else "📄"
            print(ANSI.moveTo(fileTreeWidth + 4, 6 + index) + highlight +
                  "$icon ${path.toString()}" + ANSI.RESET)
        }
        
        if (searchResults.isEmpty()) {
            print(ANSI.moveTo(fileTreeWidth + 4, 6) + 
                  ANSI.GRAY + "Ingen resultater fundet" + ANSI.RESET)
        }
    }
    
    private fun drawFilePreview() {
        previewFile?.let { path ->
            if (!path.toFile().isDirectory) {
                print(ANSI.moveTo(fileTreeWidth + 4, 4) + 
                      ANSI.GREEN + "Preview: " + ANSI.RESET + path.toString())
                
                try {
                    val lines = path.toFile().readLines()
                    val maxLines = height - 8  // Giv plads til header og status bar
                    lines.take(maxLines).forEachIndexed { index, line ->
                        print(ANSI.moveTo(fileTreeWidth + 4, 6 + index) + line)
                    }
                    if (lines.size > maxLines) {
                        print(ANSI.moveTo(fileTreeWidth + 4, height - 3) + 
                              ANSI.GRAY + "... (${lines.size - maxLines} flere linjer)" + ANSI.RESET)
                    }
                } catch (e: Exception) {
                    print(ANSI.moveTo(fileTreeWidth + 4, 6) + 
                          ANSI.GRAY + "Kunne ikke læse filen: ${e.message}" + ANSI.RESET)
                }
            }
        }
    }
    
    private fun navigateToFile(path: Path) {
        // Find stien til filen
        val parts = path.normalize().toString().split(File.separator)
        var current = fileTree
        
        // Åbn alle mapper på vejen
        parts.forEach { part ->
            if (part != ".") {
                current.children.find { it.name == part }?.let { node ->
                    if (node.isDirectory && !node.isExpanded) {
                        node.isExpanded = true
                    }
                    current = node
                }
            }
        }
        
        // Opdater visning
        visibleFiles.clear()
        updateVisibleFiles(fileTree)
        
        // Find og vælg filen i listen
        visibleFiles.indexOfFirst { it.node.path == path }
            .takeIf { it >= 0 }
            ?.let { selectedFileIndex = it }
        
        // Vis preview hvis det er en fil
        if (!path.toFile().isDirectory) {
            previewFile = path
        }
    }
    
    fun start() {
        try {
            Terminal.hideCursor()
            Terminal.enableRawMode()
            
            updateVisibleFiles(fileTree)
            drawLayout()
            mainLoop()
        } finally {
            Terminal.showCursor()
            Terminal.disableRawMode()
            Terminal.clear()
        }
    }
    
    private fun drawLayout() {
        Terminal.clear()
        
        // Top bar
        println("${ANSI.GREEN}KAider v1.0${ANSI.RESET}")
        println("─".repeat(width))
        
        // Filtræ header
        print(if (activePanel == 0) ANSI.HIGHLIGHT else "" + 
              ANSI.GREEN + "Files" + ANSI.RESET)
        
        // Vertikal separator
        for (i in 2 until height - 1) {
            print(ANSI.moveTo(fileTreeWidth + 1, i) + "│")
        }
        
        // Filtræ indhold
        visibleFiles.forEachIndexed { index, vnode ->
            val node = vnode.node
            val highlight = if (index == selectedFileIndex && activePanel == 0) ANSI.HIGHLIGHT else ""
            val icon = if (node.isDirectory) "📁" else "📄"
            print(ANSI.moveTo(2, 4 + index) + highlight + 
                  vnode.indent + vnode.prefix + icon + " " + node.name + ANSI.RESET)
        }
        
        // Højre panel
        if (searchMode) {
            print(ANSI.moveTo(fileTreeWidth + 4, 4) + 
                  ANSI.GREEN + "Søg: " + ANSI.RESET + searchBuffer)
        } else if (showingSearchResults) {
            drawSearchResults()
        } else if (previewFile != null) {
            drawFilePreview()
        } else {
            print(ANSI.moveTo(fileTreeWidth + 4, 4) + ANSI.GRAY + "Velkommen til KAider!" + ANSI.RESET)
            print(ANSI.moveTo(fileTreeWidth + 4, 5) + ANSI.GRAY + "Hvordan kan jeg hjælpe?" + ANSI.RESET)
            print(ANSI.moveTo(fileTreeWidth + 4, 7) + 
                  (if (activePanel == 1) ANSI.HIGHLIGHT else "") + "> " + ANSI.RESET)
        }
        
        // Status bar
        print(ANSI.moveTo(0, height - 1))
        println("─".repeat(width))
        print("KAider>" + ANSI.moveTo(width - 45, height) + 
              "Tab:skift panel Enter:vælg /:søg Esc:afbryd ?:help")
    }
    
    private fun handleInput(c: Char) {
        if (searchMode) {
            when (c) {
                '\u001B' -> { // ESC
                    searchMode = false
                    searchBuffer.clear()
                    showingSearchResults = false
                }
                '\r' -> { // Enter
                    searchResults = searchFiles(searchBuffer.toString())
                    searchMode = false
                    showingSearchResults = true
                    selectedSearchResult = 0
                    activePanel = 1  // Skift til højre panel
                }
                '\u007F' -> { // Backspace
                    if (searchBuffer.isNotEmpty()) searchBuffer.deleteAt(searchBuffer.length - 1)
                }
                else -> searchBuffer.append(c)
            }
        } else if (showingSearchResults && activePanel == 1) {
            when (c) {
                '\u001B' -> { // ESC
                    showingSearchResults = false
                    previewFile = null
                }
                'j', '\u001B[B' -> {  // Ned
                    if (selectedSearchResult < searchResults.size - 1) selectedSearchResult++
                }
                'k', '\u001B[A' -> {  // Op
                    if (selectedSearchResult > 0) selectedSearchResult--
                }
                '\r' -> {  // Enter - vælg resultat
                    if (searchResults.isNotEmpty()) {
                        val selectedPath = searchResults[selectedSearchResult]
                        navigateToFile(selectedPath)
                        showingSearchResults = false
                        activePanel = 0  // Skift tilbage til filtræ
                    }
                }
            }
        } else {
            when (c) {
                'q' -> return
                '\t' -> activePanel = 1 - activePanel  // Skift mellem paneler
                'j', '\u001B[B' -> {  // Ned (j eller pil ned)
                    if (activePanel == 0 && selectedFileIndex < visibleFiles.size - 1) {
                        selectedFileIndex++
                        // Opdater preview
                        previewFile = visibleFiles[selectedFileIndex].node.path
                    }
                }
                'k', '\u001B[A' -> {  // Op (k eller pil op)
                    if (activePanel == 0 && selectedFileIndex > 0) {
                        selectedFileIndex--
                        // Opdater preview
                        previewFile = visibleFiles[selectedFileIndex].node.path
                    }
                }
                '\r' -> {  // Enter
                    if (activePanel == 0) {
                        val node = visibleFiles[selectedFileIndex].node
                        if (node.isDirectory) {
                            node.isExpanded = !node.isExpanded
                            visibleFiles.clear()
                            updateVisibleFiles(fileTree)
                        } else {
                            previewFile = node.path
                        }
                    }
                }
                '/' -> {  // Start søgning
                    if (activePanel == 0) {
                        searchMode = true
                        searchBuffer.clear()
                    }
                }
                '\u001B' -> { // ESC - luk preview
                    previewFile = null
                }
            }
        }
        drawLayout()
    }
    
    private fun mainLoop() {
        while (true) {
            val c = Terminal.readChar()
            if (c == 'q' && !searchMode && !showingSearchResults) break
            handleInput(c)
        }
    }
}

fun main() {
    KAiderTUI().start()
} 