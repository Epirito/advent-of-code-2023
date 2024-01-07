package org.example

import java.io.File
import kotlin.math.max

data class Play(val blue: Int, val red: Int, val green: Int)
data class Game(val n:Int, val plays: List<Play>)
object Day2 {
    fun part1() {
        val games = games()
        val possible = games.filter {it.plays.all{play->play.red <=12 && play.blue <= 14 && play.green <= 13}}
        println(possible.sumOf { it.n })
    }

    private fun games(): List<Game> {
        val input = File("input-2").readLines()
        val games = input.map { it.split(" ") }
            .mapIndexed(fun(i, it): Game {
                val plays = mutableListOf<Play>()
                var playTerms = it.slice(2..it.size - 1)
                while (playTerms.size >= 2) {
                    var blue = 0
                    var red = 0
                    var green = 0
                    while (true) {
                        val numberTerm = playTerms[0]
                        val colorTerm = playTerms[1]
                        val (color, separator) = Regex("([a-z]+)(;|,|$)")
                            .find(colorTerm)!!.destructured
                        when (color) {
                            "blue" -> blue += numberTerm.toInt()
                            "red" -> red += numberTerm.toInt()
                            "green" -> green += numberTerm.toInt()
                        }
                        playTerms = playTerms.slice(2..playTerms.size - 1)
                        if (separator == ";" || separator == "") break
                    }
                    plays.add(Play(blue, red, green))
                }
                return Game(i + 1, plays)
            })
        return games
    }

    fun part2() {
        val powers = games()
            .map{it.plays.fold(Play(0,0,0), {acc, play ->
                Play(max(acc.blue, play.blue), max(acc.red, play.red), max(acc.green, play.green))})}
            .map{it.blue*it.red*it.green}
        println(powers.sum())
    }
}