/**
 * [Day18](https://adventofcode.com/2022/day/18)
 */

private class Day18 {
    data class Point(val x: Int, val y: Int, val z: Int) {
        operator fun get(index: Int) = when (index) {
            0 -> x
            1 -> y
            2 -> z
            else -> throw RuntimeException()
        }
    }
}

fun main() {
    fun parse(input: List<String>) = input.map {
        it.split(",").let { (x, y, z) ->
            Day18.Point(x.toInt(), y.toInt(), z.toInt())
        }
    }

    fun calcSurface(list: List<Day18.Point>): Int {
        // if 2 axis are same and have a continuous value on 1 axis,
        // surface count can be subtracted by 2
        val dups = listOf(
            Triple(0, 1, 2),
            Triple(1, 2, 0),
            Triple(2, 0, 1),
        ).sumOf { pattern ->
            list.groupBy {
                it[pattern.first] to it[pattern.second]
            }.map { (_, points) ->
                points.sortedBy { it[pattern.third] }
            }.sumOf { points ->
                points.windowed(2).filter { (a, b) -> b[pattern.third] - a[pattern.third] == 1 }.size
            }
        }
        return 6 * list.size - 2 * dups
    }

    fun part1(input: List<String>): Int {
        val list = parse(input)
        return calcSurface(list)
    }

    fun part2(input: List<String>): Int {
        // calc like a mould.
        // 1. check outline of droplets
        // 2. deal as input that is not judged as outline
        // 3. calc surface (same as part1)
        val list = parse(input).map { (x, y, z) ->
            // slide points enable to calc
            Day18.Point(x + 1, y + 1, z + 1)
        }
        val width = list.maxOf(Day18.Point::x) + 2 // max(x) + 1
        val height = list.maxOf(Day18.Point::y) + 2 // max(y) + 1
        val depth = list.maxOf(Day18.Point::z) + 2 // max(z) + 1

        val area = Array(width) { Array(height) { Array(depth) { 0 } } }
        list.forEach { point ->
            area[point.x][point.y][point.z] = 1
        }

        val queue = ArrayDeque(listOf(Day18.Point(0, 0, 0)))
        while (queue.isNotEmpty()) {
            val (x, y, z) = queue.removeLast()
            area[x][y][z] = 2
            listOf(
                Triple(-1, 0, 0), // left
                Triple(1, 0, 0), // right
                Triple(0, -1, 0), // up
                Triple(0, 1, 0), // down
                Triple(0, 0, -1), // front
                Triple(0, 0, 1), // back
            ).forEach { (dx, dy, dz) ->
                if (
                    x + dx in 0 until width &&
                    y + dy in 0 until height &&
                    z + dz in 0 until depth &&
                    area[x + dx][y + dy][z + dz] == 0
                ) {
                    queue.add(Day18.Point(x + dx, y + dy, z + dz))
                }
            }
        }
        val modeledPoints = buildList {
            for (x in 0 until width) {
                for (y in 0 until height) {
                    for (z in 0 until depth) {
                        if (area[x][y][z] <= 1) {
                            add(Day18.Point(x, y, z))
                        }
                    }
                }
            }
        }

        return calcSurface(modeledPoints)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 64)
    check(part2(testInput) == 58)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}