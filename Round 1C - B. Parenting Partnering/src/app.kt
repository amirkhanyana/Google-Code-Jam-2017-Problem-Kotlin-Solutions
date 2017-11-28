import java.util.*

data class TimeInterval(val from: Int, val to: Int) : Comparable<TimeInterval> {
    val size: Int
        get() = if (from > to)
            24 * 60 - from + to
        else to - from

    override fun compareTo(other: TimeInterval): Int = from.compareTo(other.from)
}

data class ParentActivity(val parentId: Int, val interval: TimeInterval) : Comparable<ParentActivity> {
    override fun compareTo(other: ParentActivity): Int = interval.compareTo(other.interval)
}

data class FreeInterval(val leftParentActivity: ParentActivity, val rightParentActivity: ParentActivity) : Comparable<FreeInterval> {
    val interval = TimeInterval(leftParentActivity.interval.to, rightParentActivity.interval.from)

    override fun compareTo(other: FreeInterval): Int = interval.size.compareTo(other.interval.size)
}

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val T = input.nextInt()
    var Ti = 1
    while (Ti <= T) {
        val parentsCount = 2
        val parentsActivitiesCount = IntArray(parentsCount, { input.nextInt() })
        val activitiesByParent = Array(parentsCount, { parentId -> Array(parentsActivitiesCount[parentId], { ParentActivity(parentId, TimeInterval(input.nextInt(), input.nextInt())) }) })
        val parentActivities = activitiesByParent.flatten().toTypedArray()
        parentActivities.sort()
        val intervalsCount = parentActivities.size
        var prevParentActivity = parentActivities.last()
        val parentActivitiesIterator = parentActivities.iterator()
        val freeIntervals = Array(intervalsCount, {
            val activity = parentActivitiesIterator.next()
            val freeInterval = FreeInterval(prevParentActivity, activity)
            prevParentActivity = activity
            freeInterval
        })
        freeIntervals.sort()
        val parentBusyness = IntArray(parentsCount)
        for (parentActivity in parentActivities) {
            parentBusyness[parentActivity.parentId] += parentActivity.interval.size
        }
        val parentsDone = BooleanArray(parentsCount)
        var switches = 0
        for (freeInterval in freeIntervals) {
            if (freeInterval.leftParentActivity.parentId != freeInterval.rightParentActivity.parentId) {
                ++switches
                continue
            }
            switches += 2
            val parentId = freeInterval.leftParentActivity.parentId
            if (parentsDone[parentId]) continue
            parentBusyness[parentId] += freeInterval.interval.size
            if (parentBusyness[parentId] > 720) {
                parentsDone[parentId] = true
            } else {
                switches -= 2
            }
        }
        println("Case #$Ti: $switches")

        ++Ti
    }
}