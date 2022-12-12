import java.util.ArrayDeque

/**
 * [Day12](https://adventofcode.com/2022/day/12)
 */

fun main() {
    fun toGrid(input: List<String>): Array<Array<Char>> = input.map {
        it.toCharArray().toTypedArray()
    }.toTypedArray()

    fun bfs(
            grid: Array<Array<Char>>,
            start: Pair<Int, Int>,
            costs: Array<Array<Int>>,
            comparator: (current: Char, next: Char) -> Boolean
    ) {
        val height = grid.size
        val width = grid[0].size

        val queue = ArrayDeque(listOf(start))
        while (queue.isNotEmpty()) {
            val (y, x) = queue.removeFirst()
            listOf(
                    // left
                    y to x - 1,
                    // up
                    y - 1 to x,
                    // right
                    y to x + 1,
                    // down
                    y + 1 to x,
            ).filter { (ny, nx) ->
                ny in 0 until height && nx in 0 until width
            }.forEach { (ny, nx) ->
                if (comparator(grid[y][x], grid[ny][nx]) && costs[y][x] + 1 < costs[ny][nx]) {
                    costs[ny][nx] = costs[y][x] + 1
                    queue.add(ny to nx)
                }
            }
        }
    }

    fun calcCostMap(
            grid: Array<Array<Char>>,
            indexS: Pair<Int, Int>,
            indexE: Pair<Int, Int>,
            startSelector: (indexS: Pair<Int, Int>, indexE: Pair<Int, Int>) -> Pair<Int, Int>,
            comparator: (current: Char, next: Char) -> Boolean,
    ): Array<Array<Int>> {
        val start = startSelector(indexS, indexE)
        grid[indexS.first][indexS.second] = 'a'
        grid[indexE.first][indexE.second] = 'z'
        val costs = Array(grid.size) {
            Array(grid[0].size) { 100_000_000 }
        }.also {
            it[start.first][start.second] = 0
        }
        bfs(grid, start, costs, comparator)
        return costs
    }

    fun part1(input: List<String>): Int {
        val grid = toGrid(input)
        val indexS = input.joinToString(separator = "").indexOf('S').let {
            it / grid[0].size to it % grid[0].size
        }
        val indexE = input.joinToString(separator = "").indexOf('E').let {
            it / grid[0].size to it % grid[0].size
        }
        val costs = calcCostMap(
                grid, indexS, indexE,
                startSelector = { s, _ -> s },
                comparator = { current, next -> next - current <= 1 }
        )
        return costs[indexE.first][indexE.second]
    }

    fun part2(input: List<String>): Int {
        val grid = toGrid(input)
        val indexS = input.joinToString(separator = "").indexOf('S').let {
            it / grid[0].size to it % grid[0].size
        }
        val indexE = input.joinToString(separator = "").indexOf('E').let {
            it / grid[0].size to it % grid[0].size
        }
        val costs = calcCostMap(
                grid, indexS, indexE,
                startSelector = { _, e -> e },
                comparator = { current, next -> current - next <= 1 }
        )
        return grid.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                if (c == 'a') costs[y][x] else null
            }
        }.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}