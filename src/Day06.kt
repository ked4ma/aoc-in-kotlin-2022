/**
 * [Day06](https://adventofcode.com/2022/day/6)
 */
fun main() {
    fun findMarker(input: String, len: Int) : Int{
        val map = mutableMapOf<Char, Int>()
        (0 until len - 1).forEach {
            map[input[it]] = map.getOrDefault(input[it], 0) + 1
        }
        for (i in len - 1 until input.length) {
            map[input[i]] = map.getOrDefault(input[i], 0) + 1
            if (map.keys.size == len) {
                return i + 1
            }
            map.getValue(input[i - (len - 1)]).let {
                if (it == 1) {
                    map.remove(input[i - (len - 1)])
                } else {
                    map[input[i - (len - 1)]] = it - 1
                }
            }
        }
        return -1
    }

    fun part1(input: String): Int {
        return findMarker(input, 4)
    }

    fun part2(input: String): Int {
        return findMarker(input, 14)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")[0]
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06")[0]
    println(part1(input))
    println(part2(input))
}