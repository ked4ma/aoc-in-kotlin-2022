/**
 * [Day07](https://adventofcode.com/2022/day/7)
 */

private class Day07 {
    sealed class Node {
        abstract val name: String

        data class File(override val name: String, val size: Int) : Node() {
            override fun toString(): String {
                return "File: $name"
            }
        }

        abstract class Dir : Node() {
            abstract val parent: Dir
            abstract val absPath: String
            val children = mutableMapOf<String, Dir>()
            val files = mutableMapOf<String, File>()

            override fun toString(): String {
                return "[Dir($name): (children: ${children}, files: ${files})]"
            }
        }

        data class Root(override val name: String) : Dir() {
            override val parent = this
            override val absPath = "/"
            override fun toString(): String {
                return super.toString()
            }
        }

        data class NDir(override val name: String, override val parent: Dir) : Dir() {
            override val absPath = "${parent.absPath}$name/"
            override fun toString(): String {
                return super.toString()
            }
        }
    }

    enum class Command {
        CD, LS
    }
}

fun main() {
    fun makeFileTree(input: List<String>): Day07.Node.Dir {
        val root: Day07.Node.Dir = Day07.Node.Root("/")
        var current: Day07.Node.Dir = root
        input.forEach { line ->
            val data = line.split(" ")
            when (line[0]) {
                '$' -> {
                    when (Day07.Command.valueOf(data[1].uppercase())) {
                        Day07.Command.LS -> return@forEach // continue
                        Day07.Command.CD -> {
                            val dest = data[2]
                            current = when (dest) {
                                "/" -> root
                                ".." -> current.parent
                                else -> current.children.getOrPut(dest) { Day07.Node.NDir(dest, current) }
                            }
                        }
                    }
                }

                else -> {
                    if (data[0] == "dir") {
                        current.children.putIfAbsent(data[1], Day07.Node.NDir(data[1], current))
                    } else { // file
                        current.files.putIfAbsent(data[1], Day07.Node.File(data[1], data[0].toInt()))
                    }
                }
            }
        }
        return root
    }

    fun dfs(tree: Day07.Node.Dir, memo: MutableMap<String, Int>): Int {
        if (tree.absPath in memo) {
            return memo.getValue(tree.absPath)
        }
        val sum = tree.children.map { (_, v) ->
            memo.getOrPut(v.absPath) { dfs(v, memo) }
        }.sum()
        val res = sum + tree.files.values.map(Day07.Node.File::size).sum()
        memo[tree.absPath] = res
        return res
    }

    fun part1(input: List<String>): Int {
        val tree = makeFileTree(input)
        val memo = mutableMapOf<String, Int>()
        dfs(tree, memo)
        return memo.values.filter { it <= 100000 }.sum()
    }

    fun part2(input: List<String>): Int {
        val tree = makeFileTree(input)
        val memo = mutableMapOf<String, Int>()
        dfs(tree, memo)
        val target = 30000000 - (70000000 - memo.getValue("/"))
        return memo.values.filter { it >= target }.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}