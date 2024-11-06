package emperorfin.android.dummyjsonproducts.ui.screen.products.stateholder

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import emperorfin.android.dummyjsonproducts.R
import emperorfin.android.dummyjsonproducts.di.DefaultDispatcher
import emperorfin.android.dummyjsonproducts.di.IoDispatcher
import emperorfin.android.dummyjsonproducts.domain.datalayer.dao.IProductsDao
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
data class ProductsViewModel @Inject constructor(
    val application: Application,
    private val productRepository: IProductsRepository,
    private val productModelMapper: ProductModelMapper,
    private val productUiModelMapper: ProductUiModelMapper,
    @IoDispatcher private val coroutineDispatcherIo: CoroutineDispatcher,
    @DefaultDispatcher private val coroutineDispatcherDefault: CoroutineDispatcher,
) : ViewModel() {

    companion object {
        private const val NUM_OF_PRODUCTS_MINUS_1: Int = -1
        private const val NUM_OF_PRODUCTS_0: Int = 0

        private const val QUERY_PARAM_SKIP: String = "skip"
    }

    private val _isLoading = MutableStateFlow(false)
    private val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val errorMessage: StateFlow<Int?> = _errorMessage

    private val _messageSnackBar: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val messageSnackBar: StateFlow<Int?> = _messageSnackBar

    private val _products: MutableStateFlow<ResultData<List<ProductUiModel>>> = MutableStateFlow(ResultData.Loading)
    val products: StateFlow<ResultData<List<ProductUiModel>>> = _products

    private val _productsOld: MutableStateFlow<ResultData<List<ProductUiModel>>> = MutableStateFlow(ResultData.Success(emptyList()))

    private val _newProductsLength: MutableStateFlow<Int> = MutableStateFlow(IProductsDao.REMOTE_NUM_OF_PRODUCTS_TO_SKIP)
    val newProductsLength: StateFlow<Int> = _newProductsLength

    private val _totalRetrievedProductsLength: MutableStateFlow<Int> = MutableStateFlow(0)
    private val totalRetrievedProductsLength: StateFlow<Int> = _totalRetrievedProductsLength

    init {

        loadProducts(
            params = ProductParams(
                id = -1L,
                otherArgs = mapOf(QUERY_PARAM_SKIP to "0")
            ),
            isRefresh = false
        )
    }

    val uiState: StateFlow<ProductsUiState> = combine(
        isLoading, errorMessage, products, messageSnackBar
    ) { isLoading, errorMessage, products, messageSnackBar ->

        when (products) {

            ResultData.Loading -> {
                ProductsUiState(isLoading = true)
            }
            is ResultData.Error -> {
                ProductsUiState(
                    errorMessage = (products.failure as ProductFailure).message,
                    messageSnackBar = messageSnackBar
                )
            }
            is ResultData.Success<List<ProductUiModel>> -> {

                val _products: List<ProductUiModel> = products.data

                val productsSorted = _products.sortedBy {
                    it.title
                }

                ProductsUiState(
                    products = productsSorted,
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
            initialValue = ProductsUiState(isLoading = true)
        )

    fun snackBarMessageShown() {
        _messageSnackBar.value = null
    }

    fun fetchNextProductPage() {

        val remainingRetrievableRemoteProducts: Int = IProductsDao.REMOTE_TOTAL_PRODUCTS - totalRetrievedProductsLength.value

        if (remainingRetrievableRemoteProducts < 1) {
            Toast.makeText(
                application,
                R.string.message_max_num_of_products_retrieved,
                Toast.LENGTH_LONG
            ).show()

            return
        }

        if (!isLoading.value) {

            val skip: Int = newProductsLength.value

            loadProducts(
                params = ProductParams(
                    id = -1L,
                    otherArgs = mapOf(QUERY_PARAM_SKIP to skip.toString())
                ),
                isRefresh = false
            )
        }
    }

    private fun getProductsViaRepository(
        params: Params,
        isRefresh: Boolean = false,
    ) = viewModelScope.launch(context = coroutineDispatcherIo) {

        val productsOld = (_productsOld.value as ResultData.Success).data
        val productsNew = mutableListOf<ProductUiModel>()

        _products.value = ResultData.Loading

        val productsResultData: ResultData<List<ProductModel>> =
            productRepository.getProducts(params = params, forceUpdate = isRefresh)

        if (productsResultData.succeeded) {
            val productsEntity = (productsResultData as ResultData.Success).data

            val productsUiModel: List<ProductUiModel> = productsEntity.map {
                productModelMapper.transform(it)
            }.map {
                productUiModelMapper.transform(it)
            }

            productsNew.addAll(productsOld)
            productsNew.addAll(productsUiModel)

            _newProductsLength.value = productsNew.size
            _totalRetrievedProductsLength.value += productsNew.size

//            _products.value = ResultData.Success(data = productsUiModel)
            _productsOld.value = ResultData.Success(data = productsNew)
            _products.value = ResultData.Success(data = productsNew)

        } else {
            val error: ResultData.Error = (productsResultData as ResultData.Error)
            _products.value = error

        }

    }

    private fun searchProducts(params: ProductParams, isRefresh: Boolean = true) {
        getProductsViaRepository(params = params, isRefresh = isRefresh)
    }

    private fun getAllProducts(params: None = None(), isRefresh: Boolean = false) {
        getProductsViaRepository(params = params, isRefresh = isRefresh)
    }

    /**
     * To get all products, pass None() as params.
     * To search for products, pass ProductParams(title) or ProductParams(description) as params.
     */
    fun loadProducts(params: Params, isRefresh: Boolean){
        viewModelScope.launch {

            var productsCount by Delegates.notNull<Int>()

            val productsCountDataResultEvent = productRepository.countAllProducts(params = None())

            productsCount = if (productsCountDataResultEvent.succeeded)
                (productsCountDataResultEvent as ResultData.Success).data
            else
                NUM_OF_PRODUCTS_MINUS_1

            if (productsCount > NUM_OF_PRODUCTS_0 || isRefresh){

                if (hasInternetConnection(application)){

                    searchProducts(
                        params = params as ProductParams,
                        isRefresh = true,
                    )
                } else {

                    withContext(Dispatchers.Main){
                        Toast.makeText(
                            application,
                            R.string.message_no_internet_loading_cached_products,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    getAllProducts(
                        params = None(),
                        isRefresh = false
                    )
                }
            } else {

                if (hasInternetConnection(application)){
                    searchProducts(
                        params = params as ProductParams,
                        isRefresh = true
                    )
                } else {

                    _messageSnackBar.value = R.string.message_no_internet_connectivity

                    _products.value = ResultData.Error(
                        failure = ProductFailure.ProductRemoteError(
                            message = R.string.message_no_internet_connectivity
                        )
                    )
                }
            }

        }
    }

}
