package org.example

import java.io.File
import java.math.BigInteger
import java.util.stream.IntStream
import java.util.stream.Stream
import kotlin.time.measureTime

fun mapSeed(n:BigInteger, map:List<List<BigInteger>>):BigInteger{
    val range = map.find{it[1]<=n && it[1]+it[2]>n}
    if (range!=null) {
        return range[0]+(n - range[1])
    }
    return n
}
data class Range(val start:BigInteger, val end:BigInteger)
typealias Steps = List<Pair<BigInteger,BigInteger>>
data class PiecewiseMap(val steps:Steps, val lastValue: BigInteger)
fun endOfStep(i:Int, map:PiecewiseMap):BigInteger {
    if (i<map.steps.size-1) {
        return map.steps[i+1].first - BigInteger.ONE
    }
    return map.lastValue
}
fun pipeMaps(n:BigInteger, maps:List<List<List<BigInteger>>>):BigInteger{
    var i = 0
    var currentN = n
    while(i<maps.size){
        val map = maps[i]
        currentN = mapSeed(currentN,map)
        i++
    }
    return currentN
}
fun piecewiseFromRanges(ranges: List<Pair<Range,BigInteger>>):PiecewiseMap{
    val sortedRanges = ranges.sortedBy{it.first.start}
    val result = emptyList<Pair<BigInteger,BigInteger>>().toMutableList()
    sortedRanges.forEachIndexed(fun(i,range) {
        result.add(Pair(range.first.start,range.second))
        if (i < sortedRanges.size - 1 && sortedRanges[i+1].first.start-range.first.end > BigInteger.ONE) {
            result.add(Pair(sortedRanges[i].first.end+BigInteger.ONE,sortedRanges[i].first.end))
        }
    })
    return PiecewiseMap(result, sortedRanges.last().first.end)
}
fun mapPiecewise(x: List<Range>, mapping: PiecewiseMap):List<Range> {
    var j = 0
    val result = listOf<Range>().toMutableList()


    var i = 0

    while(endOfStep(j,mapping) < x[0].start) {
        j++
        if (j==mapping.steps.size) return x
    }

    val firstMappingValue = mapping.steps[j].first
    while(i < x.size) {
        val range = x[i]
        if (range.end < firstMappingValue) {
            result.add(range)
            i++
            continue
        }
        if (range.start < firstMappingValue) {
            result.add(Range(range.start,firstMappingValue-BigInteger.ONE))
        }
        break
    }
    while(i < x.size) {
        val range = x[i]
        val endOfMappingStep = endOfStep(j,mapping)
        while(j < mapping.steps.size && mapping.steps[j].first <= range.end && endOfMappingStep >= range.start ) {
            val start = if (mapping.steps[j].first > range.start) mapping.steps[j].first else range.start
            val end = if (endOfMappingStep < range.end) endOfMappingStep else range.end
            result.add(Range(start,end))
            if (end == range.end) {
                break
            }
            j++
        }
        if (j==mapping.steps.size) {
            result.add(Range(mapping.lastValue+BigInteger.ONE,range.end))
            break
        }
        i++
    }
    while(i < x.size) {
        val range = x[i]
        result.add(range)
        i++
    }
    return result
}
fun pipePiecewiseMaps(x:List<Range>, maps:List<PiecewiseMap>):List<Range>{
    var i = 0
    var currentX = x
    while(i<maps.size){
        val map = maps[i]
        currentX = mapPiecewise(currentX.sortedBy{it.start}, map)
        println(currentX)
        i++
    }
    return currentX
}
object Day5 {
    fun part1(){
        val input = File("input-5-test").readText()
        val seeds = Regex("[0-9]+").findAll(input.slice(0..input.indexOf("\n"))).map{m->m.value.toBigInteger()}.toList()
        val maps = Regex(":\n([0-9 ]+\n?)+").findAll(input).map{it.value}.toList()
            .map{Regex("[0-9 ]+").findAll(it).map{m->m.value}.toList().map{it.split(" ").map{it.toBigInteger()}}}
        val mappedSeeds = seeds.map{ pipeMaps(it,maps) }
        println(mappedSeeds.min())
    }
    fun part2test() {
        println("hi test")
        val input = File("input-5").readText()
        val seedRanges = Regex("[0-9]+").findAll(input.slice(0..input.indexOf("\n"))).map{m->m.value.toBigInteger()}.toList()
        val seeds = MutableList(0,{BigInteger.ZERO})
        for(i in 0..seedRanges.size/2-1) {
            for(j in 0..seedRanges[i*2+1].toInt()-1) {
                seeds.add(seedRanges[i*2]+j.toBigInteger())
            }
        }
        val maps = Regex(":\n([0-9 ]+\n?)+").findAll(input).map{it.value}.toList()
            .map{Regex("[0-9 ]+").findAll(it).map{m->m.value}.toList().map{it.split(" ").map{it.toBigInteger()}}}
        val mappedSeeds = seeds.map{ pipeMaps(it,maps) }
        println(mappedSeeds.min())
    }
    fun part2brute() {
        val input = File("input-5").readText()
        val seedRanges = Regex("[0-9]+").findAll(input.slice(0..input.indexOf("\n"))).map{m->m.value.toBigInteger()}.toList()
        val seeds = MutableList(0,{BigInteger.ZERO})
        val result = listOf<BigInteger>().toMutableList()
        val maps = Regex(":\n([0-9 ]+\n?)+").findAll(input).map{it.value}.toList()
            .map{Regex("[0-9 ]+").findAll(it).map{m->m.value}.toList().map{it.split(" ").map{it.toBigInteger()}}}
        for(i in 0..<seedRanges.size/2) {
            println(measureTime {
                result.add(Stream.iterate(BigInteger.ZERO, fun(bi):Boolean {
                    return bi < seedRanges[i * 2 + 1]
                }, fun(bi) = bi+10.toBigInteger()).parallel().map { pipeMaps(it,maps) }
                    .reduce{a,b->if (a<b) {
                        a
                    } else {
                        b
                    }}.orElseThrow())
                println(result)
            })

        }

        println(result)
    }
    fun piecewiseTest() {
        println(piecewiseFromRanges(listOf(
            Pair(Range(BigInteger.ZERO,BigInteger.valueOf(7)),BigInteger.valueOf(1)),
            Pair(Range(BigInteger.valueOf(8),BigInteger.valueOf(9)),BigInteger.valueOf(100)),
            Pair(Range(BigInteger.valueOf(11),BigInteger.valueOf(15)),BigInteger.valueOf(1000)),
        )))
    }
    fun part2(){
        val input = File("input-5-test").readText()
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
        val maps = mapTerms.map{piecewiseFromRanges(it.map{Pair(Range(it[1], it[1]+it[2]),it[0])})}
        val mapped = pipePiecewiseMaps(seedRanges,maps)
    }
}