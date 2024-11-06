package emperorfin.android.dummyjsonproducts.ui.screen.products.stateholder

import emperorfin.android.dummyjsonproducts.ui.model.product.ProductUiModel

data class ProductsUiState(
    val products: List<ProductUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: Int? = null,
    val messageSnackBar: Int? = null,
)
