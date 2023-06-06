package com.terrencealuda.tcardio.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        //if (!allPermissionsGranted()) ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
      /* permissionLauncher =
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
            }*/
        if (!allPermissionsGranted()) ActivityCompat.requestPermissions(this,
            REQUIRED_PERMISSIONS,
            REQUEST_CODE_PERMISSIONS
        )

        /*permissionLauncher.launch(Manifest.permission.BODY_SENSORS)
        permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)*/

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


                /* latestNewsViewModel.uiState.collect { uiState ->
                     // New value received
                     when (uiState) {
                         is LatestNewsUiState.Success -> showFavoriteNews(uiState.news)
                         is LatestNewsUiState.Error -> showError(uiState.exception)
                     }
                 }*/
            }
        }

        /* lifecycleScope.launchWhenStarted {
           //  var hey = mainViewModel.firsttimercheck.collect()
           //  if(hey.equals(false)) {
                 mainViewModel.latestAvgBpms.collect {
                     val df = DecimalFormat("#.#")
                     df.roundingMode = RoundingMode.DOWN
                     thalachh = df.format(it)
                 }
          /*   }else{
                 thalachh = 0.0.toString()
             }*/
         }*/
        /* lifecycleScope.launchWhenStarted {
            // var hey = mainViewModel.firsttimercheck.collect()
            // if(hey.equals(false)) {
                 mainViewModel.lastBpm.collect {
                     heartRateBpm = it.toString()
                 }
            /* }else{
                 heartRateBpm = 0.0.toString()
             }*/


         }*/
        /*lifecycleScope.launch(Dispatchers.Main) {
            mainViewModel.lastBpm.collect {
                Log.i("IIII", it.toString())
            }
        }*/

        /*setContent {
            WearApp(heartRateBpm, calMeasured, thalachh)
        }*/
        setContent {
            WearApp()
        }
        /*setContent {
            WearApp(heartRateBpm, calMeasured, thalachh)
        }*/
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                Log.i("PERMISSIONS","Permissions granted by the user.")
            } else {
                Log.i("PERMISSIONS","Permissions not granted by the user.")
                finish()
            }
        }
    }
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.BODY_SENSORS,
                Manifest.permission.ACTIVITY_RECOGNITION
            ).toTypedArray()
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
            var stepsTaken = "0"
            var prevPrediction = "86.0%"
            var userCals = "0.0"

            //val coroutineScope = rememberCoroutineScope()
            val firsttimer by viewModel.firsttimercheck.collectAsState(initial = true)
            //val thalachh by viewModel.latestAvgBpms.collectAsState(initial = 0.0)
            //val heartRateBpm by viewModel.latestHeartRate.collectAsState(initial = 0.0)

            if (firsttimer.equals(false)) {
                val thalachh by viewModel.latestAvgBpms.collectAsState(initial = 0.0)
                val heartRateBpm by viewModel.latestHeartRate.collectAsState(initial = 0.0)
                val latest_cals by viewModel.latestCal.collectAsState(initial = 0.0)
                val lastTenPredictions by viewModel.lastTenPredictions.collectAsState(initial = listOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
                val latestSteps by viewModel.latestSteps.collectAsState(initial = 0)
                
                stepsTaken = latestSteps.toString()
                var tempPrevPrediction = lastTenPredictions[0].toString()
                prevPrediction = "$tempPrevPrediction%"
                userCals = latest_cals.toString()
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
                            stepsTaken,
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
                            countLabel1 = userCals,
                            healthDataLabel1 = "kCal.",
                            countLabel2 = thalachhTxt,
                            healthDataLabel2 = "Thal."
                        )
                    }
                    item {


                        CardioColumnNoIcon(
                            childTextMods,
                            prevPrediction,
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