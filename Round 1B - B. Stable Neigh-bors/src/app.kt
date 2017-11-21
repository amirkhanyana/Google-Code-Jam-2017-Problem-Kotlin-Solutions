import UnicornType.*
import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val T = input.nextInt()
    var Ti = 1
    while (Ti <= T) {
        val N = input.nextInt()
        R.count = input.nextInt()
        O.count = input.nextInt()
        Y.count = input.nextInt()
        G.count = input.nextInt()
        B.count = input.nextInt()
        V.count = input.nextInt()

        val unicornPlacer = UnicornPlacer(N)

        println("Case #" + Ti + ": " + unicornPlacer.place())

        ++Ti
    }
}

enum class UnicornType(var count: Int, val symbol: Char) {
    R(0, 'R'), O(0, 'O'), Y(0, 'Y'), G(0, 'G'), B(0, 'B'), V(0, 'V')
}

class UnicornPlacer(private val n: Int) {
    private val stable = CharArray(n)
    private var index = 0

    private var starting = B

    private fun chooseStarting() {
        starting = when {
            B.count > 0 -> B
            R.count > 0 -> R
            else -> Y
        }
    }

    private fun isConnected(): Boolean {
        if (B.count == 0 && O.count > 0) return false
        if (R.count == 0 && G.count > 0) return false
        if (Y.count == 0 && V.count > 0) return false
        return true
    }

    fun place(): String {
        if (!isConnected()) return "IMPOSSIBLE"
        chooseStarting()
        val succeed = placeFrom(starting)
        return if (succeed && index == n && areSiblings(stable[0], stable[n - 1])) {
            StringBuilder().append(stable).toString()
        } else {
            "IMPOSSIBLE"
        }
    }

    private fun areSiblings(type1: Char, type2: Char): Boolean = when (type1) {
        'R' -> type2 == 'G' || type2 == 'B' || type2 == 'Y'
        'O' -> type2 == 'B'
        'Y' -> type2 == 'R' || type2 == 'B' || type2 == 'V'
        'G' -> type2 == 'R'
        'B' -> type2 == 'O' || type2 == 'R' || type2 == 'Y'
        'V' -> type2 == 'Y'
        else -> {
            false
        }
    }

    private fun placeFrom(from: UnicornType): Boolean {
        val corner: UnicornType
        val other1: UnicornType
        val other2: UnicornType
        val other1DiffCount: Int
        val other2DiffCount: Int
        when (from) {
            B -> {
                corner = O
                other1 = R
                other2 = Y
                other1DiffCount = R.count - G.count
                other2DiffCount = Y.count - V.count
            }
            R -> {
                corner = G
                other1 = B
                other2 = Y
                other1DiffCount = B.count - O.count
                other2DiffCount = Y.count - V.count
            }
            Y -> {
                corner = V
                other1 = B
                other2 = R
                other1DiffCount = B.count - O.count
                other2DiffCount = R.count - G.count
            }
            else -> {
                return false
            }
        }
        val cornerCount = corner.count
        if (cornerCount > 0) {
            if (from.count < cornerCount) return false
            for (i in 0 until cornerCount) {
                stable[index++] = from.symbol
                from.count--
                stable[index++] = corner.symbol
                corner.count--
            }
        }

        if (from.count == 0) return true

        stable[index++] = from.symbol
        from.count--

        val next = if (other1DiffCount == other2DiffCount) {
            if (other1 == starting) other1 else other2
        } else {
            if (other1DiffCount > other2DiffCount) other1 else other2
        }

        if (next.count > 0) {
            placeFrom(next)
        }
        return true
    }
}
