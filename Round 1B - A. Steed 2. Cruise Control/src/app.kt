import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val T = input.nextInt()
    var Ti = 1
    while (Ti <= T) {
        val D = input.nextInt()
        val N = input.nextInt()
        val positionToSpeed = Array(N, { Pair(input.nextInt(), input.nextInt()) })
        var maxTime = 0.0
        for ((pos, speed) in positionToSpeed) {
            maxTime = maxOf(maxTime, (D - pos) * 1.0 / speed)
        }

        val maxSpeed = D / maxTime
        println("Case #" + Ti + ": " + ("%.6f".format(maxSpeed)))

        ++Ti
    }
}