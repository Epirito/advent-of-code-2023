package org.example

import java.io.File

object Day4 {
    fun part1() {
        val input = File("input-4").readLines()
        val numLists = input.map{it.split(':')[1].split('|').map{Regex("[0-9]+").findAll(it).map{m->m.value}.toList()}}
        val winning = numLists.map{it[0].filter{num->it[1].any{otherNum->num==otherNum}}.size}
        val scores = winning.map{if(it===0) {0}else{Math.pow(2.toDouble(), (it-1).toDouble()).toInt()}}
        println(scores.sum())
    }
    fun part2() {
        val input = File("input-4").readLines()
        val numLists = input.map{it.split(':')[1].split('|').map{Regex("[0-9]+").findAll(it).map{m->m.value}.toList()}}
        val winning = numLists.map{it[0].filter{num->it[1].any{otherNum->num==otherNum}}.size}
        //val scores = winning.map{if(it===0) {0}else{Math.pow(2.toDouble(), (it-1).toDouble()).toInt()}}
        val copies = MutableList(input.size, {1})
        for(i in 0..input.size-1) {
            for(j in i+1..i+winning[i]) {
                copies[j]+=copies[i]
            }
        }
        val result = copies.sum()
        println(result)
    }
}