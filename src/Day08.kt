import kotlin.math.max

/**
 * [Day08](https://adventofcode.com/2022/day/8)
 */

fun main() {
    fun trees(input: List<String>): Array<Array<Int>> = input.map { line ->
        line.toCharArray().map(Char::digitToInt).toTypedArray()
    }.toTypedArray()

    fun toInfoList(input: List<String>): Pair<Array<Array<Int>>, Array<Array<Array<Int>>>> {
        val trees = trees(input)
        val around = Array(trees.size) {
            Array(trees[0].size) {
                Array(4) { 0 }
            }
        }.also {
            val lastI = trees.lastIndex
            val lastJ = trees[0].lastIndex
            for (i in trees.indices) {
                for (j in trees[0].indices) {
                    if (j < lastJ) {
                        // left
                        it[i][j + 1][0] = max(it[i][j][0], trees[i][j])
                        // right
                        it[i][lastJ - j - 1][2] = max(it[i][lastJ - j][2], trees[i][lastJ - j])
                    }
                    if (i < lastI) {
                        // up
                        it[i + 1][j][1] = max(it[i][j][1], trees[i][j])
                        // down
                        it[lastI - i - 1][j][3] = max(it[lastI - i][j][3], trees[lastI - i][j])
                    }
                }
            }
        }
        return trees to around
    }

    fun part1(input: List<String>): Int {
        val (trees, around) = toInfoList(input)
        return (trees.size + trees[0].size) * 2 - 4 +
                (1 until trees.lastIndex).sumOf { i ->
                    (1 until trees[0].lastIndex).count { j ->
                        trees[i][j] > around[i][j].min()
                    }
                }
    }

    fun part2(input: List<String>): Int {
        val trees = trees(input)
        val lastI = trees.lastIndex
        val lastJ = trees[0].lastIndex

        val around = Array(trees.size) {
            Array(trees[0].size) {
                Array(4) { 0 }
            }
        }

        fun search(i: Int, j: Int, direction: Int, value: Int): Int {
            if (i == 0 || i == lastI || j == 0 || j == lastJ) return 0
            val (ni, nj) = when (direction) {
                0 -> i to j - 1 // left
                1 -> i - 1 to j // up
                2 -> i to j + 1 // right
                3 -> i + 1 to j // down
                else -> throw RuntimeException()
            }
            around[i][j][direction] = if (trees[ni][nj] < value) search(ni, nj, direction, value) + 1 else 1
            return around[i][j][direction]
        }

        for (i in 1 until trees.lastIndex) {
            for (j in 1 until trees[0].lastIndex) {
                for (k in 0..3) {
                    search(i, j, k, trees[i][j])
                }
            }
        }

        return around.maxOf { row ->
            row.maxOf { cell ->
                cell.reduce { acc, v -> acc * v }
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}