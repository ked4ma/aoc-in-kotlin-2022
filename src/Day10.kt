/**
 * [Day10](https://adventofcode.com/2022/day/10)
 */
fun main() {
    fun toOpFragments(input: List<String>): List<Int> = buildList {
        input.forEach {
            add(0)
            if (it.startsWith("addx")) {
                add(it.split(" ")[1].toInt())
            }
        }
    }

    fun part1(input: List<String>): Int {
        val ops = toOpFragments(input)

        var count = 0
        var registerX = 1
        ops.take(220).forEachIndexed { i, v ->
            val j = i + 1
            if ((j + 20) % 40 == 0) {
                count += j * registerX
            }
            registerX += v
        }

        return count
    }


    fun part2(input: List<String>): String {
        val ops = toOpFragments(input)
        var registerX = 1

        return buildList {
            ops.take(240).chunked(40).forEach { chunk ->
                add(buildString {
                    chunk.forEachIndexed { i, v ->
                        val j = i + 1
                        append(if (j in registerX..registerX + 2) '#' else '.')
                        registerX += v
                    }
                })
            }
        }.joinToString(separator = "\n")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    check(part2(testInput) == listOf(
            "##..##..##..##..##..##..##..##..##..##..",
            "###...###...###...###...###...###...###.",
            "####....####....####....####....####....",
            "#####.....#####.....#####.....#####.....",
            "######......######......######......####",
            "#######.......#######.......#######....."
    ).joinToString(separator = "\n"))

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}