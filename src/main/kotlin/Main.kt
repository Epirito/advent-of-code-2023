package org.example
import java.io.File
import java.nio.file.Paths
import java.util.stream.IntStream
import kotlin.streams.toList

fun main() {
    val input = Day8.parse(File("input-8").readText())
    print(input.rules.size * input.roads.size)
    println(input)
    println(Regex("L+").findAll(input.rules.concatToString()).map{it.value.length}.toList())
    println(Day8.solve2(input))
}