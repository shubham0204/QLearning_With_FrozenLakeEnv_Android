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

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // The IDs of the 16 imageViews present in activity_main.xml
    private val imageViewIDs = intArrayOf(
        R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4,
        R.id.imageView5, R.id.imageView6, R.id.imageView7, R.id.imageView8,
        R.id.imageView9, R.id.imageView10, R.id.imageView11, R.id.imageView12,
        R.id.imageView13, R.id.imageView14, R.id.imageView15, R.id.imageView16
    )
    private lateinit var imageViews : Array<ImageView>

    // To check whether the agent is training
    private var isTraining = false


    companion object {

        private var logTextView : TextView? = null

        // Log some text on the main screen.
        fun logText( text : String ) {
            logTextView!!.text = text
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Populate imageViews array with ImageViews with imageViewIDs
        imageViews = imageViewIDs.map{ id -> findViewById<ImageView>( id ) }.toTypedArray()

        logTextView = findViewById( R.id.logTextView )

        // Add welcome text
        logTextView!!.text = "Welcome to the Frozen Lake Environment! \uD83C\uDF8A"

    }

    // Trigger when R.id.startTraining is clicked.
    fun onStartTraining( v : View) {
        if ( !isTraining ){
            // Start the training with num_episodes = 10
            val qLearningAgent = QLearningAgent( this , imageViews )
            qLearningAgent.start( 10 )
            isTraining = true
            // Disable the "start training" button.
            v.isEnabled = false
        }

    }

}