import kotlin.math.min

/**
 * [Day13](https://adventofcode.com/2022/day/13)
 */

private class Day13 {
    sealed class Packet {
        abstract fun toData(): Data

        companion object {
            fun parse(input: String): Packet {
                val packet = innerParse(input).second!!
                // check parsed correctly
                check(packet.toString() == input.replace(",", ", "))
                return packet
            }

            private fun innerParse(input: String): Pair<Int, Packet?> {
                if (input.isEmpty()) return 0 to null
                if (input.startsWith('[')) {
                    val data = Data()
                    var index = 1
                    var current = 1
                    while (current < input.length) {
                        when (input[current]) {
                            ',' -> {
                                if (index < current) {
                                    data.list.add(Value(input.substring(index, current).toInt()))
                                }
                                index = current + 1
                                current = index - 1
                            }

                            ']' -> {
                                if (index < current) {
                                    data.list.add(Value(input.substring(index, current).toInt()))
                                }
                                return current + 1 to data
                            }

                            '[' -> {
                                val (read, sub) = innerParse(input.substring(current))
                                data.list.add(sub!!)
                                index = current + read
                                current = index - 1
                            }
                        }
                        current++
                    }
                    return current to data
                }
                return input.length to Value(input.toInt())
            }
        }
    }

    data class Value(val n: Int) : Packet() {
        override fun toData(): Data {
            return Data().also {
                it.list.add(this)
            }
        }

        override fun toString(): String {
            return n.toString()
        }
    }

    class Data : Packet() {
        val list = mutableListOf<Packet>()

        override fun toData(): Data = this

        override fun toString(): String {
            return list.toString()
        }
    }

    enum class STATUS {
        RIGHT, INVALID, PASS
    }
}

fun main() {
    fun toPacketPairs(input: List<String>): List<Pair<Day13.Packet, Day13.Packet>> = buildList {
        for (i in input.indices step 3) {
            val left = Day13.Packet.parse(input[i])
            val right = Day13.Packet.parse(input[i + 1])
            add(left to right)
        }
    }

    fun isRightOrder(left: Day13.Packet, right: Day13.Packet): Day13.STATUS {
        when {
            left is Day13.Value && right is Day13.Value -> {
                if (left.n < right.n) return Day13.STATUS.RIGHT
                if (left.n > right.n) return Day13.STATUS.INVALID
                return Day13.STATUS.PASS
            }

            left is Day13.Data && right is Day13.Data -> {
                val leftList = left.list
                val rightList = right.list
                for (i in 0 until min(leftList.size, rightList.size)) {
                    val status = isRightOrder(leftList[i], rightList[i])
                    if (status != Day13.STATUS.PASS) {
                        return status
                    }
                }
                return when {
                    leftList.size < rightList.size -> Day13.STATUS.RIGHT
                    leftList.size > rightList.size -> Day13.STATUS.INVALID
                    else -> Day13.STATUS.PASS
                }
            }

            else -> {
                return isRightOrder(left.toData(), right.toData())
            }
        }
    }

    fun part1(input: List<String>): Int {
        val packets = toPacketPairs(input)
        val sum = packets.foldIndexed(0) { index, acc, (left, right) ->
            acc + (if (isRightOrder(left, right) == Day13.STATUS.INVALID) 0 else index + 1)
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val additionalPackets = listOf(
                Day13.Packet.parse("[[2]]"),
                Day13.Packet.parse("[[6]]"),
        )
        val packets = toPacketPairs(input).flatMap { (left, right) ->
            listOf(left, right)
        } + additionalPackets

        return packets.sortedWith { p1, p2 ->
            when (isRightOrder(p1, p2)) {
                Day13.STATUS.RIGHT -> -1
                Day13.STATUS.INVALID -> 1
                Day13.STATUS.PASS -> 0
            }
        }.mapIndexedNotNull { index, packet ->
            if (packet in additionalPackets) index + 1 else null
        }.reduce { acc, i -> acc * i }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}