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

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.R
import java.util.Timer

public enum class CountdownStatus {
    Playing,
    Paused,
    Finished
}

class CountdownViewModel : ViewModel() {

    private var _isPlaying = MutableLiveData(CountdownStatus.Paused)
    val isPlaying: LiveData<CountdownStatus> = _isPlaying

    private var _secondsLeft = MutableLiveData(0)
    val secondsLeft: LiveData<Int> = _secondsLeft

    private var _progress = MutableLiveData(0f)
    val progress: LiveData<Float> = _progress

    private var timer: Timer? = null

    private var totalSeconds = 0

    var mediaPlayer: MediaPlayer? = null

    fun toggleTimer(context: Context) {

        val status = _isPlaying.value!!

        when (status) {
            CountdownStatus.Finished -> {
                _isPlaying.value = CountdownStatus.Paused
                stopSound()
            }
            CountdownStatus.Playing -> {
                _isPlaying.value = CountdownStatus.Paused
                stopSound()
                timer?.cancel()
                timer = null
            }
            CountdownStatus.Paused -> {
                if (_secondsLeft.value!! > 0) {
                    _isPlaying.value = CountdownStatus.Playing

                    val newTimer = object : CountDownTimer(totalSeconds.toLong() * 1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val seconds = (millisUntilFinished / 1000).toInt()
                            Log.d("TAG", "seconds: $seconds")
                            _secondsLeft.postValue(seconds + 1)
                            _progress.postValue((1 - ((seconds + 1).toFloat() / totalSeconds)))
                        }

                        override fun onFinish() {
                            Log.d("TAG", "finish")
                            _secondsLeft.postValue(0)
                            _progress.postValue((1 - (0f / totalSeconds)))
                            _isPlaying.postValue(CountdownStatus.Finished)
                            playSound(context)
                        }
                    }
                    newTimer.start()
                }
            }
        }
    }

    fun addSeconds(seconds: Int) {
        val newSeconds: Int = _secondsLeft.value!! + seconds
        _secondsLeft.value = newSeconds
        totalSeconds = newSeconds
    }

    fun removeSeconds(seconds: Int) {
        val newSeconds: Int = (_secondsLeft.value!! - seconds).coerceAtLeast(0)
        _secondsLeft.value = newSeconds
        totalSeconds = newSeconds
    }

    fun playSound(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.sound)
            mediaPlayer!!.isLooping = true
            mediaPlayer!!.start()
        } else mediaPlayer!!.start()
    }

    fun stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }
}
