package chess

fun main() {
    val chess = Chess()
    chess.printChessBoard()
}


class Chess {
    companion object {
        const val MIN_BOUND = 1
        const val MAX_BOUND = 8
        const val MIN_FILE = 'a'
        const val MAX_FILE = 'h'
    }

    private val whitePawns: List<Pair<Char, Int>> = (MIN_FILE..MAX_FILE).map { Pair(it, 2) }
    private val blackPawns: List<Pair<Char, Int>> = (MIN_FILE..MAX_FILE).map { Pair(it, 7) }

    fun printChessBoard() {
        println("Pawns-Only Chess")
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

}