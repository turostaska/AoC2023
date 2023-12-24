package days

import util.Resource
import kotlin.math.*

private const val input = "waitForIt"

private val example = """
    Time:      7  15   30
    Distance:  9  40  200
""".trimIndent() to 288

private fun String.splitByWhitespaces() = split("\\s+".toRegex()).filter(String::isNotBlank)

// Whole number solutions for a*x^2 + b*x + c > 0
private fun solveQuadraticInequality(a: Long, b: Long, c: Long): LongRange {
    val discriminant = b * b - 4 * a * c
    val x0 = (-b + sqrt(discriminant.toDouble())) / (2 * a)
    val x1 = (-b - sqrt(discriminant.toDouble())) / (2 * a)
    val xMin = ceil(min(x0, x1)).toLong()
    val xMax = floor(max(x0, x1)).toLong()
    val xMinWhole = if (a * xMin * xMin + b * xMin + c > 0) xMin else xMin + 1
    val xMaxWhole = if (a * xMax * xMax + b * xMax + c > 0) xMax else xMax - 1
    return xMinWhole..xMaxWhole
}

object WaitForIt {
    fun solution1(isTesting: Boolean = false) {
        val lines = if (isTesting) example.first.lines()
        else Resource.readText(input).lines()

        val times = lines[0].removePrefix("Time:").splitByWhitespaces().map(String::toLong)
        val distances = lines[1].removePrefix("Distance:").splitByWhitespaces().map(String::toLong)
        val races = times.zip(distances)

        val prod = races.map { (tMax, d) ->
            val solutions = solveQuadraticInequality(-1, tMax, -d)
            (solutions.last - solutions.first + 1)
        }.reduce { acc, i -> acc * i }

        println("The result is: $prod")
    }

    fun solution2(isTesting: Boolean = true) {
        val lines = if (isTesting) example.first.lines()
        else Resource.readText(input).lines()

        val time = lines[0].removePrefix("Time:").filter { !it.isWhitespace() }.toLong()
        val distance = lines[1].removePrefix("Distance:").filter { !it.isWhitespace() }.toLong()

        // v = tPress
        // t = tTotal - tPress
        // dist < tPress * (tTotal - tPress) => 0 < -tPress^2 + tTotal * tPress - dist
        val solutions = solveQuadraticInequality(-1, time, -distance)
        val numSolutions = solutions.last - solutions.first + 1

        println("The result is: $numSolutions")
    }
}