package days

import util.Resource

private const val input = "gearRatios"

/**
--- Day 3: Gear Ratios ---
You and the Elf eventually reach a gondola lift station; he says the gondola lift will take you up to the water source,
but this is as far as he can bring you. You go inside.

It doesn't take long to find the gondolas, but there seems to be a problem: they're not moving.

"Aaah!"

You turn around to see a slightly-greasy Elf with a wrench and a look of surprise. "Sorry, I wasn't expecting anyone!
The gondola lift isn't working right now; it'll still be a while before I can fix it." You offer to help.

The engineer explains that an engine part seems to be missing from the engine, but nobody can figure out which one.
If you can add up all the part numbers in the engine schematic, it should be easy to work out which part is missing.

The engine schematic (your puzzle input) consists of a visual representation of the engine. There are lots of numbers
and symbols you don't really understand, but apparently any number adjacent to a symbol, even diagonally, is a
"part number" and should be included in your sum. (Periods (.) do not count as a symbol.)

Here is an example engine schematic:

467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
In this schematic, two numbers are not part numbers because they are not adjacent to a symbol: 114 (top right) and 58
(middle right). Every other number is adjacent to a symbol and so is a part number; their sum is 4361.

Of course, the actual engine schematic is much larger. What is the sum of all of the part numbers in the engine schematic?
 **/

private val example = """
    467..114..
    ...*......
    ..35..633.
    ......#...
    617*......
    .....+.58.
    ..592.....
    ......755.
    ...${'$'}.*....
    .664.598..
""".trimIndent() to 4361

private val Char.isSymbol
    get() = this != '.' && !this.isDigit()

object GearRatios {
    fun solution1(isTesting: Boolean = false) {
        val lines = if (isTesting) example.first.lines()
        else Resource.readText(input).lines()

        val sum = lines.flatMapIndexed { i, line ->
            "[1-9][0-9]*".toRegex().findAll(line).filter { match ->
                (-1..1).forEach { yOffset ->
                    (match.range.first - 1..match.range.last + 1).forEach { x ->
                        val y = i + yOffset
                        val char: Char? = lines.getOrNull(y)?.getOrNull(x)
                        if (char?.isSymbol == true)
                            return@filter true
                    }
                }
                false
            }
        }.sumOf { it.value.toInt() }

        println("The result is: $sum")
    }

    fun solution2(isTesting: Boolean = false) {
        val lines = if (isTesting) example.first.lines()
            else Resource.readText(input).lines()
        val gears = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()

        val sum = lines.flatMapIndexed { i, line ->
            val numberRegex = "[1-9][0-9]*".toRegex()
            numberRegex.findAll(line).map { match ->
                (-1..1).forEach { yOffset ->
                    (match.range.first - 1..match.range.last + 1).forEach { x ->
                        val y = i + yOffset
                        val char: Char? = lines.getOrNull(y)?.getOrNull(x)
                        if (char != '*') return@map 0
                        val number = match.value.toInt()

                        gears.merge(y to x, mutableListOf(number)) {
                            oldValue, newValue -> oldValue.apply { this += newValue }
                        }

                        val gear = gears[y to x]
                        val gearSize = gear!!.size
                        when (gearSize) {
                            2 -> return@map gear[0] * gear[1]
                            3 -> return@map -(gear[0] * gear[1])
                            else -> return@map 0
                        }
                    }
                }
                0
            }
        }.sum()

        println("The result is: $sum")
    }
}