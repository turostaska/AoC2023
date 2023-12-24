package days

import util.Resource

private const val input = "camelCards"

private val example = """
    32T3K 765
    T55J5 684
    KK677 28
    KTJJT 220
    QQQJA 483
""".trimIndent() to 6440

enum class Card {
    `2`, `3`, `4`, `5`, `6`, `7`, `8`, `9`, T, J, Q, K, A
}

data class Hand(val cards: List<Card>) {
    init {
        require(cards.size == 5)
    }

    constructor(text: String): this(text.map { Card.valueOf(it.toString()) })

    val value: Int = run {
        val numberOfCards = cards.groupingBy { it }.eachCount().values.sortedDescending()
        val mostFrequent = numberOfCards.first()
        when {
            mostFrequent == 5 -> 100
            mostFrequent == 4 -> 90
            mostFrequent == 3 && numberOfCards[1] == 2 -> 80
            mostFrequent == 3 -> 70
            mostFrequent == 2 && numberOfCards[1] == 2 -> 60
            mostFrequent == 2 -> 50
            else -> 0
        }
    }
}

object CamelCards {
    fun solution1(isTesting: Boolean = false) {
        val lines = if (isTesting) example.first.lines()
            else Resource.readText(input).lines()

        val hands = lines.map { line ->
            val parts = line.split(" ")
            val hand = parts[0].let(::Hand)
            val bid = parts[1].toLong()
            hand to bid
        }

        val orderedHands = hands.sortedWith(
            compareBy<Pair<Hand, Long>> { it.first.value }
                .then(compareBy { it.first.cards[0] })
                .then(compareBy { it.first.cards[1] })
                .then(compareBy { it.first.cards[2] })
                .then(compareBy { it.first.cards[3] })
                .then(compareBy { it.first.cards[4] })
        )

        val prod = orderedHands.map { it.second }.mapIndexed { index, it -> it * (index + 1) }.sum()
        println("The result is: $prod")
    }
}
