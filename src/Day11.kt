/**
 * [Day11](https://adventofcode.com/2022/day/11)
 */

private class Day11 {
    class Monkey private constructor(
            val id: Int,
            items: List<Long>,
            val inspect: (Long) -> Long,
            val divisible: Int,
            private val destTrue: Int,
            private val destFalse: Int
    ) {
        val items = ArrayDeque<Long>()

        init {
            this.items.addAll(items)
        }

        fun test(n: Long): Int = if (n % divisible == 0L) destTrue else destFalse

        companion object {
            fun parse(input: List<String>): Monkey {
                val numRegex = """(\d+)""".toRegex()
                val id = numRegex.find(input[0])!!.groupValues.first().toInt()
                val items = input[1].split(":")[1].split(",").map(String::trim).map(String::toLong)
                val op = parseOperation(input[2].split("=")[1].trim())
                val divisible = numRegex.find(input[3])!!.groupValues.first().toInt()
                val destTrue = numRegex.find(input[4])!!.groupValues.first().toInt()
                val destFalse = numRegex.find(input[5])!!.groupValues.first().toInt()
                return Monkey(id, items, op, divisible, destTrue, destFalse)
            }
        }
    }

    companion object {
        fun parseOperation(opStr: String): (Long) -> Long {
            val (_, operandL, op, operandR) = """([^ ]+) ([+\-*]) ([^ ]+)""".toRegex().find(opStr)!!.groupValues
            return { old: Long ->
                val l = if (operandL == "old") old else operandL.toLong()
                val r = if (operandR == "old") old else operandR.toLong()
                when (op) {
                    "+" -> l + r
                    "-" -> l - r
                    "*" -> l * r
                    else -> throw RuntimeException()
                }
            }
        }
    }
}

fun main() {
    fun parseMonkey(input: List<String>): List<Day11.Monkey> = buildList {
        for (i in input.indices step 7) {
            add(Day11.Monkey.parse(input.subList(i, i + 6)))
        }
    }

    fun simulate(monkeyList: List<Day11.Monkey>, repeat: Int, worryHandler: (Long) -> Long): Array<Long> {
        val inspectCounts = Array(monkeyList.size) { 0L }

        repeat(repeat) {
            monkeyList.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    var item = monkey.items.removeFirst()
                    item = monkey.inspect(item)
                    inspectCounts[monkey.id]++
                    item = worryHandler(item)
                    monkeyList[monkey.test(item)].items.addLast(item)
                }
            }
        }

        return inspectCounts
    }

    fun part1(input: List<String>): Long {
        val monkeyList = parseMonkey(input)

        val inspectCounts = simulate(monkeyList, 20) { it / 3 }

        return inspectCounts.sortedDescending().take(2).reduce { acc, i -> acc * i }
    }


    fun part2(input: List<String>): Long {
        val monkeyList = parseMonkey(input)
        val divider = monkeyList.map(Day11.Monkey::divisible).reduce { acc, i -> acc * i }

        val inspectCounts = simulate(monkeyList, 10000) { it % divider }

        return inspectCounts.sortedDescending().take(2).reduce { acc, i -> acc * i }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}