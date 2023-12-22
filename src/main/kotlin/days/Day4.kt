package days

import util.Resource

private const val input = "scratchCards"

private val example = """
    Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
    Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
    Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
    Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
    Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
""".trimIndent() to 13 // to 30

object Scratchcards {
    fun solution1(isTesting: Boolean = false) {
        val lines = if (isTesting) example.first.lines()
            else Resource.readText(input).lines()

        val sum = lines.sumOf { line ->
            val parts = line.substringAfter(": ").split(" | ")
            val winningNumbers = parts[0].split(" ").filter(String::isNotBlank).map(String::toInt)
            val actualNumbers = parts[1].split(" ").filter(String::isNotBlank).map(String::toInt)

            val matches = winningNumbers.intersect(actualNumbers.toSet()).size
            if (matches == 0) 0 else 2.pow(matches - 1)
        }

        println("The result is: $sum")
    }

    fun solution2(isTesting: Boolean = false) {
        val lines = if (isTesting) example.first.lines()
            else Resource.readText(input).lines()
        val extraCopies = mutableMapOf<Int, Int>()

        val sum = lines.mapIndexed { i, line ->
            val lineNumber = i + 1
            val parts = line.substringAfter(": ").split(" | ")
            val winningNumbers = parts[0].split(" ").filter(String::isNotBlank).map(String::toInt)
            val actualNumbers = parts[1].split(" ").filter(String::isNotBlank).map(String::toInt)

            val matches = winningNumbers.intersect(actualNumbers.toSet()).size
            val extraMatches = extraCopies[lineNumber] ?: 0
            (lineNumber + 1..lineNumber + matches).forEach {
                extraCopies.merge(it, 1 + extraMatches, Int::plus)
            }
            1 + extraMatches
        }.sum()

        println("The result is: $sum")
    }
}

private fun Int.pow(pow: Int): Int {
    var acc = 1
    repeat(pow) {
        acc *= this
    }
    return acc
}
