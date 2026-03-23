package com.amur.pocky.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.amur.pocky.ui.addcard.AddCardScreen
import com.amur.pocky.ui.home.HomeScreen
import com.amur.pocky.ui.scanner.ScannerScreen

object Routes {
    const val HOME = "home"
    const val ADD_CARD = "add_card"
    const val EDIT_CARD = "edit_card/{cardId}"
    const val SCANNER = "scanner"

    fun editCard(cardId: Long) = "edit_card/$cardId"
}

@Composable
fun PockyNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onEditCardClick = { cardId -> navController.navigate(Routes.editCard(cardId)) },
                onAddCardClick = { navController.navigate(Routes.ADD_CARD) },
                onScanClick = { navController.navigate(Routes.SCANNER) },
            )
        }

        composable(Routes.ADD_CARD) {
            AddCardScreen(
                onNavigateBack = { navController.popBackStack() },
                scannedData = it.savedStateHandle.get<String>("scannedData"),
                scannedFormat = it.savedStateHandle.get<String>("scannedFormat"),
            )
        }

        composable(
            route = Routes.EDIT_CARD,
            arguments = listOf(navArgument("cardId") { type = NavType.LongType }),
        ) {
            AddCardScreen(
                onNavigateBack = { navController.popBackStack() },
                cardId = it.arguments?.getLong("cardId"),
            )
        }

        composable(Routes.SCANNER) {
            ScannerScreen(
                onBarcodeScanned = { data, format ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.apply {
                            set("scannedData", data)
                            set("scannedFormat", format)
                        }
                    navController.popBackStack()
                    navController.navigate(Routes.ADD_CARD)
                },
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
