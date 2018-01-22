import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val T = input.nextInt()
    var Ti = 1
    while (Ti <= T) {
        val N = input.nextInt()
        val P = input.nextInt()
        val groups = IntArray(N, { input.nextInt() % P })
        val modedGroups = IntArray(P)
        for (group in groups) {
            modedGroups[group]++
        }

        println("Case #$Ti: " + Solver(modedGroups, P).run())

        ++Ti
    }
}

class Solver(private val modedGroups: IntArray, private val P: Int) {
    private val groupStore = IntArray(P)

    private var maxGroups = 0

    fun run(): Int {
        for (i in 2..P) {
            solve(P, i)
        }
        maxGroups += modedGroups[0]
        modedGroups[0] = 0
        return maxGroups + if (modedGroups.any({ it != 0 })) 1 else 0
    }

    private fun solve(sum: Int, numberOfGroupsToUse: Int) {
        if (numberOfGroupsToUse == 1) {
            groupStore[sum]++
            val min = (1 until P)
                    .filter { groupStore[it] != 0 }
                    .map { modedGroups[it] / groupStore[it] }.min()?: P
            if (min > 0) {
                maxGroups += min
                for (i in 1 until P) {
                    modedGroups[i] -= min * groupStore[i]
                }
            }
            groupStore[sum]--
            return
        }
        for (i in 1 until P) {
            if (i == sum) continue
            groupStore[i]++
            solve((sum + P - i) % P, numberOfGroupsToUse - 1)
            groupStore[i]--
        }
    }
}