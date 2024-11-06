package emperorfin.android.dummyjsonproducts.data.repository

import emperorfin.android.dummyjsonproducts.R
import emperorfin.android.dummyjsonproducts.di.IoDispatcher
import emperorfin.android.dummyjsonproducts.di.ProductsLocalDataSource
import emperorfin.android.dummyjsonproducts.di.ProductsRemoteDataSource
import emperorfin.android.dummyjsonproducts.domain.datalayer.datasource.ProductsDataSource
import emperorfin.android.dummyjsonproducts.domain.datalayer.repository.IProductsRepository
import emperorfin.android.dummyjsonproducts.domain.exception.ProductFailure
import emperorfin.android.dummyjsonproducts.domain.model.product.ProductModel
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.input.product.ProductParams
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.ResultData
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product.Params
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.inject.Inject

data class ProductsRepository @Inject constructor(
    @ProductsLocalDataSource private val productLocalDataSource: ProductsDataSource,
    @ProductsRemoteDataSource private val productRemoteDataSource: ProductsDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : IProductsRepository {

    private var cachedProducts: ConcurrentMap<String, ProductModel>? = null

    override suspend fun countAllProducts(params: Params, countRemotely: Boolean): ResultData<Int> = withContext(ioDispatcher) {
        if (countRemotely) {
            return@withContext productRemoteDataSource.countAllProducts(params = params)
        } else {
            return@withContext productLocalDataSource.countAllProducts(params = params)
        }
    }

    override suspend fun getProducts(params: Params, forceUpdate: Boolean): ResultData<List<ProductModel>> {
        return withContext(ioDispatcher) {
            // Respond immediately with cache if available and not dirty
            if (!forceUpdate) {
                cachedProducts?.let { products ->
                    return@withContext ResultData.Success(products.values.sortedBy { it.title })
                }
            }

            val newProducts: ResultData<List<ProductModel>> =
                fetchProductsFromRemoteOrLocal(params = params, forceUpdate = forceUpdate)

            // Refresh the cache with the new products
            (newProducts as? ResultData.Success)?.let { refreshCache(it.data) }

            cachedProducts?.values?.let { products ->
                return@withContext ResultData.Success(products.sortedBy { it.title })
            }

            (newProducts as? ResultData.Success)?.let {
                if (it.data.isNotEmpty()) { // it.data.isEmpty()
                    return@withContext ResultData.Success(it.data)
                } else {
                    // TODO: Note new impl line addition
                    return@withContext ResultData.Error(
                        failure = ProductFailure.ProductListNotAvailableRepositoryError()
                    )
                }
            }

            // TODO: Check to be sure newProducts isn't a type other than ResultData.Error.
            //      If it is, return an explicit ResultData.Error objet.
            return@withContext newProducts as ResultData.Error
        }
    }

    override suspend fun getProduct(params: Params, forceUpdate: Boolean): ResultData<ProductModel> {
        return withContext(ioDispatcher) {
            // Respond immediately with cache if available and not dirty
            if (!forceUpdate) {

                if (params is ProductParams) {
                    if (cachedProducts?.containsKey(params.id.toString()) == true) {

                        val productCached = cachedProducts?.get(params.id.toString())

                        return@withContext ResultData.Success(productCached!!)
                    }
                }
            }

            val newProduct: ResultData<ProductModel> =
                fetchProductFromRemoteOrLocal(params = params, forceUpdate = forceUpdate)

            // Refresh the cache with the new product
            (newProduct as? ResultData.Success)?.let { refreshCache(it.data) }

            if (params is ProductParams) {
                if (cachedProducts?.containsKey(params.id.toString()) == true) {

                    val productCached = cachedProducts?.get(params.id.toString())

                    return@withContext ResultData.Success(productCached!!)
                }
            }

            (newProduct as? ResultData.Success)?.let {
                return@withContext ResultData.Success(it.data)
            }

            return@withContext newProduct as ResultData.Error
        }
    }

    override suspend fun saveProduct(product: ProductModel, saveRemotely: Boolean): ResultData<Long> = withContext(ioDispatcher) {

        if (saveRemotely) {
            return@withContext productRemoteDataSource.saveProduct(product = product)
        } else {
            return@withContext productLocalDataSource.saveProduct(product = product)
        }

    }

    override suspend fun deleteProduct(params: Params, deleteRemotely: Boolean): ResultData<Int> = withContext(ioDispatcher) {

        if (deleteRemotely) {
            return@withContext productRemoteDataSource.deleteProduct(params = params)
        } else {
            return@withContext productLocalDataSource.deleteProduct(params = params)
        }
    }

    private suspend fun fetchProductsFromRemoteOrLocal(params: Params, forceUpdate: Boolean): ResultData<List<ProductModel>> {
        var isRemoteException = false

        // Remote first
        if (forceUpdate) {
            when (val productsRemote = productRemoteDataSource.getProducts(params = params)) {
//             is Error -> return remoteProducts // Timber.w("Remote data source fetch failed")
                is ResultData.Error -> {
                    if (productsRemote.failure is ProductFailure.ProductRemoteError)
                        isRemoteException = true
                }
                is ResultData.Success -> {
                    refreshLocalDataSource(products = productsRemote.data)

                    return productsRemote
                }
//             else -> throw IllegalStateException()
                else -> {}
            }
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            if (isRemoteException)
                return ResultData.Error(
                    ProductFailure.GetProductRepositoryError(
                        message = R.string.exception_occurred_remote
                    )
                )

            return ResultData.Error(
                // TODO: Change GetProductRemoteError to GetProductRepositoryError and update
                //  test cases too.
                ProductFailure.GetProductRepositoryError(
                    message = R.string.error_cant_force_refresh_products_remote_data_source_unavailable
                )
            )
        }

        // Local if remote fails
        val productsLocal = productLocalDataSource.getProducts(params = params)

        if (productsLocal is ResultData.Success) return productsLocal

        if ((productsLocal as ResultData.Error).failure is ProductFailure.ProductLocalError)
            return ResultData.Error(
                ProductFailure.GetProductRepositoryError(
                    R.string.exception_occurred_local
                )
            )

//        return Error((productsLocal as Error).failure)
        return ResultData.Error(
            ProductFailure.GetProductRepositoryError(
                R.string.error_fetching_from_remote_and_local
            )
        )
    }

    private suspend fun fetchProductFromRemoteOrLocal(params: Params, forceUpdate: Boolean): ResultData<ProductModel> {
        var isRemoteException = false

        // Remote first
        if (forceUpdate) {
            when (val productRemote = productRemoteDataSource.getProduct(params = params)) {
//             is Error -> return remoteProduct // Timber.w("Remote data source fetch failed")
                is ResultData.Error -> {
                    if (productRemote.failure is ProductFailure.ProductRemoteError)
                        isRemoteException = true
                }
                is ResultData.Success -> {
                    refreshLocalDataSource(product = productRemote.data)

                    return productRemote
                }
//             else -> throw IllegalStateException()
                else -> {}
            }
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            if (isRemoteException)
                return ResultData.Error(
                    ProductFailure.GetProductRepositoryError(
                        message = R.string.exception_occurred_remote
                    )
                )

            return ResultData.Error(
                // TODO: Change GetProductRemoteError to GetProductRepositoryError and update
                //  test cases too.
                ProductFailure.GetProductRemoteError(
                    message = R.string.error_cant_force_refresh_products_remote_data_source_unavailable
                )
            )
        }

        // Local if remote fails
        val productLocal = productLocalDataSource.getProduct(params = params)

        if (productLocal is ResultData.Success) return productLocal

        if ((productLocal as ResultData.Error).failure is ProductFailure.ProductLocalError)
            return ResultData.Error(
                ProductFailure.GetProductRepositoryError(
                    R.string.exception_occurred_local
                )
            )

//        return Error((productLocal as Error).failure)
        return ResultData.Error(
            ProductFailure.GetProductRepositoryError(
                R.string.error_fetching_from_remote_and_local
            )
        )
    }

    private fun refreshCache(products: List<ProductModel>) {
        cachedProducts?.clear()

        products.sortedBy { it.title }.forEach {
            cacheAndPerform(it) {}
        }
    }

    private fun refreshCache(product: ProductModel) {
        cachedProducts?.remove(product.id.toString())

        cacheAndPerform(product) {}
    }

    private suspend fun refreshLocalDataSource(products: List<ProductModel>) {

        return // TODO: REMOVE THIS LINE TO REFRESH LOCAL DATA SOURCE

        products.forEach {
            val params = ProductParams(id = it.id)

            productLocalDataSource.deleteProduct(params = params)

            productLocalDataSource.saveProduct(product = it)
        }
    }

    private suspend fun refreshLocalDataSource(product: ProductModel) {

        val params = ProductParams(id = product.id)

        productLocalDataSource.deleteProduct(params = params)

        productLocalDataSource.saveProduct(product = product)
    }

    private fun cacheProduct(product: ProductModel): ProductModel {

        val cachedProduct = ProductModel.newInstance(
            id = product.id,
            title = product.title,
            description = product.description,
            brand = product.brand,
            price = product.price,
            image = product.image,
            thumbnail = product.thumbnail,
        )

        // Create if it doesn't exist.
        if (cachedProducts == null) {
            cachedProducts = ConcurrentHashMap()
        }

        cachedProducts?.put(cachedProduct.id.toString(), cachedProduct)

        return cachedProduct
    }

    private inline fun cacheAndPerform(product: ProductModel, perform: (ProductModel) -> Unit) {

        val cachedProduct = cacheProduct(product)

        perform(cachedProduct)
    }
}
