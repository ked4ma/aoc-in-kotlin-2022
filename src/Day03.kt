import java.lang.RuntimeException

/**
 * [Day03](https://adventofcode.com/2022/day/3)
 */
fun main() {
    fun toCompartments(input: List<String>): List<Pair<String, String>> = input.map {
        it.substring(0 until it.length / 2) to it.substring(it.length / 2 until it.length)
    }

    fun toIndex(c: Char): Int = if (c in 'a'..'z') c - 'a' else c - 'A' + 26

    fun part1(input: List<String>): Int {
        return toCompartments(input).map { (f, s) ->
            val first = Array(52) { 0 }
            val second = Array(52) { 0 }
            for (i in f.indices) {
                first[toIndex(f[i])]++
                second[toIndex(s[i])]++
            }
            for (i in first.indices) {
                if (first[i] > 0 && second[i] > 0) {
                    return@map i + 1
                }
            }
            0
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val conv = input.map {
            it.toList().sorted().distinct()
        }.map {
            it.fold(0L) { acc, b -> acc or (1L shl toIndex(b)) }
        }
        var result = 0
        for (i in conv.indices step 3) {
            result += (conv[i] and conv[i + 1] and conv[i + 2]).countTrailingZeroBits() + 1
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}