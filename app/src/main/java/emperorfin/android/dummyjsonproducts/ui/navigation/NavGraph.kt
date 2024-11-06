package emperorfin.android.dummyjsonproducts.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import emperorfin.android.dummyjsonproducts.ui.navigation.Destinations.ROUTE_PRODUCTS
import emperorfin.android.dummyjsonproducts.ui.navigation.Destinations.ROUTE_PRODUCT_DETAILS
import emperorfin.android.dummyjsonproducts.ui.navigation.ScreenArgs.SCREEN_PRODUCT_DETAILS_PRODUCT_ID
import emperorfin.android.dummyjsonproducts.ui.screen.productdetails.ProductDetailsScreen
import emperorfin.android.dummyjsonproducts.ui.screen.products.ProductsScreen


@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_PRODUCTS,
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    },
) {

    val productDetailsRouteWithArgs = "${ROUTE_PRODUCT_DETAILS}/{$SCREEN_PRODUCT_DETAILS_PRODUCT_ID}"

    ProvideWindowInsets {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination,
        ) {

            composable(ROUTE_PRODUCTS) {
                ProductsScreen(navigationActions = navActions)
            }

            composable(
                productDetailsRouteWithArgs,
                arguments = listOf(
                    navArgument(SCREEN_PRODUCT_DETAILS_PRODUCT_ID) { type = NavType.StringType },
                )
            ) { backStackEntry ->

                val productId: String = backStackEntry.arguments?.getString(SCREEN_PRODUCT_DETAILS_PRODUCT_ID)!!

                ProductDetailsScreen(
                    navigationActions = navActions,
                    productId = productId,
                )
            }
        }
    }

}