package org.example
data class InputData(val rules:CharArray, val roads: List<Pair<String, Pair<String,String>>>)
data class Entry(val road: String, val rule: Int)
data class Automaton(var current:String, val history:MutableList<Entry>,var repeating:IntRange?)

fun f(t: Int, automata: List<Automaton>):Int {
    println("all repeating")
    var currentT = t

    while(true) {
        println(currentT)
        val timeToNext = automata.map {
            val period = it.repeating!!.last - it.repeating!!.first
            (it.history.size - it.repeating!!.first) % period
        }
        currentT += timeToNext.min()
        if (timeToNext.all{it==timeToNext[0]}) return currentT
    }
}
object Day8 {
    fun solve(data:InputData):Int {
        val roads = data.roads.toMap()
        var current = "AAA"
        var i = 0
        while(true) {
            val direction = data.rules[i % data.rules.size]
            val road = roads.get(current)!!
            current = if (direction == 'L') road.first else road.second
            i++
            println(current)
            if (current=="ZZZ") return i
        }

    }
    fun solve2(data:InputData):Int {
        val roads = data.roads.toMap()
        val automata = roads.keys.filter{it.last()=='A'}.map{Automaton(it, mutableListOf(),null)}
        var i = 0
        while(true) {
            val rule = i % data.rules.size
            val direction = data.rules[rule]
            automata.forEach{
                if (it.repeating==null) {
                    val precedent = it.history.find{e->e.road==it.current && e.rule==rule}
                    if (precedent!=null) {
                        it.repeating = IntRange(it.history.indexOf(precedent), it.history.size)
                    }

                }
                it.history.add(Entry(it.current, rule))
                it.current = if (direction == 'L') roads.get(it.current)!!.first else roads.get(it.current)!!.second
            }
            if (automata.all{it.repeating!=null}) return f(i,automata)
            i++
            if (automata.all{it.current.last()=='Z'}) return i
        }
    }
    fun parseExample() {
        println(parse("LLR\n" +
                "\n" +
                "AAA = (BBB, BBB)\n" +
                "BBB = (AAA, ZZZ)\n" +
                "ZZZ = (ZZZ, ZZZ)"))
    }
    fun parse(input:String): InputData {
        val lines = input.split("\n")
        val roads = lines.filter{it.contains("=")}.map{it.split("=")}.map{it[0].trim() to Regex("[A-Z]+").findAll(it[1]).map{it.value}.toList().let{m->Pair(m[0],m[1])}}
        val rules = lines.first().toCharArray()
        return InputData(rules,roads)
    }
}