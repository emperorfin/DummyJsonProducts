package emperorfin.android.dummyjsonproducts.ui.navigation

import androidx.navigation.NavHostController
import emperorfin.android.dummyjsonproducts.ui.navigation.Destinations.ROUTE_PRODUCTS
import emperorfin.android.dummyjsonproducts.ui.navigation.Destinations.ROUTE_PRODUCT_DETAILS
import emperorfin.android.dummyjsonproducts.ui.navigation.Screens.SCREEN_PRODUCTS
import emperorfin.android.dummyjsonproducts.ui.navigation.Screens.SCREEN_PRODUCT_DETAILS


/**
 * Screens used in [Destinations]
 */
private object Screens {
    const val SCREEN_PRODUCTS: String = "products"
    const val SCREEN_PRODUCT_DETAILS: String = "productdetails"
}

/**
 * Destinations used in the [MainActivity]
 */
object Destinations {
    const val ROUTE_PRODUCTS: String = SCREEN_PRODUCTS
    const val ROUTE_PRODUCT_DETAILS: String = SCREEN_PRODUCT_DETAILS
}

object ScreenArgs {
    const val SCREEN_PRODUCT_DETAILS_PRODUCT_ID: String = "productid"
}

/**
 * Models the navigation actions in the app.
 */
class NavigationActions(private val navController: NavHostController) {

    fun navigateToProductsScreen() {
        navController.navigate(ROUTE_PRODUCTS)
    }

    /**
     * args: "{id}"
     */
    fun navigateToProductDetailsScreen(args: String) {

        val routeWithArgs = "${ROUTE_PRODUCT_DETAILS}/$args"

        navController.navigate(routeWithArgs)
    }

    fun navigateBack() {
        navController.navigateUp()
    }

}