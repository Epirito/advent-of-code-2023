package org.example

import java.io.File

class Day1 {
    fun part2() {
        val input = File("input-1").readLines()
        // regex to find all occurences of the string "one" or "two"
        val numNames = listOf("zero","one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        val regex = Regex("[0-9]|o(?=ne)|t(?=wo)|t(?=hree)|f(?=our)|f(?=ive)|s(?=ix)|s(?=even)|e(?=ight)|n(?=ine)")
        val occurrences = input.map { (regex.findAll(it).map(fun(occ):Int {
            val num = occ.value.toIntOrNull()
            if (num != null) return num
            return numNames.indexOf(numNames.find {name -> Regex("^${name}").containsMatchIn(it.slice(IntRange(occ.range.first(), it.length-1)))})
        }))  }
        val firstAndLast = occurrences.map { Pair(it.firstOrNull(), it.lastOrNull()) }
        println(firstAndLast.zip(input))
        val decimals = firstAndLast.map { (it.first.toString() + it.second.toString()).toInt() }
        val sum = decimals.sum()
        println(sum)
    }
}