package com.terrencealuda.tcardio.presentation

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.terrencealuda.tcardio.MainViewModel
import com.terrencealuda.tcardio.UiState
import com.terrencealuda.tcardio.daggerhilt.TAG
import com.terrencealuda.tcardio.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                when (result) {
                    true -> {
                        Log.i(TAG, "Body sensors permission granted")
                        viewModel.togglePassiveData(true)
                    }
                    false -> {
                        Log.i(TAG, "Body sensors permission not granted")
                        viewModel.togglePassiveData(false)
                    }
                }
            }

        binding.enablePassiveData.isVisible = false

        binding.enablePassiveData.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Make sure we have the necessary permission first.
                permissionLauncher.launch(Manifest.permission.BODY_SENSORS)
            } else {
                viewModel.togglePassiveData(false)
            }
        }

        // Bind viewmodel state to the UI.
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                updateViewVisiblity(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.latestHeartRate.collect {
                binding.lastMeasuredValue.text = it.toString()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.passiveDataEnabled.collect {
                binding.enablePassiveData.isChecked = it
            }
        }
    }

    private fun updateViewVisiblity(uiState: UiState) {
        (uiState is UiState.Startup).let {
            binding.progress.isVisible = it
        }
        // These views are visible when heart rate capability is not available.
        (uiState is UiState.HealthDataNotAvailable).let {
            //binding.brokenHeart.isVisible = it
            //binding.notAvailable.isVisible = it
        }
        // These views are visible when the capability is available.
        (uiState is UiState.HealthDataAvailable).let {
            /// binding.enablePassiveData.isVisible = it
            binding.lastMeasuredLabel.isVisible = it
            binding.lastMeasuredValue.isVisible = it
            binding.heart.isVisible = it
        }
    }
}