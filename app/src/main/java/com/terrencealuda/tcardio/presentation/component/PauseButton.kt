@file:OptIn(ExperimentalHorologistApi::class)

package com.terrencealuda.tcardio.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material.Button
import com.terrencealuda.tcardio.R
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.material.Icon

@Composable
fun PauseButton(onPauseClick: () -> Unit) {
    Button(onClick = {
        onPauseClick()
    }) {
        Icon(
            imageVector = Icons.Default.Pause,
            contentDescription = stringResource(id = R.string.pauseOrResume)
        )
    }
}