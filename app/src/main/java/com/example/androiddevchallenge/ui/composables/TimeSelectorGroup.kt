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
package com.example.androiddevchallenge.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TimeSelectorGroup(secondsRemaining: Int, increase: (Int) -> Unit, decrease: (Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        TimeSelector(
            value = secondsRemaining / 3600,
            onIncrease = { increase(3600) },
            onDecrease = { decrease(3600) }
        )

        val secondsRemainingMinusHours = secondsRemaining % 3600
        TimeSelector(
            value = secondsRemainingMinusHours / 60,
            onIncrease = { increase(60) },
            onDecrease = { decrease(60) }
        )

        TimeSelector(
            value = secondsRemainingMinusHours % 60,
            onIncrease = { increase(1) },
            onDecrease = { decrease(1) }
        )
    }
}
