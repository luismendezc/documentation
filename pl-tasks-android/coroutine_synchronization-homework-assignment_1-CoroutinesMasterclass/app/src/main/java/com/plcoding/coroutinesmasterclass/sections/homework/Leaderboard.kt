package com.plcoding.coroutinesmasterclass.sections.homework

import androidx.compose.runtime.traceEventEnd
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Leaderboard {

    private val mutex = Mutex()

    private var topThreeScores = listOf<MutableMap.MutableEntry<String, Int>>()
    private val listeners = mutableListOf<LeaderboardListener>()
    private val leaderboard: MutableMap<String, Int> = mutableMapOf()

    fun addListener(listener: LeaderboardListener) {
        this.listeners.add(listener)
    }

    fun removeListener(listener: LeaderboardListener) {
        this.listeners.remove(listener)
    }

    suspend fun updateScore(playerName: String, score: Int){
        mutex.withLock {
            leaderboard[playerName] = score
            val topThree = leaderboard
                .entries
                .sortedByDescending { it.value }
                .take(3)

            if (topThree == topThreeScores) {
                return
            }
            topThreeScores = topThree
            val result = topThree.withIndex()
                .joinToString("\n") { (index, entry) ->
                    "#${index + 1} is ${entry.key} with ${entry.value} points"
                }
            listeners.forEach {
                it.onLeaderboardUpdated(result)
            }
        }
    }





}