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


import com.ml.projects.frozenlake.matrix.Matrix
import matrix.MatrixOps
import kotlin.random.Random


// SFFF       (S: starting point, safe)
// FHFH       (F: frozen surface, safe)
// FFFH       (H: hole, fall to your doom)
// HFFG       (G: goal, where the frisbee is located)

// actions( index , direction ) : ( 0 , left ) , ( 1 , up ) , ( 2 , right ) , ( 3 , down )

// Class for the Frozen Lake Enviroment
class FrozenLakeEnv() {

    companion object {
        val ACTION_LEFT = 0
        val ACTION_UP = 1
        val ACTION_RIGHT = 2
        val ACTION_DOWN = 3
    }

    private lateinit var envMatrix : Matrix

    // Initial position of the agent ( the S state ).
    var agentPosX = 0
    var agentPosY = 0
    // Number of actions and states
    var actionSpaceN : Int = 4
    var observationSpaceN : Int = 16

    init {
        initEnvMatrix()
    }

    // Perform an action in the environment.
    fun step( action : Int ) : EnvOutput {
        var agentNewPosX : Int = agentPosX
        var agentNewPosY : Int = agentPosY
        // Update the agent's position for subtracting/adding a 1 to it's existing position.
        when( action ) {
            ACTION_LEFT -> agentNewPosX = agentPosX - 1
            ACTION_UP -> agentNewPosY = agentPosY - 1
            ACTION_RIGHT -> agentNewPosX = agentPosX + 1
            ACTION_DOWN -> agentNewPosY = agentPosY + 1
        }
        // Check if the updated positions are valid.
        if ( agentNewPosX in 0..3 && agentNewPosY in 0..3 ) {
            agentPosX = agentNewPosX
            agentPosY = agentNewPosY
            val reward = getReward( agentPosX , agentPosY )
            return EnvOutput(
                    getStateFromPos( agentPosX , agentPosY ) ,
                    reward ,
                    // A reward of -1 means that we fell into the hole! The episode has ended here ...
                    reward == -1.0
            )

        }
        else {
            return EnvOutput(
                    getStateFromPos( agentPosX , agentPosY ) ,
                    0.0 ,
                    false)
        }
    }

    class EnvOutput( var newState : Int ,  var reward : Double , var isTerminated : Boolean )

    // Get a random action ( Just as env.actionSpace.sample() in Python )
    fun actionSpaceSample() : Int {
        return Random.nextInt( actionSpaceN )
    }

    // Reset the positions of the agent ( Just as env.reset() in Python )
    fun reset() : Int {
        agentPosY = 0
        agentPosX = 0
        return getStateFromPos( agentPosX , agentPosY )
    }

    // Get the index for the current state using the agent's position.
    private fun getStateFromPos( agentPosX : Int , agentPosY : Int ) : Int {
        return agentPosX + ( 4 * agentPosY )
    }

    // Get a reward for the given position.
    private fun getReward( agentPosX : Int , agentPosY : Int ) : Double {
        return envMatrix.get( agentPosX , agentPosY )
    }

    private fun initEnvMatrix() {
        envMatrix = MatrixOps.zerosLike( 4 , 4 )
        // Set -1 at the positions of the "holes"
        envMatrix.set( 1 , 1  , -1.0 )
        envMatrix.set( 3 , 1  , -1.0 )
        envMatrix.set( 3 , 2 , -1.0 )
        envMatrix.set( 0 , 3 , -1.0 )
        // Set 1 at the position of the goal
        envMatrix.set( 3 , 3 , 1.0 )
    }

}
