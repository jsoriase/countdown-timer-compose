/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.composables.TimeSelectorGroup
import com.example.androiddevchallenge.ui.composables.ProgressTimer

@Composable
fun CountdownScreen() {

    val countdownViewModel: CountdownViewModel = viewModel()

    val secondsLeft by countdownViewModel.secondsLeft.observeAsState(0)
    val progress by countdownViewModel.progress.observeAsState(0f)
    val status by countdownViewModel.isPlaying.observeAsState(initial = false)

    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 48.dp)
                .animateContentSize(
                    animationSpec = tween()
                )
        ) {
            if (status == CountdownStatus.Paused) {
                TimeSelectorGroup(
                    secondsRemaining = secondsLeft,
                    increase = countdownViewModel::addSeconds,
                    decrease = countdownViewModel::removeSeconds
                )
            } else {
                val hours = secondsLeft / 3600
                val secondsRemainingMinusHours = secondsLeft % 3600
                val minutes = secondsRemainingMinusHours / 60
                val seconds = secondsRemainingMinusHours % 60
                ProgressTimer(
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds,
                    progress = progress
                )
            }
        }

        FloatingActionButton(
            onClick = { countdownViewModel.toggleTimer(context) },
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Crossfade(targetState = status) {
                when (status) {
                    CountdownStatus.Paused -> {
                        Icon(
                            painter = painterResource(id = R.drawable.play),
                            contentDescription = "Start the timer"
                        )
                    }
                    CountdownStatus.Playing -> {
                        Icon(
                            painter = painterResource(id = R.drawable.pause),
                            contentDescription = "Stop the timer"
                        )
                    }
                    CountdownStatus.Finished -> {
                        Icon(
                            painter = painterResource(id = R.drawable.stop),
                            contentDescription = "Stop the timer"
                        )
                    }
                }
            }
        }
    }
}
