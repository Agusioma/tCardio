package com.terrencealuda.tcardio.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.terrencealuda.tcardio.MainViewModel
import com.terrencealuda.tcardio.databinding.ActivityPredictionScreenBinding
import com.terrencealuda.tcardio.predictor.HeartPredictor
import com.terrencealuda.tcardio.repository.PassiveDataRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class PredictionScreen : AppCompatActivity() {

    @Inject
    lateinit var heartPredictor: HeartPredictor

    @Inject
    lateinit var repository: PassiveDataRepository

    private lateinit var binding: ActivityPredictionScreenBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var thalachh = 0.0F
        var trbps = 0.0F
        var age = 22.0F
        var sex = 1.0F
        var chol = 180.0F
        var fbs = 0.0F


        lifecycleScope.launch(Dispatchers.Main) {
            mainViewModel.latestAvgBpms.collect {
                trbps = it.toFloat()
            }

        }
        /*lifecycleScope.launch(Dispatchers.Main) {

            mainViewModel.lastBpm.collect {
                Log.i("IIII", it.toString())
            }
        }*/

        lifecycleScope.launch(Dispatchers.Main) {

            mainViewModel.maxBpm.collect {
                thalachh = it.toFloat()
            }
        }

        binding = ActivityPredictionScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {

            var pred = heartPredictor.doInference(trbps, thalachh, age, sex, chol, fbs)

            var cb = pred.get("prediction")

            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.DOWN
            val heartPred = df.format(((cb?.get(0))?.times(100)))

            lifecycleScope.launch(Dispatchers.Main) {
                repository.storePrediction(heartPred.toDouble())
            }

            binding.caption.text = heartPred.toString()+"%"
        }catch(e: Exception){
            Log.e("ERR: ", e.toString())
        }
    }
}