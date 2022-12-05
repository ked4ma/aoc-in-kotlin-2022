typealias Stack<T> = ArrayList<T>

/**
 * [Day05](https://adventofcode.com/2022/day/5)
 */
fun main() {
    data class Operation(val num: Int, val from: Int, val to: Int)

    fun conv(input: List<String>): Pair<List<Stack<Char>>, List<Operation>> {
        val index = input.indexOfFirst(String::isEmpty)
        val len = input[index - 1].trim().split("""\s+""".toRegex()).size
        val stacks = (0 until len).map {
            Stack<Char>()
        }
        for (i in index - 2 downTo 0) {
            for (j in 1 until input[i].length step 4) {
                if (input[i][j] != ' ') {
                    stacks[(j - 1) / 4].add(input[i][j])
                }
            }
        }

        val operations = (index + 1 until input.size).mapNotNull { i ->
            """move (\d+) from (\d+) to (\d+)""".toRegex().find(input[i])?.let {
                val (_, num, from, to) = it.groupValues
                Operation(num.toInt(), from.toInt() - 1, to.toInt() - 1)
            }
        }
        return Pair(stacks, operations)
    }

    fun build(stacks: List<Stack<Char>>): String = buildString {
        stacks.forEach {
            it.lastOrNull()?.let { c ->
                append(c)
            }
        }
    }

    fun part1(input: List<String>): String {
        val (stacks, operations) = conv(input)
        operations.forEach { op ->
            repeat(op.num) {
                val c = stacks[op.from].removeLast()
                stacks[op.to].add(c)
            }
        }
        return build(stacks)
    }

    fun part2(input: List<String>): String {
        val (stacks, operations) = conv(input)

        operations.forEach { op ->
            val pos = stacks[op.to].size
            repeat(op.num) {
                val c = stacks[op.from].removeLast()
                stacks[op.to].add(pos, c)
            }
        }

        return build(stacks)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}