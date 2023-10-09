/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    // initialize a GameViewModel, using ViewModelProviders
    private lateinit var viewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )
        Log.i("GameFragment", "called ViewModelProvider.of")
        /* ##### PS: never construct view-model yourself if u did
        u end up constructing a view-model every time the fragment was created #####
        #### lifecycle lib creates ViewModel for u , u request it from ViewModelProvider */
        viewModel =  ViewModelProvider(this).get(GameViewModel::class.java)

        //binding gameViewModel f rom xml file and the viewModel
        binding.gameViewModel = viewModel

        // those 2 events calls are no longer needed here becauce there're already setup on the game_fragment.xml file
       /* binding.correctButton.setOnClickListener {
            viewModel.onCorrect()
        }
        binding.skipButton.setOnClickListener {
            viewModel.onSkip()
        }*/

        //referencing to score liveDate
        // whenever the score changes the observer will be called
        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->

            binding.scoreText.text = newScore.toString()
        })

        viewModel.word.observe(viewLifecycleOwner, Observer{  newWord ->

            binding.wordText.text = newWord.toString()
        })

        //format the time to a readable time
        viewModel.currentTime.observe(viewLifecycleOwner, Observer { newTime ->

            binding.timerText.text = DateUtils.formatElapsedTime(newTime)
        })

        //using liveData to represent the state the state of an event
        // Sets up event listening to navigate the player when the game is finished
        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { hasFinished ->

            if (hasFinished){
                gameFinished()
                viewModel.onGameFinishComplete()
            }
        })

        return binding.root
    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {

        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value?:0)
        findNavController(this).navigate(action)
        //Toast.makeText(this.activity, "Game finished", Toast.LENGTH_SHORT).show()
    }

}
