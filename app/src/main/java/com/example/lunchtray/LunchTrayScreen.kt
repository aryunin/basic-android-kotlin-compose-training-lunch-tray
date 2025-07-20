/*
 * Copyright (C) 2023 The Android Open Source Project
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
package com.example.lunchtray

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.ui.AccompanimentMenuScreen
import com.example.lunchtray.ui.AppBar
import com.example.lunchtray.ui.CheckoutScreen
import com.example.lunchtray.ui.EntreeMenuScreen
import com.example.lunchtray.ui.OrderViewModel
import com.example.lunchtray.ui.Screen
import com.example.lunchtray.ui.SideDishMenuScreen
import com.example.lunchtray.ui.StartOrderScreen

// TODO: Screen enum

// TODO: AppBar

@Composable
fun LunchTrayApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Screen.Start.name
    val currentScreen = Screen.valueOf(currentRoute)
    val canNavigateBack = navController.previousBackStackEntry != null

    // Create ViewModel
    val viewModel: OrderViewModel = viewModel()

    Scaffold(
        topBar = {
            AppBar(
                currentScreen = currentScreen,
                canNavigateBack = canNavigateBack,
                navigateUp = navController::navigateUp,
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = Screen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Start.name) {
                StartOrderScreen(
                    onStartOrderButtonClicked = {
                        navController.navigate(Screen.EntreeMenu.name)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = Screen.EntreeMenu.name) {
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = {
                        cancel(viewModel, navController)
                    },
                    onNextButtonClicked = {
                        navController.navigate(Screen.SideDishMenu.name)
                    },
                    onSelectionChanged = {
                        viewModel.updateEntree(it)
                    }
                )
            }
            composable(route = Screen.SideDishMenu.name) {
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = {
                        cancel(viewModel, navController)
                    },
                    onNextButtonClicked = {
                        navController.navigate(Screen.AccompanimentMenu.name)
                    },
                    onSelectionChanged = {
                        viewModel.updateSideDish(it)
                    }
                )
            }
            composable(route = Screen.AccompanimentMenu.name) {
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = {
                        cancel(viewModel, navController)
                    },
                    onNextButtonClicked = {
                        navController.navigate(Screen.Checkout.name)
                    },
                    onSelectionChanged = {
                        viewModel.updateAccompaniment(it)
                    }
                )
            }
            composable(route = Screen.Checkout.name) {
                CheckoutScreen(
                    orderUiState = uiState,
                    onNextButtonClicked = { },
                    onCancelButtonClicked = { cancel(viewModel, navController) }
                )
            }
        }
    }
}

private fun cancel(viewModel: OrderViewModel, navController: NavHostController) {
    viewModel.resetOrder()
    navController.popBackStack(Screen.Start.name, false)
}