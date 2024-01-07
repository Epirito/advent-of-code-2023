package org.example

import java.io.File
import java.math.BigInteger
data class Mapping(val input: Range, val output: Range)
fun isDense(mappings: List<Mapping>):Boolean {
    var i = 0
    while(i<mappings.size-1) {
        if (mappings[i].input.end+BigInteger.ONE < mappings[i+1].input.start) {
            return false
        }
        i++
    }
    return true
}

fun mapRange(mappings: List<Mapping>, range: Range):List<Range>{
    val lowestRelevantMapping = mappings.indexOfFirst{it.input.start <= range.end && it.input.end >= range.start}
    if (lowestRelevantMapping==-1) {
        return listOf(range)
    }
    val highestRelevantMapping = mappings.indexOfLast{it.input.start <= range.end && it.input.end >= range.start}
    var currentValue = range.start
    var i = lowestRelevantMapping
    val result = listOf<Range>().toMutableList()
    while(i <= highestRelevantMapping) {
        val mapping = mappings[i]
        if (currentValue < mapping.input.start) {
            result.add(Range(currentValue, mapping.input.start-BigInteger.ONE))
            currentValue = mapping.input.start
        }
        val preimage = Range(listOf(currentValue, mapping.input.start).max(), listOf(range.end, mapping.input.end).min())
        result.add(Range(mapping.output.start + (preimage.start - mapping.input.start), mapping.output.start + (preimage.end - mapping.input.start)))
        currentValue = mapping.input.end+BigInteger.ONE
        if (currentValue > range.end) {
            return result
        }
        i++
    }
    result.add(Range(currentValue, range.end))
    return result
}
fun pipeMap(ranges: List<Range>, mappingsPipeline: List<List<Mapping>>):List<Range>{
    var i = 0
    var currentRanges = ranges
    while(i<mappingsPipeline.size) {
        currentRanges = currentRanges.flatMap{mapRange(mappingsPipeline[i], it)}
        i++
    }
    return currentRanges
}
object Day5Again {
    fun part2() {
        val input = File("input-5").readText()
        val rangeTerms = Regex("[0-9]+").findAll(input.slice(0..input.indexOf("\n")))
            .map{m->m.value.toBigInteger()}.toList()
        val seedRanges = listOf<Range>().toMutableList()
        println(rangeTerms)

        for(i in 0..rangeTerms.size/2-1) {
            seedRanges.add(Range(rangeTerms[i*2],rangeTerms[i*2]+rangeTerms[i*2+1]))
        }
        println(seedRanges)
        val mapTerms = Regex(":\n([0-9 ]+\n?)+").findAll(input).map{it.value}.toList()
            .map{Regex("[0-9 ]+").findAll(it).map{m->m.value}.toList().map{it.split(" ").map{it.toBigInteger()}}}
        val maps = mapTerms.map{it.map{Mapping(
            Range(it[1], it[1]+it[2]-BigInteger.ONE),
            Range(it[0], it[0]+it[2]-BigInteger.ONE)
        )}.sortedBy { it.input.start }}
        println(maps)
        println("all dense:")
        println(maps.all{isDense(it)})
        val mapped = pipeMap(seedRanges, maps)
        println(mapped)
        println(mapped.map{it.start}.min())
    }
}