import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val T = input.nextInt()
    var Ti = 1
    while (Ti <= T) {
        val N = input.nextInt()
        val P = input.nextInt()
        val recipe = IntArray(N, { input.nextInt() })
        val packages = Array(N, { IntArray(P, { input.nextInt() }) })

        val ingredientToPackageSizeIntersections = Array(N, { mutableListOf<MutableSet<IntRange>>() })

        for (ingredientId in 0 until N) {
            for (packageId in 0 until P) {
                var start = Math.ceil(packages[ingredientId][packageId] / 1.1 / recipe[ingredientId]).toInt()
                val end = (packages[ingredientId][packageId] / 0.9 / recipe[ingredientId]).toInt()
                if (end > 0 && start <= end) {
                    start = if (start > 0) start else start + 1
                    ingredientToPackageSizeIntersections[ingredientId].add(mutableSetOf(IntRange(start, end)))
                }
            }
        }

        var minPackageSizes =  ingredientToPackageSizeIntersections[0].size

        for (i in 1 until ingredientToPackageSizeIntersections.size) {
            var usedPackages = 0
            for (packageSizeIntersectionSet in ingredientToPackageSizeIntersections[i].toList()) {
                val packageSizeRange = packageSizeIntersectionSet.first()
                packageSizeIntersectionSet.clear()
                for (prevPackageSizeIntersectionsSet in ingredientToPackageSizeIntersections[i - 1]) {
                    for (prevPackageSizes in prevPackageSizeIntersectionsSet) {
                        val intersection = intersectRanges(packageSizeRange, prevPackageSizes)
                        if (!intersection.isEmpty()) {
                            addAndOptimizeSet(packageSizeIntersectionSet, intersection)
                        }
                    }
                }
                if (packageSizeIntersectionSet.isNotEmpty()) ++usedPackages
            }
            minPackageSizes = minOf(minPackageSizes, usedPackages)
        }

        println("Case #$Ti: $minPackageSizes")

        ++Ti
    }
}

fun addAndOptimizeSet(optimizedSet: MutableSet<IntRange>, rangeToAdd: IntRange) {
    var newRangeStart = rangeToAdd.start
    var newRangeEnd = rangeToAdd.endInclusive
    for (intRange in optimizedSet.toSet()) {
        val intersection = intRange.intersect(rangeToAdd)
        if (!intersection.isEmpty()) {
            optimizedSet.remove(intRange)
            newRangeStart = minOf(intRange.start, newRangeStart)
            newRangeEnd = maxOf(intRange.endInclusive, newRangeEnd)
        }
    }

    optimizedSet.add(IntRange(newRangeStart, newRangeEnd))
}

fun intersectRanges(range1: IntRange, range2: IntRange): IntRange {
    if (range1.start <= range2.endInclusive || range2.start <= range1.endInclusive) {
        return IntRange(maxOf(range1.start, range2.start), minOf(range1.endInclusive, range2.endInclusive))
    } else {
        return IntRange.EMPTY
    }
}

