import java.lang.RuntimeException

/**
 * [Day01](https://adventofcode.com/2022/day/1)
 */
fun main() {
    fun judge(self: Shape, opponent: Shape): Int = when {
        self.winTo() == opponent -> 6
        self == opponent -> 3
        else -> 0
    }

    fun part1(input: List<String>): Int {
        fun convToStrategies(input: List<String>): List<Pair<Shape, Shape>> = input.map {
            it.split(" ").let { (l, r) ->
                Shape.parse(l) to Shape.parse(r)
            }
        }

        val strategy = convToStrategies(input)
        return strategy.sumOf { (opponent, self) ->
            judge(self, opponent) + self.score
        }
    }

    fun part2(input: List<String>): Int {
        fun convToStrategies(input: List<String>): List<Pair<Shape, String>> = input.map {
            it.split(" ").let { (l, r) ->
                Shape.parse(l) to r
            }
        }

        val strategy = convToStrategies(input)
        return strategy.sumOf { (opponent, str) ->
            val self = when (str) {
                "X" -> opponent.winTo()
                "Y" -> opponent
                "Z" -> opponent.loseTo()
                else -> throw RuntimeException()
            }
            judge(self, opponent) + self.score
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

private enum class Shape(val score: Int) {
    Rock(1) {
        override fun winTo() = Scissors
        override fun loseTo() = Paper
    },
    Paper(2) {
        override fun winTo() = Rock
        override fun loseTo() = Scissors
    },
    Scissors(3) {
        override fun winTo() = Paper
        override fun loseTo() = Rock
    };

    abstract fun winTo(): Shape
    abstract fun loseTo(): Shape

    companion object {
        fun parse(s: String) = when (s) {
            "A", "X" -> Rock
            "B", "Y" -> Paper
            "C", "Z" -> Scissors
            else -> throw RuntimeException()
        }
    }
}
