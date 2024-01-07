package org.example

import java.io.File
data class Bid(val hand: CharArray, val bid:Int)
object Day7 {
    fun part1() {
        val values = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
        val handTypes = listOf<(hand:CharArray)->Boolean>(
            {it.all{a->a==it[0]}},
            {it.groupBy{it}.any{entry->entry.value.size==4}},
            {it.groupBy{it}.any{entry->entry.value.size==3}
                    && it.groupBy{it}.any{entry->entry.value.size==2}},
            {it.groupBy{it}.any{entry->entry.value.size==3}},
            {it.groupBy{it}.filter{entry->entry.value.size==2}.size==2},
            {it.groupBy{it}.any{entry->entry.value.size==2}},
            {it.groupBy{it}.all{entry->entry.value.size==1}}
        )
        fun sortBids(bids: List<Bid>): List<Bid> =
            bids.sortedBy{a->handTypes.indexOf(handTypes.find{cb->cb(a.hand)})*Math.pow(values.size.toDouble(),5.toDouble()) + a.hand.mapIndexed{i,c->values.indexOf(c)*Math.pow(values.size.toDouble(),(4 - i).toDouble())}.sum()}
        val input = File("input-7").readLines()
        val bids = input.map{it.split(" ")}.map{Bid(it[0].toCharArray(), it[1].toInt())}
        val sorted = sortBids(bids)
        val winnings = sorted.mapIndexed{i,it->it.bid * (sorted.size-i)}
        println(sorted.map{it.hand.map{c->c.toString()}})
        println(winnings)
        println(winnings.sum())
    }
    fun part2(){
        val values = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
        val handTypes = listOf<(hand:CharArray)->Boolean>(
            {it.filter{c->c!='J'}.let{notJ->notJ.all{a->a==notJ[0]}}},
            {it.filter{c->c!='J'}.let{notJ->
                notJ.groupBy{c->c}.any{entry->entry.value.size>=notJ.size-1}}},
            {it.filter{c->c!='J'}.let{notJ->notJ.groupBy{c->c}.toList()
                .sortedBy{-it.second.size}.let{chars->
                val jSize = it.size-notJ.size
                for(i in 0..jSize){
                    if (chars[0].second.size+i >= 3 && chars[1].second.size+jSize-i >= 2) {
                        return@let true
                    }
                }
                return@let false
            }}},
            {it.filter{c->c!='J'}.let{notJ->notJ.groupBy{c->c}.any{entry->entry.value.size >= notJ.size - 2}}},
            {it.filter{c->c!='J'}.let{notJ->notJ.groupBy{c->c}.toList().sortedBy{-it.second.size}.let{chars->
                val jSize = it.size-notJ.size
                for(i in 0..jSize){
                    if (chars[0].second.size+i >= 2 && chars[1].second.size+jSize-i >= 2) {
                        return@let true
                    }
                }
                return@let false
            }}},
            {it.filter{c->c!='J'}.let{notJ->notJ.groupBy{c->c}
                .any{entry->entry.value.size >= notJ.size - 3}}},
            {true}
        )
        val input = File("input-7").readLines()
        val bids = input.map{it.split(" ")}.map{Bid(it[0].toCharArray(), it[1].toInt())}
        val bidPower = bids.map{a->
            handTypes.indexOf(handTypes.find{cb->cb(a.hand)}) *
                999 * Math.pow(values.size.toDouble(), 5.toDouble()) +
                a.hand.mapIndexed{i,c->values.indexOf(c) *
                        Math.pow(values.size.toDouble(), (4 - i).toDouble())}.sum()}
        val ranks = bidPower.distinct().sorted()
        val byRank = ranks.map{bids.filterIndexed(){i,_->bidPower[i]==it}}
        println(byRank.flatten().filter{handTypes.indexOf(handTypes.find{cb->cb(it.hand)})==0})
        println(byRank.flatten().let{it.map{a->
            handTypes.indexOf(handTypes.find{cb->cb(a.hand)})}})
        val winnings = byRank.flatMapIndexed{i,it->it.map{bid->bid.bid * (byRank.size-i)}}
        println(winnings)
        println(winnings.sum())
        println(handTypes.indexOf(handTypes.find{cb->cb("JJJJ4".toCharArray())!!}))
    }
}