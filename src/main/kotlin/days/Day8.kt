package days

import util.Resource

private const val input = "hauntedWasteland"

private val example = """
    LLR

    AAA = (BBB, BBB)
    BBB = (AAA, ZZZ)
    ZZZ = (ZZZ, ZZZ)
""".trimIndent() to 6

val Pair<String, String>.left get() = first
val Pair<String, String>.right get() = second

// source: https://www.baeldung.com/kotlin/lcm
fun findLcm(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun List<Long>.findLcm(): Long {
    var result = first()
    forEach {
        result = findLcm(result, it)
    }
    return result
}

object HauntedWasteland {
    fun solution(isTesting: Boolean = false) {
        val lines = if (isTesting) example.first.lines()
        else Resource.readText(input).lines()

        val instructions = lines.first()
        val directions = lines.drop(2).associate { line ->
            val parts = line.split(" = ")
            val source = parts[0]
            val directions = parts[1].removeSurrounding("(", ")").split(", ").zipWithNext().first()

            source to directions
        }

        var numSteps = 0L
        val minStepsNeeded = mutableListOf<Long>()
//        var currentNodes = mutableListOf("AAA")
        var currentNodes = directions.keys.filter { it.endsWith('A') }.toMutableList()
        while (currentNodes.any()) {
            instructions.forEach { dir ->
                val getter = if (dir == 'L') Pair<String, String>::left else Pair<String, String>::right
                currentNodes = currentNodes.map { directions[it]!!.let(getter) }.toMutableList()
                val haltedNodes = currentNodes.filter { it.endsWith('Z') }.toSet()
                numSteps += 1
                if (haltedNodes.any()) {
                    currentNodes -= haltedNodes
                    minStepsNeeded += numSteps
                }
            }
        }

        val lcm = minStepsNeeded.findLcm()
        println("The result is: $lcm")
    }
}
