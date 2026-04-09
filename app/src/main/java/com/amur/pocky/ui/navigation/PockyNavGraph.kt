package com.amur.pocky.ui.navigation

import android.net.Uri
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
    const val ADD_CARD = "add_card?scannedData={scannedData}&scannedFormat={scannedFormat}"
    const val EDIT_CARD = "edit_card/{cardId}"
    const val SCANNER = "scanner"

    fun addCard() = "add_card"
    fun addCardWithScan(data: String, format: String) =
        "add_card?scannedData=${Uri.encode(data)}&scannedFormat=${Uri.encode(format)}"
    fun editCard(cardId: Long) = "edit_card/$cardId"
}

@Composable
fun PockyNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onEditCardClick = { cardId -> navController.navigate(Routes.editCard(cardId)) },
                onAddCardClick = { navController.navigate(Routes.addCard()) },
                onScanClick = { navController.navigate(Routes.SCANNER) },
            )
        }

        composable(
            route = Routes.ADD_CARD,
            arguments = listOf(
                navArgument("scannedData") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("scannedFormat") {
                    type = NavType.StringType
                    defaultValue = ""
                },
            ),
        ) {
            AddCardScreen(
                onNavigateBack = { navController.popBackStack() },
                scannedData = it.arguments?.getString("scannedData")?.takeIf { s -> s.isNotEmpty() },
                scannedFormat = it.arguments?.getString("scannedFormat")?.takeIf { s -> s.isNotEmpty() },
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
                    navController.popBackStack()
                    navController.navigate(Routes.addCardWithScan(data, format))
                },
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
