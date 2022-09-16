package chess

fun main() {
    val chess = Chess()
    chess.greeting()
    chess.run()
}


class Chess {
    private companion object {
        const val MIN_BOUND = 1
        const val MAX_BOUND = 8
        const val MIN_FILE = 'a'
        const val MAX_FILE = 'h'
        private val firstPlayer: State = FirstPlayer()
        private val secondPlayer: State = SecondPlayer()
    }

    data class Position(val file: Char, val rank: Int) {
        override fun toString(): String {
            return "$file$rank"
        }
    }

    abstract class State {
        companion object {
            private val turnPattern = Regex("^([a-h][1-8]){2}|exit$")
        }
        abstract var isExit: Boolean
        protected abstract val color: String
        protected abstract val initialRank: Int
        abstract var playerName: String
        abstract val pawns: MutableList<Position>
        protected var captured: Position? = null
        var enPassant: Position? = null

        open fun isValid(s: String): Boolean {
            if ("exit" == s) {
                isExit = true
                return true
            }
            if (!turnPattern.matches(s)) {
                println("Invalid Input")
                return false
            }
            val start = Position(s[0], s[1].toString().toInt())
            if (!pawns.contains(start)) {
                println("No $color pawn at $start")
                return false
            }
            return true
        }

        fun readTurn() {
            do {
                println("$playerName's turn:")
                val turn = readln()
                val isValid = isValid(turn)
                if (isValid && !isExit) {
                    val start = Position(turn[0], turn[1].toString().toInt())
                    val end = Position(turn[2], turn[3].toString().toInt())
                    enPassant = if (kotlin.math.abs(end.rank - start.rank) == 2) end else null
                    next().pawns.remove(captured)
                    pawns[pawns.indexOf(start)] = end
                }
            } while (!isValid)
        }

        abstract fun next(): State
    }

    private class FirstPlayer: State() {
        override var isExit: Boolean = false
        override val color = "white"
        override val initialRank: Int = 2
        override var playerName: String = "First Player"
        override val pawns: MutableList<Position> = (MIN_FILE..MAX_FILE).map { Position(it, 2) }.toMutableList()
        override fun next(): State = secondPlayer

        override fun isValid(s: String): Boolean {
            if (super.isValid(s)) {
                if (isExit) return true
                val start = Position(s[0], s[1].toString().toInt())
                val end = Position(s[2], s[3].toString().toInt())
                if (start.file == end.file) {
                    // move forward
                    val diff = end.rank - start.rank
                    val isBusyPos = next().pawns.contains(end)
                            || next().pawns.contains(Position(end.file, end.rank - 1))
                    if (!isBusyPos && (1 == diff || (start.rank == initialRank && 2 == diff))) {
                        return true
                    }
                } else if (end.file - 1 == start.file && end.rank - 1 == start.rank) {
                    // capture left
                    if (next().pawns.contains(end)) {
                        captured = end
                        return true
                    }
                    // en passant left
                    if (next().enPassant?.rank == start.rank && next().enPassant?.file == end.file) {
                        captured = next().enPassant
                        return true
                    }
                } else if (end.file + 1 == start.file && end.rank - 1 == start.rank) {
                    // capture right
                    if (next().pawns.contains(end)) {
                        captured = end
                        return true
                    }
                    // en passant right
                    if (next().enPassant?.rank == start.rank && next().enPassant?.file == end.file) {
                        captured = next().enPassant
                        return true
                    }
                }
                println("Invalid Input")
                return false
            }
            return false
        }
    }

    private class SecondPlayer: State() {
        override var isExit: Boolean = false
        override val color = "black"
        override val initialRank: Int = 7
        override var playerName: String = "Second Player"
        override val pawns: MutableList<Position> = (MIN_FILE..MAX_FILE).map { Position(it, 7) }.toMutableList()
        override fun next(): State = firstPlayer
        override fun isValid(s: String): Boolean {
            if (super.isValid(s)) {
                if (isExit) return true
                val start = Position(s[0], s[1].toString().toInt())
                val end = Position(s[2], s[3].toString().toInt())
                if (start.file == end.file) {
                    // move forward
                    val diff = start.rank - end.rank
                    val isBusyPos = next().pawns.contains(end)
                            || next().pawns.contains(Position(end.file, end.rank + 1))
                    if (!isBusyPos && (1 == diff || (start.rank == initialRank && 2 == diff))) {
                        return true
                    }
                } else if (end.file - 1 == start.file && end.rank + 1 == start.rank) {
                    // capture left
                    if (next().pawns.contains(end)) {
                        captured = end
                        return true
                    }
                    // en passant left
                    if (next().enPassant?.rank == start.rank && next().enPassant?.file == end.file) {
                        captured = next().enPassant
                        return true
                    }
                } else if (end.file + 1 == start.file && end.rank + 1 == start.rank) {
                    // capture right
                    if (next().pawns.contains(end)) {
                        captured = end
                        return true
                    }
                    // en passant right
                    if (next().enPassant?.rank == start.rank && next().enPassant?.file == end.file) {
                        captured = next().enPassant
                        return true
                    }
                }
                println("Invalid Input")
                return false
            }
            return false
        }
    }


    private var state = secondPlayer

    fun greeting() {
        println("Pawns-Only Chess")
        println("First Player's name:")
        firstPlayer.playerName = readln()
        println("Second Player's name:")
        secondPlayer.playerName = readln()
        printChessBoard()
    }

    private fun printChessBoard() {
        for (rank in MAX_BOUND downTo MIN_BOUND) {
            println("  ".plus("+---".repeat(MAX_BOUND)).plus("+"))
            print("$rank ")
            for (file in MIN_FILE..MAX_FILE) {
                print("| ")
                val position = Position(file, rank)
                print(if (position in firstPlayer.pawns) 'W' else if (position in secondPlayer.pawns) 'B' else ' ')
                print(' ')
            }
            println('|')
        }
        println("  ".plus("+---".repeat(MAX_BOUND)).plus("+"))
        println("    ".plus((MIN_FILE..MAX_FILE).joinToString("   ")))
    }

    fun run() {
        state = secondPlayer
        do {
            state = state.next()
            state.readTurn()
            if (!state.isExit) {
                printChessBoard()
            }
        } while (!state.isExit)
        println("Bye!")
    }


}