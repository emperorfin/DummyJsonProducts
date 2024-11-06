package emperorfin.android.dummyjsonproducts.ui.screen.productdetails.stateholder

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import emperorfin.android.dummyjsonproducts.R
import emperorfin.android.dummyjsonproducts.di.DefaultDispatcher
import emperorfin.android.dummyjsonproducts.di.IoDispatcher
import emperorfin.android.dummyjsonproducts.domain.datalayer.repository.IProductsRepository
import emperorfin.android.dummyjsonproducts.domain.exception.ProductFailure
import emperorfin.android.dummyjsonproducts.domain.model.product.ProductModel
import emperorfin.android.dummyjsonproducts.domain.model.product.ProductModelMapper
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.input.product.None
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.input.product.ProductParams
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.ResultData
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product.Params
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.succeeded
import emperorfin.android.dummyjsonproducts.ui.model.product.ProductUiModel
import emperorfin.android.dummyjsonproducts.ui.model.product.ProductUiModelMapper
import emperorfin.android.dummyjsonproducts.ui.util.InternetConnectivityUtil.hasInternetConnection
import emperorfin.android.dummyjsonproducts.ui.util.WhileUiSubscribed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.properties.Delegates


@HiltViewModel
data class ProductDetailsViewModel @Inject constructor(
    val application: Application,
    private val productsRepository: IProductsRepository,
    private val productModelMapper: ProductModelMapper,
    private val productUiModelMapper: ProductUiModelMapper,
    @IoDispatcher private val coroutineDispatcherIo: CoroutineDispatcher,
    @DefaultDispatcher private val coroutineDispatcherDefault: CoroutineDispatcher,
) : ViewModel() {

    companion object {
        private const val NUM_OF_MOVIES_MINUS_1: Int = -1
        private const val NUM_OF_MOVIES_0: Int = 0
    }

    private val _isLoading = MutableStateFlow(false)
    private val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val errorMessage: StateFlow<Int?> = _errorMessage

    private val _messageSnackBar: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val messageSnackBar: StateFlow<Int?> = _messageSnackBar

    private val _product: MutableStateFlow<ResultData<ProductUiModel>> =  MutableStateFlow(ResultData.Loading)
    val product: StateFlow<ResultData<ProductUiModel>> = _product

    val uiState: StateFlow<ProductDetailsUiState> = combine(
        isLoading, errorMessage, product, messageSnackBar
    ) { isLoading, errorMessage, product, messageSnackBar ->

        when (product) {

            ResultData.Loading -> {
                ProductDetailsUiState(isLoading = true)
            }
            is ResultData.Error -> {
                ProductDetailsUiState(
                    errorMessage = (product.failure as ProductFailure).message,
                    messageSnackBar = messageSnackBar
                )
            }
            is ResultData.Success<ProductUiModel> -> {

                val _product: ProductUiModel = product.data

                ProductDetailsUiState(
                    product = _product,
                    isLoading = isLoading,
//                    errorMessage = errorMessage,
                    errorMessage = null,
                    messageSnackBar = messageSnackBar
                )
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = ProductDetailsUiState(isLoading = true)
        )

    fun snackBarMessageShown() {
        _messageSnackBar.value = null
    }

    private fun getProductViaRepository(
        params: Params,
        isRefresh: Boolean = false,
    ) = viewModelScope.launch(context = coroutineDispatcherIo) {

        _product.value = ResultData.Loading

        val productResultData: ResultData<ProductModel> =
            productsRepository.getProduct(params = params, forceUpdate = isRefresh)

        if (productResultData.succeeded) {
            val productEntity = (productResultData as ResultData.Success).data

            val productModel: ProductModel = productModelMapper.transform(productEntity)

            val productUiModel: ProductUiModel = productUiModelMapper.transform(productModel)

            _product.value = ResultData.Success(data = productUiModel)

        } else {
            val error: ResultData.Error = (productResultData as ResultData.Error)
            _product.value = error
        }

    }

    fun loadProduct(params: ProductParams, isRefresh: Boolean){
        viewModelScope.launch {

            var productsCount by Delegates.notNull<Int>()

            val productsCountDataResultEvent = productsRepository.countAllProducts(params = None())

            productsCount = if (productsCountDataResultEvent.succeeded)
                (productsCountDataResultEvent as ResultData.Success).data
            else
                NUM_OF_MOVIES_MINUS_1

            if (productsCount > NUM_OF_MOVIES_0 || isRefresh){

                if (hasInternetConnection(application)){

                    getProductViaRepository(
                        params = params,
                        isRefresh = true,
                    )
                } else {

                    withContext(Dispatchers.Main){
                        Toast.makeText(
                            application,
                            R.string.message_no_internet_searching_cached_products,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    getProductViaRepository(
                        params = params,
                        isRefresh = false
                    )
                }
            } else {

                if (hasInternetConnection(application)){
                    getProductViaRepository(
                        params = params,
                        isRefresh = true
                    )
                } else {

                    _messageSnackBar.value = R.string.message_no_internet_connectivity

                    _product.value = ResultData.Error(
                        failure = ProductFailure.ProductRemoteError(
                            message = R.string.message_no_internet_connectivity
                        )
                    )
                }
            }

        }
    }

}
