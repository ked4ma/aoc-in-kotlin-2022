import kotlin.math.max

/**
 * [Day17](https://adventofcode.com/2022/day/17)
 */

private class Day17 {
    data class Rock(val array: List<List<Char>>) {
        val height = array.size
        val width = array[0].size
    }

    companion object {
        val ROCKS = listOf(
            Rock(listOf("####".toList())),
            Rock(".#.|###|.#.".split("|").map(String::toList)),
            Rock("..#|..#|###".split("|").map(String::toList)),
            Rock("#|#|#|#".split("|").map(String::toList)),
            Rock("##|##".split("|").map(String::toList))
        )
    }
}

fun main() {
    fun fallHist(winds: String, num: Int): List<Int> {
        var topHist = mutableListOf(0)
        val chamber = mutableListOf(Array(7) { '-' })
        var top = 0
        var windIndex = 0
        fun fall(rock: Day17.Rock) {
            repeat((top + rock.height + 3) - chamber.lastIndex) {
                chamber.add(".......".toCharArray().toTypedArray())
            }
            var rockHeight = top + rock.height + 3
            var rockLeft = 2

            fun isCross(height: Int, left: Int): Boolean {
                rock.array.forEachIndexed { h, row ->
                    row.forEachIndexed { w, v ->
                        if (v == '.') return@forEachIndexed
                        if (chamber[height - h][left + w] != '.') return true
                    }
                }
                return false
            }

            do {
                val rockMove = if (winds[windIndex % winds.length] == '>') 1 else -1
                windIndex++
                var moves = 0
                rockLeft = when {
                    rockLeft + rockMove !in 0..7 - rock.width -> rockLeft
                    isCross(rockHeight, rockLeft + rockMove) -> rockLeft
                    else -> {
                        rockLeft + rockMove
                    }
                }
                if (!isCross(rockHeight - 1, rockLeft)) {
                    rockHeight--
                    moves++
                }
            } while (moves > 0)

            rock.array.forEachIndexed { h, row ->
                row.forEachIndexed { w, v ->
                    if (v == '#') {
                        chamber[rockHeight - h][rockLeft + w] = v
                    }
                }
            }
            top = max(top, rockHeight)
        }

        repeat(num) {
            fall(Day17.ROCKS[it % Day17.ROCKS.size])
            topHist.add(top)
        }
        return topHist
    }

    fun part1(input: String): Int {
        return fallHist(input, 2022).last()
    }

    fun part2(input: String): Long {
        val topHist = fallHist(input, 5000)
        val diff = (1..topHist.lastIndex).map { topHist[it] - topHist[it - 1] }

        fun findRepeat(start: Int): Pair<Int, Int>? {
            var len = 10 // want to exclude little value like 1, 2, ...
            while (start + 2 * len < diff.lastIndex) {
                if (diff.subList(start, start + len) == diff.subList(start + len, start + 2 * len)) {
                    return start to len
                }
                len++
            }
            return null
        }

        var i = 0
        val start: Int
        val len: Int
        while (true) {
            val d = findRepeat(i)
            if (d == null) {
                i++
                continue
            }
            start = d.first
            len = d.second
            break
        }

        val loops = (1_000_000_000_000 - start) / len
        val rem = (1_000_000_000_000 - start) % len
        return diff.subList(0, start).sum().toLong() +
                loops * diff.subList(start, start + len).sum().toLong() +
                diff.subList(start, start + rem.toInt()).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput[0]) == 3068)
    check(part2(testInput[0]) == 1_514_285_714_288L)

    val input = readInput("Day17")
    println(part1(input[0]))
    println(part2(input[0]))
}