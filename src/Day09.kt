import kotlin.math.abs

/**
 * [Day09](https://adventofcode.com/2022/day/9)
 */

private class Day09 {
    data class Point(val x: Int, val y: Int)
    enum class Direction(val dx: Int, val dy: Int) {
        LEFT(-1, 0),
        UP(0, -1),
        RIGHT(1, 0),
        DOWN(0, 1);

        companion object {
            fun parse(direction: String): Direction = when (direction) {
                "L" -> LEFT
                "U" -> UP
                "R" -> RIGHT
                "D" -> DOWN
                else -> throw RuntimeException()
            }
        }
    }
}

fun main() {
    fun move(point: Day09.Point, direction: Day09.Direction): Day09.Point =
            Day09.Point(point.x + direction.dx, point.y + direction.dy)

    fun moveTail(head: Day09.Point, tail: Day09.Point): Day09.Point {
        return if (abs(head.x - tail.x) > 1 || abs(head.y - tail.y) > 1) {
            Day09.Point(head.x - (head.x - tail.x) / 2, head.y - (head.y - tail.y) / 2)
        } else {
            tail
        }
    }

    fun part1(input: List<String>): Int {
        val visited = mutableSetOf<Day09.Point>()
        var head = Day09.Point(0, 0)
        var tail = Day09.Point(0, 0)
        visited.add(tail)

        input.forEach { line ->
            val (d, n) = line.split(" ").let { (d, n) ->
                Day09.Direction.parse(d) to n.toInt()
            }
            repeat(n) {
                head = move(head, d)
                tail = moveTail(head, tail)
                visited.add(tail)
            }
        }
        return visited.size
    }


    fun part2(input: List<String>): Int {
        val visited = mutableSetOf<Day09.Point>()
        val points = Array(10) {
            Day09.Point(0, 0)
        }
        visited.add(points.last())

        input.forEach { line ->
            val (d, n) = line.split(" ").let { (d, n) ->
                Day09.Direction.parse(d) to n.toInt()
            }
            repeat(n) {
                points[0] = move(points[0], d)
                for (i in 1..9) {
                    points[i] = moveTail(points[i - 1], points[i])
                }
                visited.add(points.last())
            }
        }
        return visited.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)
    val testInputLarge = readInput("Day09_test_large")
    check(part2(testInputLarge) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}