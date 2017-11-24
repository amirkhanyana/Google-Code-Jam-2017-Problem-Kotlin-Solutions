import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val T = input.nextInt()
    var Ti = 1
    while (Ti <= T) {
        val N = input.nextInt()
        val K = input.nextInt()
        val pancakes = Array(N, { Pair(input.nextInt(), input.nextInt()) })
        val pancakesByCylinderArea = pancakes.mapIndexed({ i, v -> i to v }).sortedBy { cylinderArea(it.second.first, it.second.second) }.toMap().toMutableMap()
        val pancakesByRad = pancakes.mapIndexed({ i, v -> i to v }).sortedByDescending { it.second.first }.toMap().toMutableMap()
        for (i in 0 until N - K) {
            val pancakesByCylinderAreaList = pancakesByCylinderArea.toList()
            val pancake = pancakesByCylinderAreaList[0]
            val pancakesByRadIterator = pancakesByRad.iterator()
            val largest = pancakesByRadIterator.next()
            val secondLargest = pancakesByRadIterator.next()
            if (largest.key == pancake.first) {
                val nextSmallestCylinder = pancakesByCylinderAreaList[1]
                if (cylinderArea(largest.value.first, largest.value.second) + surfaceArea(largest.value.first) - surfaceArea(secondLargest.value.first) > cylinderArea(nextSmallestCylinder.second.first, nextSmallestCylinder.second.second)) {
                    pancakesByCylinderArea.remove(nextSmallestCylinder.first)
                    pancakesByRad.remove(nextSmallestCylinder.first)
                } else{
                    pancakesByCylinderArea.remove(pancake.first)
                    pancakesByRad.remove(pancake.first)
                }
            } else {
                pancakesByCylinderArea.remove(pancake.first)
                pancakesByRad.remove(pancake.first)
            }
        }
        val pancakesByRadAsList = pancakesByRad.toList()

        println("Case #" + Ti + ": " + (pancakesByRadAsList.sumByDouble { cylinderArea(it.second.first, it.second.second) } + surfaceArea(pancakesByRadAsList[0].second.first)))

        ++Ti
    }
}

fun cylinderArea(r: Int, h: Int) = 2 * Math.PI * r * h
fun surfaceArea(r: Int) = Math.PI * r * r