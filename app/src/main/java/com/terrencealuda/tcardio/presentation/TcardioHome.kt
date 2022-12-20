package com.terrencealuda.tcardio.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.OnlinePrediction
import androidx.compose.material.icons.rounded.StackedLineChart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.*
import com.terrencealuda.tcardio.MainViewModel
import com.terrencealuda.tcardio.presentation.theme.TCardioTheme
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat

@AndroidEntryPoint
class TcardioHome : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var heartRateBpm: String = "0"
        var calMeasured: String = "221.0"
        var thalachh: String = "121"

        lifecycleScope.launchWhenStarted{
            mainViewModel.latestAvgBpms.collect {
                val df = DecimalFormat("#.#")
                df.roundingMode = RoundingMode.DOWN
                thalachh = df.format(it)
            }

        }
        lifecycleScope.launchWhenStarted {

            mainViewModel.lastBpm.collect {
                heartRateBpm = it.toString()
            }

        }
        /*lifecycleScope.launch(Dispatchers.Main) {
            mainViewModel.lastBpm.collect {
                Log.i("IIII", it.toString())
            }
        }*/

        setContent {
            WearApp(heartRateBpm, calMeasured, thalachh)
        }

    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun WearApp(heartRateBpm: String, par1: String, par2:String) {

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
            ScalingLazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                state = listState,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { ScreenTitle(childTextMods, "tCardio") }
                item { ScreenBigTitle(childTextMods, "Today") }
                item {

                    CardioColumn(
                        iconMods,
                        childTextMods,
                        heartRateBpm,
                        "Heart rate",
                        Icons.Rounded.MonitorHeart
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
                item{
                    CardioRow(
                        textMods = childTextMods,
                        countLabel1 = par1,
                        healthDataLabel1 = "Cal",
                        countLabel2 = par2,
                        healthDataLabel2 = "Thalachh"
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
                        childrenMods, iconMods, "Health data\nstatus",
                        Icons.Outlined.HealthAndSafety,
                        1

                    )
                }
                item {
                    CardioChip(
                        childrenMods, iconMods, "Heart Attack\n prediction",
                        Icons.Rounded.OnlinePrediction,
                        2
                    )
                }
                item {
                    CardioChip(
                        childrenMods, iconMods, "Health Data\nstatistics",
                        Icons.Rounded.StackedLineChart
                    )
                }
                item {
                    CardioChip(
                        childrenMods, iconMods, "Settings",
                        Icons.Outlined.Settings
                    )
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