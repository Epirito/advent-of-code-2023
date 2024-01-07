package org.example

import BigSqrt
import java.io.File
import java.math.BigInteger

fun lowestAcc(t:Int, d: Int,startFrom: Int):Int {
    for (acc in startFrom..(t-1)) {
        if (acc * (t - acc) > d) return acc
    }
    throw Exception("No solution")
}
fun lowestAcc2(t:BigInteger, d: BigInteger,startFrom: BigInteger):BigInteger {
    var acc = startFrom
    while(acc <= (t-BigInteger.ONE)) {
        if (acc * (t - acc) > d) return acc
        acc++
    }
    throw Exception("No solution")
}
fun highestAcc(t:Int, d: Int, startFrom:Int):Int {
    for (acc in startFrom downTo 1) {
        if (acc * (t - acc) > d) return acc
    }
    throw Exception("No solution")
}
fun highestAcc2(t:BigInteger, d: BigInteger, startFrom:BigInteger):BigInteger {
    var acc = startFrom
    while(acc >= BigInteger.ONE) {
        if (acc * (t - acc) > d) return acc
        acc--
    }
    throw Exception("No solution")
}
fun solution(t:Int, d: Int):Int {
    val lowest = Math.ceil((t-Math.sqrt(Math.pow(t.toDouble(),(2).toDouble()) - 4*d))/2)
    val highest = Math.floor((Math.sqrt(Math.pow(t.toDouble(),(2).toDouble()) - 4*d) + t)/2)
    return highestAcc(t,d,highest.toInt()) - lowestAcc(t,d,lowest.toInt()) + 1
}
fun solution2(t:BigInteger, d: BigInteger):BigInteger {
    val lowest = (t-BigSqrt.sqrt(t*t - 4.toBigInteger()*d))/2.toBigInteger()
    val highest = (BigSqrt.sqrt(t*t - 4.toBigInteger() *d) + t)/2.toBigInteger()
    return highestAcc2(t,d,highest) - lowestAcc2(t,d,lowest) + BigInteger.ONE
}

object Day6 {
    fun part1(){
        val input = File("input-6").readLines()
        val timeTerms = Regex("[0-9]+").findAll(input[0]).map{it.value.toInt()}.toList()
        val distanceTerms = Regex("[0-9]+").findAll(input[1]).map{it.value.toInt()}.toList()
        val results = timeTerms.zip(distanceTerms).map{(t,d)->solution(t,d)}
        println(results)
        val result = results
            .fold(1){acc, x->acc*x}
        println(result)
    }
    fun part2(){
        val input = File("input-6").readLines()
        val timeTerm = Regex("[0-9]+").findAll(input[0]).map{it.value}.toList().joinToString("").toBigInteger()
        println(timeTerm)

        val distanceTerm = Regex("[0-9]+").findAll(input[1]).map{it.value}.toList().joinToString("").toBigInteger()
        println(distanceTerm)
        val result = solution2(timeTerm,distanceTerm)
        println(result)
    }
}