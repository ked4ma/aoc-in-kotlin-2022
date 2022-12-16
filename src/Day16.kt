import kotlin.math.ceil

/**
 * [Day16](https://adventofcode.com/2022/day/16)
 */

private class Day16 {
    data class Node(val key: String, val rate: Int) {
        val valves = mutableListOf<String>()
    }

    data class Snapshot(val keys: List<String>, val estimate: Int, val opened: Set<String>, val time: Int)
}

fun main() {
    fun parse(input: List<String>): Map<String, Day16.Node> {
        val pattern = """Valve (\w+) has flow rate=(\d+); tunnels? leads? to valves? (.+)""".toRegex()
        return buildMap {
            input.forEach { line ->
                pattern.find(line)!!.groupValues.let { (_, key, rate, valves) ->
                    val valveKeys = valves.split(", ")
                    put(key,
                        Day16.Node(key, rate.toInt()).apply {
                            this.valves.addAll(valveKeys)
                        }
                    )
                }
            }
        }
    }

    fun estimate(maps: Map<String, Day16.Node>, playerNum: Int = 1, round: Int = 30): Map<List<String>, Int> {
        val start = (1..playerNum).map { "AA" }
        val estimates = mutableMapOf(start to 0)
        val queue = ArrayDeque(listOf(Day16.Snapshot(start, 0, emptySet(), round * playerNum)))
        val bucket = mutableSetOf<Day16.Snapshot>()
        fun addToBucket(snap: Day16.Snapshot) {
            if (snap.keys !in estimates || snap.keys.filter { it !in snap.opened }.distinct().sumOf {
                    maps.getValue(it).rate
                }.let { it * (snap.time - 1) } + snap.estimate >= estimates.getValue(snap.keys)
            ) {
                bucket.add(snap)
            }
        }
        while (queue.isNotEmpty() || bucket.isNotEmpty()) {
            if (queue.isEmpty()) {
                queue.addAll(bucket)
                bucket.clear()
            }
            val (current, est, opened, rem) = queue.removeFirst()

            val index = rem % playerNum
            val nodes = current.map(maps::getValue)
            val time = ceil(rem.toFloat() / playerNum).toInt()
            when {
                rem <= 0 -> continue // finish
                // no value to open
                nodes[index].rate == 0 -> Unit
                // already opened
                current[index] in opened -> Unit
                est + nodes[index].rate * (time - 1) > estimates.getOrDefault(current, 0) -> {
                    val v = est + nodes[index].rate * (time - 1)
                    estimates[current] = v
                    addToBucket(Day16.Snapshot(current, v, opened + current[index], rem - 1))
                }

                else -> Unit
            }
            nodes[index].valves.forEach {
                val next = current.toMutableList().apply {
                    this[index] = it
                }
                addToBucket(Day16.Snapshot(next, est, opened, rem - 1))
            }
        }
        return estimates
    }

    fun part1(input: List<String>): Int {
        val maps = parse(input)
        val estimates = estimate(maps)
        return estimates.values.max()
    }

    fun part2(input: List<String>): Int {
        val maps = parse(input)
        val estimates = estimate(maps, 2, 26)
        return estimates.values.max()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 1651)
    check(part2(testInput) == 1707)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}