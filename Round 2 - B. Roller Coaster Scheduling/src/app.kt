import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val T = input.nextInt()
    var Ti = 1
    while (Ti <= T) {
        val N = input.nextInt()
        val C = input.nextInt()
        val M = input.nextInt()

        val seatToBuyer = Array(N, { IntArray(C) })
        for (i in 0 until M) {
            ++seatToBuyer[input.nextInt() - 1][input.nextInt() - 1]
        }

        var mostTicketsOneBuyer = 0
        (0 until C)
                .map { buyerId -> (0 until N).sumBy { seatToBuyer[it][buyerId] } }
                .forEach { mostTicketsOneBuyer = maxOf(mostTicketsOneBuyer, it) }

        var ticketsSoFar = 0
        var minRides = mostTicketsOneBuyer
        for ((i, buyers) in seatToBuyer.withIndex()) {
            ticketsSoFar += buyers.sum()
            minRides = maxOf(minRides, Math.ceil(ticketsSoFar * 1.0 / (i + 1)).toInt())
        }
        val promotions = seatToBuyer
                .map { it.sum() }
                .filter { it > minRides }
                .sumBy { it - minRides }

        println("Case #$Ti: $minRides $promotions")

        ++Ti
    }
}