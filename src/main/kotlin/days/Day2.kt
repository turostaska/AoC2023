package days

import util.Resource

private const val input = "cubeConundrum"

private class Color(var min: Int)

private class Game(
    val blue: Color = Color(0),
    val red: Color = Color(0),
    val green: Color = Color(0),
) {
    val isPossible: Boolean
        get() = red.min <= 12
                && green.min <= 13
                && blue.min <= 14

    operator fun get(color: String) = when(color) {
        "blue" -> blue
        "red" -> red
        "green" -> green
        else -> error("No such color")
    }

    val power: Int
        get() = blue.min * red.min * green.min
}

object CubeConundrum {
    fun solution() {
        val lines = Resource.readText(input).lines()
        val sum = lines.mapIndexed { index, line ->
            val game = Game()

            val rounds = line
                .substringAfter(": ")
                .split("; ")

            rounds.forEach { round ->
                val draws = round.split(", ")
                draws.forEach { draw ->
                    val color = draw.substringAfter(' ')
                    val quantity = draw.substringBefore(' ').toInt()
                    game[color].min = maxOf(game[color].min, quantity)
                }
            }

//            if (game.isPossible) (index + 1) else 0
            game.power
        }.sum()
        println("The result is: $sum")
    }

}