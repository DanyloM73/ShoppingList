package com.danylom73.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.danylom73.shoppinglist.ui.theme.ShoppingListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Navigation(modifier: Modifier) {
    val navController = rememberNavController()
    val viewModel: LocationViewModel = viewModel()
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)

    NavHost(
        navController,
        startDestination = "shopping_list_screen"
    ) {
        composable("shopping_list_screen") {
            ShoppingListApp(
                locationUtils = locationUtils,
                viewModel = viewModel,
                navController = navController,
                context = context,
                address = viewModel.address.value
                    .firstOrNull()?.formatted_address ?: "No address",
                modifier = modifier
            )
        }

        dialog("location_screen") { backStack ->
            viewModel.location.value?.let { it1 ->
                LocationSelectionScreen(
                    location = it1,
                    onLocationSelected = {
                        viewModel.fetchAddress("${it.latitude},${it.longitude}")
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
