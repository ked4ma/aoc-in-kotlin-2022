import kotlin.math.max
import kotlin.math.min

/**
 * [Day14](https://adventofcode.com/2022/day/14)
 */

private class Day14 {
    data class Point(val x: Int, val y: Int)
}

fun main() {
    fun toPathList(input: List<String>): List<List<Day14.Point>> = input.map { line ->
        line.split(" -> ").map {
            it.split(",").let { (x, y) -> Day14.Point(x.toInt(), y.toInt()) }
        }
    }

    fun initMatrix(pathList: List<List<Day14.Point>>, minX: Int, maxX: Int, maxY: Int): Array<Array<Char>> {
        val baseX = min(500, minX)
        val matrix = Array(maxY + 1) { Array(maxX - baseX + 1) { '.' } }
        pathList.forEach { path ->
            path.zipWithNext { a, b ->
                for (x in min(a.x, b.x)..max(a.x, b.x)) {
                    matrix[a.y][x - baseX] = '#'
                }
                for (y in min(a.y, b.y)..max(a.y, b.y)) {
                    matrix[y][a.x - baseX] = '#'
                }
            }
        }
        return matrix
    }

    fun fall(matrix: Array<Array<Char>>, minX: Int, maxX: Int, maxY: Int): Boolean {
        val baseX = min(minX, 500)
        var x = 500
        var y = 0
        while (true) {
            if (y >= maxY) { // down
                return false
            } else if (matrix[y + 1][x - baseX] == '.') {
                y++
            } else if (x == minX) { // left-down
                return false
            } else if (matrix[y + 1][x - baseX - 1] == '.') {
                x--
                y++
            } else if (x == maxX) { // right-down
                return false
            } else if (matrix[y + 1][x - baseX + 1] == '.') {
                x++
                y++
            } else {
                matrix[y][x - baseX] = 'o'
                return true
            }
        }
    }

    fun part1(input: List<String>): Int {
        val pathList = toPathList(input)
        val (minX, maxX) = pathList.flatMap { it.map(Day14.Point::x) }.let {
            it.min() to it.max()
        }
        val maxY = pathList.flatMap { it.map(Day14.Point::y) }.max()
        val matrix = initMatrix(pathList, minX, maxX, maxY)

        var count = 0
        do {
            val isRest = fall(matrix, minX, maxX, maxY)
            count++
        } while (isRest)

        return count - 1
    }

    fun part2(input: List<String>): Int {
        val pathList = toPathList(input)
        val maxY = pathList.flatMap { it.map(Day14.Point::y) }.max() + 2
        val (minX, maxX) = 0 to 1000

        val matrix = initMatrix(pathList + listOf(listOf(
                Day14.Point(minX, maxY), Day14.Point(maxX, maxY)
        )), minX, maxX, maxY)
        val baseX = min(minX, 500)

        var count = 0
        while (matrix[0][500 - baseX] == '.') {
            fall(matrix, minX, maxX, maxY)
            count++
        }

        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}