/*
 * Copyright 2020 Shubham Panchal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ml.projects.frozenlake
import FrozenLakeEnv
import android.content.Context
import android.widget.ImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.ml.projects.frozenlake.matrix.Matrix
import matrix.MatrixOps
import java.util.*

class QLearningAgent(private var context : Context, private var imageViews : Array<ImageView> )  {

    // The learning rate for our agent.
    var learningRate = 0.98

    // Discount factor ( gamma ).
    var discountFactor = 0.6

    // Use this as a threshold for determining exploitation/exploration.
    var epsilon = 0.6

    // Frozen Lake enviroment.
    private var frozenLakeEnv: FrozenLakeEnv = FrozenLakeEnv()

    // Q Table
    private var Q : Matrix? = null

    // Some other variables which will be used for updating the Q table using Bellman's equation.
    private var currentState : Int? = null
    private var action : Int? = null

    // The index of the imageView in imageViews which contains the agent's image. Lies in [ 0 , 16 )
    private var agentImageIndex : Int = 0

    // Called in MainActivity.kt
    fun start( numEpisodes : Int ) {

        // Start training on a thread ( not the main/UI thread )
        GlobalScope.launch {

            // Init the Q table
            Q = MatrixOps.zerosLike( frozenLakeEnv.observationSpaceN , frozenLakeEnv.actionSpaceN)

            for ( episodeNum in 0 until numEpisodes ){

                // To modify the UI, use the UI thread ( as we are currently on a worker thread ).
                GlobalScope.launch( Dispatchers.Main ) {
                    MainActivity.logText( "Starting episode ${episodeNum + 1} \uD83D\uDE03")
                }

                // Reset the environment.
                currentState = frozenLakeEnv.reset()

                // Perform 100 actions ( atleast )
                for ( t in 0 until 100 ) {

                    // To modify the UI, use the UI thread ( as we are currently on a worker thread ).
                    GlobalScope.launch( Dispatchers.Main ) {
                        MainActivity.logText( "Taking step ${t+1}")
                    }

                    // To modify the UI, use the UI thread ( as we are currently on a worker thread ).
                    GlobalScope.launch( Dispatchers.Main ) {
                        // First remove the agent's image from the previous imageView
                        imageViews[ agentImageIndex ].setImageDrawable( null )
                        // Update the index
                        agentImageIndex = ( 4 * frozenLakeEnv.agentPosY )  + frozenLakeEnv.agentPosX
                        // Set the agent image in the new imageView
                        imageViews[ agentImageIndex ].setImageResource( R.drawable.agent_image )
                    }

                    // Choose an action
                    action = if ( Random().nextDouble() < epsilon ){
                        // Exploration
                        frozenLakeEnv.actionSpaceSample()
                    }
                    else {
                        // Exploitation
                        Q!!.getRow( currentState!! ).indexOf( Q!!.getRow( currentState!! ).max()!! )
                    }

                    // Perform the action in the env.
                    val envOutput = frozenLakeEnv.step( action!! )

                    // Update the Q value using Bellman's equation.
                    val p = Q?.get( currentState!! , action!! )!! +
                            learningRate *
                            ( envOutput.reward + discountFactor * ( Q!!.getRow( envOutput.newState ).max()!!) - Q?.get( currentState!! , action!! )!! )
                    Q?.set( currentState!! , action!! , p )

                    // Update the currentState ( S = S' )
                    currentState = envOutput.newState

                    // Check if the episode has terminated.
                    if ( envOutput.isTerminated ){

                        // Set text on the main screen.
                        GlobalScope.launch( Dispatchers.Main ) {
                            MainActivity.logText( "Episode Terminated \uD83D\uDE0C")
                        }

                        // Have a delay of 1s before starting a new episode.
                        delay( 1000 )

                        // Break this loop, start a new episode.
                        break
                    }
                    else {

                        // Have a delay of 0.5s to see the agent's movements.
                        delay( 500 )
                    }

                }

                GlobalScope.launch( Dispatchers.Main ) {
                    imageViews[ agentImageIndex ].setImageDrawable( null )
                    imageViews[ 0 ].setImageResource( R.drawable.agent_image )
                }

            }

            // After the training has finished, notify it to the user.
            GlobalScope.launch( Dispatchers.Main ) {
                MainActivity.logText( "Training Done \uD83D\uDE0C !")
            }

        }

    }



}