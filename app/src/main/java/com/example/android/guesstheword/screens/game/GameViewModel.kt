package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    // The current word
    private val _word = MutableLiveData<String>()
    // word (exposed externally as livedata)
    val word : LiveData<String>
        get() = _word

    // The current score (internal)
    private val _score = MutableLiveData<Int>()
    // score (exposed externally as livedata)
    val score : LiveData<Int>
        get() = _score

    //eventGameFinish
    private val _eventGameFinish = MutableLiveData<Boolean>()
    //eventGameFinish (exposed externally as livedata)
    val eventGameFinish : LiveData<Boolean>
    get() = _eventGameFinish


    // The list of words - the front of the list is the next word to guess
    lateinit var wordList: MutableList<String>

    init {
        //initializing score/word/eventGameFinish value
        _score.value = 0
        _word.value = ""
        _eventGameFinish.value = false
        //calling this functions each time the viewmodel gets created and not when the fragment gets created
        resetList()
        nextWord()
        Log.i("GameViewModel", "GameViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {

            _eventGameFinish.value = true
        } else {

            _word.value = wordList.removeAt(0)
        }
    }

    /** Methods for buttons presses **/
    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }
     fun onCorrect() {
         _score.value = (score.value)?.plus(1)
        nextWord()
    }

    //function onGameFinishComplete
    //which means that the game has completely finished
    //ensure that the toast only showed once
    fun onGameFinishComplete(){

        _eventGameFinish.value = false
    }

}