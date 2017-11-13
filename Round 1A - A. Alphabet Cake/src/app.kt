import java.util.*
import kotlin.collections.HashSet

class Cake(val R: Int, val C: Int, var cake: Array<CharArray>) {
    private val EMPTY = '?'
    private val usedLetters: MutableSet<Char> = HashSet()

    fun decorate() {
        var i = 0
        while (i < R) {
            var j = 0
            while (j < C) {
                if (cake[i][j] == EMPTY || !usedLetters.contains(cake[i][j])) {
                    nextRect(i, j)
                }
                ++j
            }
            ++i
        }
    }

    private fun nextRect(startR: Int, startC: Int): Pair<Int, Int> {
        var mR = R - 1
        var mC = C - 1
        var currentLetter = EMPTY
        var currentLetterR = 0
        var i = startR
        mi@
        while (i < R) {
            var j = startC
            while (j <= mC) {
                if (cake[i][j] != EMPTY) {
                    if (usedLetters.contains(cake[i][j])) {
                        mC = j - 1
                    } else {
                        if (currentLetter != EMPTY) {
                            if (currentLetterR == i) {
                                mC = j - 1
                                break
                            } else {
                                mR = i - 1
                                break@mi
                            }
                        } else if (!usedLetters.contains(currentLetter)) {
                            currentLetter = cake[i][j]
                            currentLetterR = i
                        }
                    }
                }
                ++j
            }
            ++i
        }

        i = startR
        while (i <= mR) {
            var j = startC
            while (j <= mC) {
                cake[i][j] = currentLetter
                ++j
            }
            ++i
        }

        usedLetters.add(currentLetter)

        return Pair(mR, mC)
    }


}

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val T = input.nextInt()
    var i = 1
    while (i <= T) {
        val R = input.nextInt()
        val C = input.nextInt()
        val cakeMatrix = Array(R, { CharArray(C) })
        input.nextLine()
        for (row in cakeMatrix) {
            val line = input.nextLine()
            for ((index, _) in row.withIndex()) {
                row[index] = line[index]
            }
        }

        val cake = Cake(R, C, cakeMatrix)
        cake.decorate()

        println("Case #$i:")

        for (row in cake.cake) {
            for (cell in row) {
                print(cell)
            }
            println()
        }

        ++i
    }
}

