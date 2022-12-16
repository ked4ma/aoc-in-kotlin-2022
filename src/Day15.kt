import kotlin.math.abs
import kotlin.math.max

/**
 * [Day15](https://adventofcode.com/2022/day/15)
 */

private class Day15 {
    data class Point(val x: Int, val y: Int)
}

fun main() {
    fun toDetects(input: List<String>): List<Pair<Day15.Point, Day15.Point>> = input.map { line ->
        """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)"""
            .toRegex().find(line)!!.groupValues.let { (_, sx, sy, bx, by) ->
                Day15.Point(sx.toInt(), sy.toInt()) to Day15.Point(bx.toInt(), by.toInt())
            }
    }

    fun mergeRange(ranges: List<IntRange>): List<IntRange> {
        // [0].first <= [1].first <= ...
        var left = ranges.sortedBy(IntRange::first)
        val group = mutableListOf<IntRange>()
        while (left.isNotEmpty()) {
            var base = left.first()
            val sub = left.takeWhile {
                when {
                    // base.first <= it.first must be true because ranges sorted by first
                    // if 'it' and base is cross, first of 'it' should be smaller than or equal to last of 'base'
                    it.first <= base.last -> {
                        base = base.first..max(base.last, it.last)
                        true
                    }

                    else -> false
                }
            }
            group.add(base)
            left = left.subList(sub.size, left.size)
        }
        return group
    }

    fun calcCoveredRanges(detects: List<Pair<Day15.Point, Day15.Point>>, targetRow: Int): List<IntRange> {
        val ranges = detects.map { (sensor, beacon) ->
            val distance = abs(sensor.x - beacon.x) + abs(sensor.y - beacon.y)
            val rem = distance - abs(sensor.y - targetRow)
            (sensor.x - rem..sensor.x + rem)
        }.filterNot(IntRange::isEmpty)

        return mergeRange(ranges)
    }

    fun part1(input: List<String>, targetRow: Int): Int {
        val detects = toDetects(input)

        val coveredRanges = calcCoveredRanges(detects, targetRow)
        val overlaps = detects.flatMap(Pair<Day15.Point, Day15.Point>::toList).toSet().count { p ->
            p.y == targetRow && coveredRanges.any { it.contains(p.x) }
        }

        return coveredRanges.sumOf(IntRange::count) - overlaps
    }

    fun part2(input: List<String>): Long {
        val detects = toDetects(input)

        val (sMinX, sMaxX, sMinY, sMaxY) = detects.map(Pair<Day15.Point, Day15.Point>::first).let { list ->
            val (minX, maxX) = list.map(Day15.Point::x).let { it.min() to it.max() }
            val (minY, maxY) = list.map(Day15.Point::y).let { it.min() to it.max() }
            listOf(minX, maxX, minY, maxY)
        }
        for (y in sMinY..sMaxY) {
            val coveredRanges = calcCoveredRanges(detects, y)
            if (!coveredRanges.any { it.first <= sMinX && sMaxX <= it.last }) {
                val x = coveredRanges[1].first - 1
                return x.toLong() * 4_000_000L + y.toLong()
            }
        }

        return -1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput) == 56_000_011L)

    val input = readInput("Day15")
    println(part1(input, 2_000_000))
    println(part2(input))
}