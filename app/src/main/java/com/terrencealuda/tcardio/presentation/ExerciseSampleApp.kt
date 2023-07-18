/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:OptIn(ExperimentalHorologistApi::class)

package com.terrencealuda.tcardio.presentation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.composable
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.navscaffold.WearNavScaffold
import com.google.android.horologist.compose.navscaffold.scrollable
import com.terrencealuda.tcardio.presentation.dialogs.ExerciseNotAvailable
import com.terrencealuda.tcardio.presentation.exercise.ExerciseRoute
import com.terrencealuda.tcardio.presentation.preparing.PreparingExerciseRoute
import com.terrencealuda.tcardio.presentation.summary.SummaryRoute

/** Navigation for the exercise app. **/

@Composable
fun ExerciseSampleApp(
    navController: NavHostController,
    onFinishActivity: () -> Unit
) {
    WearNavScaffold(
        navController = navController,
        startDestination = Screen.Exercise.route
    ) {
        composable(Screen.PreparingExercise.route) {
            PreparingExerciseRoute(
                onStart = {
                    navController.navigate(Screen.Exercise.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = false
                        }
                    }
                },
                onNoExerciseCapabilities = {
                    navController.navigate(Screen.ExerciseNotAvailable.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = false
                        }
                    }
                },
                onFinishActivity = onFinishActivity
            )
        }

        scrollable(Screen.Exercise.route) {
            ExerciseRoute(
                columnState = it.columnState,
                onSummary = {
                    navController.navigateToTopLevel(Screen.Summary, Screen.Summary.buildRoute(it))
                }
            )
        }

        composable(Screen.ExerciseNotAvailable.route) {
            ExerciseNotAvailable()
        }

        scrollable(
            Screen.Summary.route + "/{averageHeartRate}/{totalDistance}/{totalCalories}/{elapsedTime}",
            arguments = listOf(
                navArgument(Screen.Summary.averageHeartRateArg) { type = NavType.FloatType },
                navArgument(Screen.Summary.totalDistanceArg) { type = NavType.FloatType },
                navArgument(Screen.Summary.totalCaloriesArg) { type = NavType.FloatType },
                navArgument(Screen.Summary.elapsedTimeArg) { type = NavType.StringType }
            )
        ) {
            SummaryRoute(
                columnState = it.columnState,
                onRestartClick = {
                    navController.navigateToTopLevel(Screen.PreparingExercise)
                }
            )
        }
    }
}




