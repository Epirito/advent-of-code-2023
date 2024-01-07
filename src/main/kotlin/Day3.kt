package org.example

import java.io.File
data class MyNum(val num: Int, val range: IntRange, val y: Int)
data class Gear(val x: Int,val y: Int)
object Day3 {
    fun part1() {
        val input = File("input-3").readLines()
        //match any character except dot:
        val symbols = input.map{Regex("[^.^0-9]")
            .findAll(it).map{match->match.range.first}.toList()}
        val nums = input.mapIndexed(fun(i, it) = Regex("[0-9]+")
            .findAll(it).map{match->MyNum(match.value.toInt(), match.range, i)}
            .filter{ symbols.filterIndexed(fun(j,_)=j in i-1..i+1)
                .any{ line -> line.any{ symbol -> it.range.first <= symbol+1 && it.range.last >= symbol-1}} }.toList())
            .flatten().map{it.num}.sum()
        println(nums)
    }
    fun part2() {
        val input = File("input-3").readLines()
        //match any character except dot:
        val gears = input.mapIndexed(fun(i,it) = Regex("\\*")
            .findAll(it).map{match->Gear(match.range.first,i)}.toList()).flatten()
        val nums = input.mapIndexed(fun(i, it) = Regex("[0-9]+")
            .findAll(it).map{match->MyNum(match.value.toInt(), match.range, i)}.toList()).flatten()
        val numsByGear = gears.map{gear ->
            nums.filter { num ->
                num.y in gear.y - 1..gear.y + 1
                        && num.range.first <= gear.x + 1 && num.range.last >= gear.x - 1
            }
        }
        println(numsByGear.filter{it.size==2}.map{it[0].num*it[1].num}.sum())
    }
}