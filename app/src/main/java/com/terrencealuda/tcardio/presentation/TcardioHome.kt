package com.terrencealuda.tcardio.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.OnlinePrediction
import androidx.compose.material.icons.rounded.StackedLineChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.wear.compose.material.*
import com.terrencealuda.tcardio.MainViewModel
import com.terrencealuda.tcardio.UiState
import com.terrencealuda.tcardio.presentation.theme.TCardioTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.terrencealuda.tcardio.R

@AndroidEntryPoint
class TcardioHome : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var heartRateBpm: String = "0"
        var calMeasured: String = "221.0"
        var thalachh: String = "121"

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                when (result) {
                    true -> {
                        Log.i("TCARDIO", "Body sensors permission granted")
                        mainViewModel.togglePassiveData(true)
                    }
                    false -> {
                        Log.i("TCARDIO", "Body sensors permission not granted")
                        mainViewModel.togglePassiveData(false)
                    }
                }
            }

        permissionLauncher.launch(android.Manifest.permission.BODY_SENSORS)

        /*
        Manifest.permission.BODY_SENSORS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACTIVITY_RECOGNITION
         */

        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                var hey = mainViewModel.firsttimercheck.collect()
                if (hey.equals(false)) {
                    mainViewModel.latestAvgBpms.collect {
                        val df = DecimalFormat("#.#")
                        df.roundingMode = RoundingMode.DOWN
                        thalachh = df.format(it)
                    }
                } else {
                    thalachh = 0.0.toString()
                }


                if (hey.equals(false)) {
                    mainViewModel.lastBpm.collect {
                        heartRateBpm = it.toString()
                    }
                } else {
                    heartRateBpm = 0.0.toString()
                }

            }
        }

        setContent {
            WearApp()
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun WearApp() {

    TCardioTheme {

        val listState = rememberScalingLazyListState()

        val childrenMods = Modifier
            .fillMaxWidth()
            .padding(
                start = 12.dp,
                end = 12.dp,
                top = 8.dp,
                bottom = 8.dp
            )

        val childTextMods = Modifier
            .fillMaxWidth()
            .padding(
                start = 12.dp,
                end = 12.dp,
                top = 1.dp,
                bottom = 1.dp
            )

        val iconMods = Modifier
            .size(36.dp)
            .wrapContentSize(align = Alignment.Center)

        val chipIconMods = Modifier
            .size(24.dp)
            .wrapContentSize(align = Alignment.Center)

        Scaffold(
            timeText = {
                if (!listState.isScrollInProgress) {
                    TimeText()
                }
            },
            modifier = Modifier.padding(top = 0.dp),
            vignette = {
                Vignette(vignettePosition = VignettePosition.TopAndBottom)
            },
            positionIndicator = {
                PositionIndicator(
                    scalingLazyListState = listState
                )
            }
            ) {
                val viewModel: MainViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()
                var thalachhTxt = "0.0"
                var heartRateBpmTxt = "0.0"
                //val coroutineScope = rememberCoroutineScope()
                val firsttimer by viewModel.firsttimercheck.collectAsState(initial = true)
                //val thalachh by viewModel.latestAvgBpms.collectAsState(initial = 0.0)
                //val heartRateBpm by viewModel.latestHeartRate.collectAsState(initial = 0.0)

                if (firsttimer.equals(false)) {
                    val thalachh by viewModel.latestAvgBpms.collectAsState(initial = 0.0)
                    val heartRateBpm by viewModel.latestHeartRate.collectAsState(initial = 0.0)

                    thalachhTxt = thalachh.toString()
                    heartRateBpmTxt = heartRateBpm.toString()
                    val df = DecimalFormat("#.#")
                    df.roundingMode = RoundingMode.DOWN
                    var thalachhF = df.format(thalachh)
                    thalachhTxt = thalachhF.toString()
                }
                ScalingLazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    state = listState,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (uiState is UiState.Startup) {
                        item { CustomCircularProgressBar() }
                    } else {
                        item { ScreenTitle(childTextMods, "tCardio") }
                        item { ScreenBigTitle(childTextMods, "Today") }
                        item {


                            CardioColumn(
                                iconMods,
                                childTextMods,
                                heartRateBpmTxt,
                                "Heart rate",
                                R.drawable.cardiology_48px,
                                0xFF00cc7a
                                /*Icons.Rounded.MonitorHeart*/
                            )
                        }
                        item {


                            CardioColumn(
                                iconMods,
                                childTextMods,
                                "0",
                                "Steps",
                                R.drawable.steps_48px,
                                0xFFCD7F32
                                /*Icons.Rounded.MonitorHeart*/
                            )
                        }
                        /*item {
                            CardioColumn(
                                iconMods,
                                childTextMods,
                                "0",
                                "Steps",
                                Icons.Rounded.DirectionsWalk
                            )
                        }
                        item { ScreenTitle(
                            childTextMods
                                .size(22.dp)
                                .padding(top = 3.dp) , "Blood metrics") }*/
                        item {
                            CardioRow(
                                textMods = childTextMods,
                                countLabel1 = "0.0",
                                healthDataLabel1 = "kCal.",
                                countLabel2 = thalachhTxt,
                                healthDataLabel2 = "Thal."
                            )
                        }
                        item {


                            CardioColumnNoIcon(
                                childTextMods,
                                "52.3%",
                                "Your previous\n prediction",
                                0xFFff6b00
                                /*Icons.Rounded.MonitorHeart*/
                            )
                        }

                    /* item {
                         CardioColumnNoIcon(
                             childTextMods,
                             "1000",
                             "Calories"
                         )
                     }*/
                        item {
                            CardioChip(
                                childrenMods, chipIconMods, "Health data\nstatus",

                                R.drawable.stethoscope_48px,
                                1

                            )
                        }
                        item {
                            CardioChip(
                                childrenMods, chipIconMods, "Check prediction",
                                R.drawable.online_prediction_48px,
                                2
                            )
                        }
                        item {
                            CardioChip(
                                childrenMods, chipIconMods, "Exercises",
                                R.drawable.fitness_center,
                                3
                            )
                        }
                        item {
                            CardioChip(
                                childrenMods, chipIconMods, "Health data\nstatistics",
                                R.drawable.stacked_line_chart_48px
                            )
                        }
                        item {
                            CardioChip(
                                childrenMods, chipIconMods, "Settings",
                                R.drawable.settings_48px
                            )
                        }
                }
            }
        }
    }
}

/*@Preview(
device = Devices.WEAR_OS_SMALL_ROUND,
showSystemUi = true
)
@Composable
fun DefaultPreview() {
WearApp()
}*/