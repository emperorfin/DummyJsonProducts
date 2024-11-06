package emperorfin.android.dummyjsonproducts.ui.screen.products

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import emperorfin.android.dummyjsonproducts.R
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.input.product.ProductParams
import emperorfin.android.dummyjsonproducts.ui.component.AppBar
import emperorfin.android.dummyjsonproducts.ui.component.EmptyContent
import emperorfin.android.dummyjsonproducts.ui.component.ContentLoader
import emperorfin.android.dummyjsonproducts.ui.component.LoadingIndicator
import emperorfin.android.dummyjsonproducts.ui.component.ProductListItem
import emperorfin.android.dummyjsonproducts.ui.extention.paging
import emperorfin.android.dummyjsonproducts.ui.model.product.ProductUiModel
import emperorfin.android.dummyjsonproducts.ui.navigation.NavigationActions
import emperorfin.android.dummyjsonproducts.ui.screen.products.stateholder.ProductsUiState
import emperorfin.android.dummyjsonproducts.ui.screen.products.stateholder.ProductsViewModel
import emperorfin.android.dummyjsonproducts.ui.util.InternetConnectivityUtil.hasInternetConnection


@Composable
fun ProductsScreen(
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier,
    navigationActions: NavigationActions?,
    viewModel: ProductsViewModel = hiltViewModel(),
) {

    val snackBarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            AppBar(
                title = stringResource(R.string.app_name),
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->

        Content(
            context = context,
            modifier = Modifier.padding(paddingValues),
            navigationActions = navigationActions,
            viewModel = viewModel,
            uiState = uiState
        )

//         Check for SnackBar messages to display on the screen
        uiState.messageSnackBar?.let { message ->
            val snackBarText = stringResource(message)
            LaunchedEffect(snackBarHostState, viewModel, message, snackBarText) {
                snackBarHostState.showSnackbar(message = snackBarText)
                viewModel.snackBarMessageShown()
            }
        }

    }
}

@Composable
private fun Content(
    modifier: Modifier,
    navigationActions: NavigationActions?,
    context: Context,
    viewModel: ProductsViewModel,
    uiState: ProductsUiState
) {

    val lazyListState = rememberLazyGridState()

    val latestPaginatedProducts = uiState.products
    val totalPaginatedProductsRetrieved = mutableListOf<ProductUiModel>()
    val isLoading = uiState.isLoading
    val errorMessage = uiState.errorMessage

    totalPaginatedProductsRetrieved.addAll(latestPaginatedProducts)

    Column {
        Spacer(modifier = Modifier.height(58.dp))

        ContentLoader(
            loading = isLoading,
            empty = totalPaginatedProductsRetrieved.isEmpty() && !isLoading,
            emptyContent = {
                EmptyContent(
                    errorLabel = errorMessage ?: R.string.content_description_error_message,
                    onRetry = {

                        val skip: Int = viewModel.newProductsLength.value

                        viewModel.loadProducts(
                            params = ProductParams(
                                id = -1L,
                                otherArgs = mapOf("skip" to skip.toString())
                            ),
                            isRefresh = false
                        )
                    }
                )
            },
            loadingIndicator = {
                LoadingIndicator(modifier = modifier)
            }
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                state = lazyListState,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background),
            ) {

                paging(
                    items = totalPaginatedProductsRetrieved,
                    newProductsLength = viewModel.newProductsLength,
                    fetch = {
                        viewModel.fetchNextProductPage()
                    }
                ) { _, item ->

                    ProductListItem(
                        product = item,
                        onClick = {

                            if (!hasInternetConnection(context.applicationContext as Application)){
                                Toast.makeText(context, R.string.message_no_internet_connectivity, Toast.LENGTH_SHORT).show()

                                return@ProductListItem
                            }

                            navigationActions?.navigateToProductDetailsScreen(it)

                        }
                    )
                }

            }

        }

    }
}