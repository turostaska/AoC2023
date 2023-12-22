package days

import util.Resource.readText

private const val input = "trebuchet"

object Trebuchet {
    private val digits = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
    )

    private val regex = digits
        .flatMap { (k,v) -> listOf(k, v.toString()) }
        .joinToString("|")
        .toRegex()

    fun solution() {
        val lines = readText(input).lines()
        val sum = lines.sumOf { line ->
            val firstDigit = regex.find(line)!!.value.let {
                it.toIntOrNull() ?: digits[it]!!
            }
            val secondDigit = regex.findLast(line)!!.value.let {
                it.toIntOrNull() ?: digits[it]!!
            }

            10 * firstDigit + secondDigit
        }
        println("The result is: $sum")
    }
}

private tailrec fun Regex.findLast(input: String, startIndex: Int = input.lastIndex): MatchResult? {
    if (startIndex < 0) return null
    return this.find(input, startIndex) ?: findLast(input, startIndex - 1)
}

