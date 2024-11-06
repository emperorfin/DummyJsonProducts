package emperorfin.android.dummyjsonproducts.ui.screen.productdetails

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.palette.graphics.Palette
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.palette.BitmapPalette
import emperorfin.android.dummyjsonproducts.R
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.input.product.ProductParams
import emperorfin.android.dummyjsonproducts.ui.component.AppBar
import emperorfin.android.dummyjsonproducts.ui.component.ContentLoader
import emperorfin.android.dummyjsonproducts.ui.component.EmptyContent
import emperorfin.android.dummyjsonproducts.ui.component.LoadingIndicator
import emperorfin.android.dummyjsonproducts.ui.component.NetworkImage
import emperorfin.android.dummyjsonproducts.ui.model.product.ProductUiModel
import emperorfin.android.dummyjsonproducts.ui.navigation.NavigationActions
import emperorfin.android.dummyjsonproducts.ui.screen.productdetails.stateholder.ProductDetailsUiState
import emperorfin.android.dummyjsonproducts.ui.screen.productdetails.stateholder.ProductDetailsViewModel
import emperorfin.android.dummyjsonproducts.ui.theme.background
import emperorfin.android.dummyjsonproducts.ui.theme.shimmerHighLight


@Composable
fun ProductDetailsScreen(
    modifier: Modifier = Modifier,
    navigationActions: NavigationActions?,
    productId: String,
    viewModel: ProductDetailsViewModel = hiltViewModel(),
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsState()

    val product = uiState.product

    LaunchedEffect(key1 = productId) {
        viewModel.loadProduct(
            params = ProductParams(id = productId.toLong()),
            isRefresh = false
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {

            var productName = ""

            if (product != null) {
                productName = product.title.ifEmpty {
                    product.description
                }
            }

            AppBar(
                title = productName,
                onBackPress = {
                    navigationActions?.navigateBack()
                }
            )
        },
        modifier = modifier.fillMaxSize(),
        containerColor = background
    ) { paddingValues ->

        Content(
            modifier = Modifier.padding(paddingValues),
            productId = productId,
            product = product,
            viewModel = viewModel,
            uiState = uiState
        )

        // Check for SnackBar messages to display on the screen
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
    productId: String,
    product: ProductUiModel?,
    viewModel: ProductDetailsViewModel,
    uiState: ProductDetailsUiState
) {

    val isLoading = uiState.isLoading
    val errorMessage = uiState.errorMessage

    ContentLoader(
        loading = isLoading,
        empty = product == null && !isLoading,
        emptyContent = {
            EmptyContent(
                errorLabel = errorMessage ?: R.string.content_description_error_message,
                onRetry = {
                    viewModel.loadProduct(
                        params = ProductParams(
                            id = productId.toLong()
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


        product?.let {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .background(background)
                    .fillMaxSize(),
            ) {

                Spacer(modifier = Modifier.height(58.dp))

                Header(it)

                Description(it)

                Spacer(modifier = Modifier.height(24.dp))
            }

        }

    }
}

@Composable
private fun Header(
    product: ProductUiModel
) {

    Column {
        var palette by remember { mutableStateOf<Palette?>(null) }
        NetworkImage(
            networkUrl = product.image,
            circularReveal = CircularReveal(duration = 300),
            shimmerParams = null,
            bitmapPalette = BitmapPalette {
                palette = it
            },
            modifier = Modifier
                .height(380.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = product.title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = product.priceWithCurrency,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = product.brand,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

    }
}

@Composable
private fun Description(
    product: ProductUiModel
) {

    Column {

        Spacer(modifier = Modifier.height(23.dp))

        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))
    }
}