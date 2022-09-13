package chess

fun main() {
    val chess = Chess()
    chess.greeting()
    chess.printChessBoard()
    chess.run()
}


class Chess {
    companion object {
        const val MIN_BOUND = 1
        const val MAX_BOUND = 8
        const val MIN_FILE = 'a'
        const val MAX_FILE = 'h'
        val turnPattern = Regex("^([a-h][1-8]){2}|exit$")
    }

    private val whitePawns: List<Pair<Char, Int>> = (MIN_FILE..MAX_FILE).map { Pair(it, 2) }
    private val blackPawns: List<Pair<Char, Int>> = (MIN_FILE..MAX_FILE).map { Pair(it, 7) }

    private var firstPlayerName: String = "First Player"
    private var secondPlayerName: String = "Second Player"

    fun greeting() {
        println("Pawns-Only Chess")
        println("First Player's name:")
        firstPlayerName = readln()
        println("Second Player's name:")
        secondPlayerName = readln()
    }

    fun printChessBoard() {
        for (rank in MAX_BOUND downTo MIN_BOUND) {
            println("  ".plus("+---".repeat(MAX_BOUND)).plus("+"))
            print("$rank ")
            for (file in MIN_FILE..MAX_FILE) {
                print("| ")
                val position = Pair(file, rank)
                print(if (position in whitePawns) 'W' else if (position in blackPawns) 'B' else ' ')
                print(' ')
            }
            println('|')
        }
        println("  ".plus("+---".repeat(MAX_BOUND)).plus("+"))
        println("    ".plus((MIN_FILE..MAX_FILE).joinToString("   ")))
    }

    fun run() {
        do {
            val firstPlayerMove = playerInput("$firstPlayerName's turn:")
            if (firstPlayerMove == "exit") break
            val secondPlayerMove = playerInput("$secondPlayerName's turn:")
            if (secondPlayerMove == "exit") break
        } while (true)
        println("Bye!")
    }

    private fun playerInput(prompt: String): String {
        var isValid: Boolean
        var turn: String
        do {
            println(prompt)
            turn = readln()
            isValid = turnPattern.matches(turn)
            if (!isValid) {
                println("Invalid Input")
            }
        } while (!isValid)
        return turn
    }

}