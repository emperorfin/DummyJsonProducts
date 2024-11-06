package emperorfin.android.dummyjsonproducts.ui.screen.productdetails.stateholder

import emperorfin.android.dummyjsonproducts.ui.model.product.ProductUiModel

data class ProductDetailsUiState(
    val product: ProductUiModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: Int? = null,
    val messageSnackBar: Int? = null,
)
