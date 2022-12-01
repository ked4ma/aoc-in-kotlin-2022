/**
 * [Day01](https://adventofcode.com/2022/day/1)
 */
fun main() {
    fun convInput(input: List<String>): List<Int> = buildList {
        var list = ArrayList<Int>()
        for (s in input) {
            if (s.isEmpty()) {
                if (list.size > 0) {
                    add(list)
                    list = ArrayList()
                }
            } else {
                list.add(s.toInt())
            }
        }
        if (list.isNotEmpty()) {
            add(list)
        }
    }.map(ArrayList<Int>::sum)

    fun part1(input: List<String>): Int {
        val conv = convInput(input)
        return conv.max()
    }

    fun part2(input: List<String>): Int {
        val conv = convInput(input)
        return conv.sortedDescending().take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
