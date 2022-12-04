import kotlin.math.max

/**
 * [Day04](https://adventofcode.com/2022/day/4)
 */
fun main() {
    fun rangeToSet(rangeStr: String): Set<Int> = rangeStr.split("-").let { (s, e) ->
        IntRange(s.toInt(), e.toInt())
    }.toSet()

    fun part1(input: List<String>): Int {
        return input.filter {
            it.split(",").let { (first, second) ->
                val fSet = rangeToSet(first)
                val sSet = rangeToSet(second)
                max(fSet.size, sSet.size) == (fSet + sSet).size
            }
        }.size
    }

    fun part2(input: List<String>): Int {
        return input.filter {
            it.split(",").let { (first, second) ->
                val fSet = rangeToSet(first)
                val sSet = rangeToSet(second)
                fSet.size + sSet.size != (fSet + sSet).size
            }
        }.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}