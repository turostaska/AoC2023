package days

import util.Resource
import kotlin.math.max
import kotlin.math.min

private const val input = "fertilizer"

private val example = """
    seeds: 79 14 55 13

    seed-to-soil map:
    50 98 2
    52 50 48

    soil-to-fertilizer map:
    0 15 37
    37 52 2
    39 0 15

    fertilizer-to-water map:
    49 53 8
    0 11 42
    42 0 7
    57 7 4

    water-to-light map:
    88 18 7
    18 25 70

    light-to-temperature map:
    45 77 23
    81 45 19
    68 64 13

    temperature-to-humidity map:
    0 69 1
    1 0 69
    
    humidity-to-location map:
    60 56 37
    56 93 4
""".trimIndent() to 35

private fun LongRange.overlap(other: LongRange) =
    max(this.first, other.first)..min(this.last, other.last)

private fun <T> Collection<T>.split(predicate: (T) -> Boolean): List<List<T>> {
    if (isEmpty()) return emptyList()

    val result = mutableListOf(mutableListOf<T>())
    this.forEach { line ->
        if (predicate(line)) {
            result += mutableListOf(mutableListOf())
        } else {
            result.last() += line
        }
    }
    return result
}

object Fertilizer {
    fun solution1(isTesting: Boolean = false) {
        val lines = if (isTesting) example.first.lines()
        else Resource.readText(input).lines()

        val parts = lines.split(String::isBlank)
        var seeds = parts[0].first().substringAfter("seeds: ").split(' ').map(String::toLong).toSet()

        parts.drop(1).forEach { part ->
            val seedMapping = seeds.associateWith { it }.toMutableMap()
            part.drop(1).forEach { line ->
                val numbers = line.split(' ').map(String::toLong)
                val sourceRangeStart = numbers[1]
                val destinationRangeStart = numbers[0]
                val rangeLength = numbers[2]
                val diff = destinationRangeStart - sourceRangeStart
                seeds.forEach { seed ->
                    if (seed in (sourceRangeStart..<sourceRangeStart + rangeLength))
                        seedMapping[seed] = seed + diff
                }
            }
            seeds = seedMapping.values.toSet()
        }

        println("The result is: ${seeds.min()}")
    }

    fun solution2(isTesting: Boolean = false) {
        val lines = if (isTesting) example.first.lines()
        else Resource.readText(input).lines()

        val parts = lines.split(String::isBlank)
        var seeds = parts[0].first()
            .substringAfter("seeds: ")
            .split(' ')
            .map(String::toLong)
            .windowed(size = 2, step = 2)
            .map { it[0]..<it[0] + it[1] }
            .toSet()

        parts.drop(1).forEach { part ->
            val seedMapping = seeds.associateWith { it }.toMutableMap()
            part.drop(1).map { line ->
                val numbers = line.split(' ').map(String::toLong)
                val sourceRangeStart = numbers[1]
                val destinationRangeStart = numbers[0]
                val rangeLength = numbers[2]
                val diff = destinationRangeStart - sourceRangeStart

                val sourceRange = sourceRangeStart..<sourceRangeStart + rangeLength
                seeds.forEach seed@{ seed ->
                    val overlap = seed.overlap(sourceRange)
                    if (overlap.isEmpty()) return@seed
                    seedMapping -= seed
                    seedMapping[overlap] = (overlap.first + diff..overlap.last + diff)
                }
            }
            seeds = seedMapping.values.toSet()
        }

        println("The result is: ${seeds.min()}")
    }
}

private fun Set<LongRange>.min(): Long {
    var min = first().first
    forEach {
        min = min(min, min(it.first, it.last))
    }
    return min
}
