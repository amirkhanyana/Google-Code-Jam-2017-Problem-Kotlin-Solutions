import java.math.BigDecimal
import java.math.MathContext
import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val T = input.nextInt()
    var Ti = 1
    while (Ti <= T) {
        val N = input.nextInt()
        val Q = input.nextInt()
        val cityHorses = Array(N, { Pair(input.nextInt(), input.nextInt()) })
        val cityRoutes = Array(N, { Array(N, { input.nextInt() }) })
        val startEndPairs = Array(Q, { Pair(input.nextInt() - 1, input.nextInt() - 1) })

        println("Case #" + Ti + ": " + PonyExpress(cityHorses, cityRoutes, startEndPairs).calcFastDeliveries())

        ++Ti
    }
}

class PonyExpress(private val cityHorses: Array<Pair<Int, Int>>, private val cityRoutes: Array<Array<Int>>, private val startEndPairs: Array<Pair<Int, Int>>) {
    private val infinity = BigDecimal(10000000001)
    private val cityTimedRoutes = Array(cityHorses.size, { i -> Array(cityHorses.size, { j -> if (i == j) BigDecimal.ZERO else infinity }) })

    fun calcFastDeliveries(): String {
        for (i in 0 until cityRoutes.size) {
            singleHorseRun(i, cityHorses[i].first, BigDecimal.ZERO, i, Array(cityHorses.size, { Pair(0, 0) }))
        }
        for (k in 0 until cityTimedRoutes.size) {
            for (i in 0 until cityTimedRoutes.size) {
                for (j in 0 until cityTimedRoutes.size) {
                    if (cityTimedRoutes[i][k] == infinity || infinity == cityTimedRoutes[k][j]) continue
                    if (cityTimedRoutes[i][j] == infinity || (cityTimedRoutes[i][j] > cityTimedRoutes[i][k] + cityTimedRoutes[k][j])) {
                        cityTimedRoutes[i][j] = cityTimedRoutes[i][k] + cityTimedRoutes[k][j]
                    }
                }
            }
        }
        val result = StringJoiner(" ")
        for (startEndPair in startEndPairs) {
            result.add(cityTimedRoutes[startEndPair.first][startEndPair.second].toString())
        }
        return result.toString()
    }

    private fun singleHorseRun(horseCityId: Int, remainingDistance: Int, horseSpentTime: BigDecimal, currentCityId: Int, lastVisitStatsForCity: Array<Pair<Int, Int>>) {
        for ((j, distance) in cityRoutes[currentCityId].withIndex()) {
            val updatedRemainingDistance = remainingDistance - distance
            if (distance == -1 || updatedRemainingDistance < 0 || (lastVisitStatsForCity[j].first >= updatedRemainingDistance && lastVisitStatsForCity[j].second >= cityHorses[horseCityId].second)) {
                continue
            }
            val updatedSpentTime = horseSpentTime + BigDecimal(distance).divide(BigDecimal(cityHorses[horseCityId].second), MathContext.DECIMAL128)
            cityTimedRoutes[horseCityId][j] = updatedSpentTime
            lastVisitStatsForCity[j] = Pair(updatedRemainingDistance, cityHorses[horseCityId].second)
            if (remainingDistance > 0) {
                singleHorseRun(horseCityId, updatedRemainingDistance, updatedSpentTime, j, lastVisitStatsForCity)
            }
        }
    }
}