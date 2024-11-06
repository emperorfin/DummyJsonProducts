package emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.dtosource

import emperorfin.android.dummyjsonproducts.data.constant.StringConstants.ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED
import emperorfin.android.dummyjsonproducts.data.constant.StringConstants.ERROR_MESSAGE_NOT_YET_IMPLEMENTED
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.dto.product.ProductDataTransferObject
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.dto.product.ProductDataTransferObjectMapper
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.endpoint.products.ProductDetailsResponse
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.endpoint.products.ProductsSearchResponse
import emperorfin.android.dummyjsonproducts.di.IoDispatcher
import emperorfin.android.dummyjsonproducts.di.MainDispatcher
import emperorfin.android.dummyjsonproducts.di.RemoteProductsDao
import emperorfin.android.dummyjsonproducts.domain.datalayer.dao.IProductsDao
import emperorfin.android.dummyjsonproducts.domain.datalayer.datasource.ProductsDataSource
import emperorfin.android.dummyjsonproducts.domain.exception.ProductFailure
import emperorfin.android.dummyjsonproducts.domain.model.product.ProductModel
import emperorfin.android.dummyjsonproducts.domain.model.product.ProductModelMapper
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.input.product.None
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.input.product.ProductParams
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.ResultData
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product.Params
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class ProductsRemoteDataSourceRetrofit @Inject internal constructor(
    @RemoteProductsDao private val productDao: IProductsDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val productDtoMapper: ProductDataTransferObjectMapper,
    private val productModelMapper: ProductModelMapper
) : ProductsDataSource {
    override suspend fun countAllProducts(params: Params): ResultData<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getProducts(params: Params): ResultData<List<ProductModel>> = withContext(ioDispatcher) {
        when(params){
            is None -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            is ProductParams -> {

                return@withContext try {

                    val skip: String = params.otherArgs!!.getValue("skip")

                    val response = productDao.getRemoteProducts(
                        skip = skip,
                    )

                    withContext(mainDispatcher){
                        if (response.isSuccessful){

                            response.body()?.let {

                                val productsModel: List<ProductModel> =
                                    buildProductModelList(response = it)

                                return@withContext ResultData.Success(productsModel)
                            }
                        }

                        return@withContext ResultData.Error(failure = ProductFailure.GetProductRemoteError())
                    }

                } catch (e: Exception){
                    return@withContext ResultData.Error(failure = ProductFailure.ProductRemoteError(cause = e))
                }
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }
    }

    override suspend fun getProduct(params: Params): ResultData<ProductModel> = withContext(ioDispatcher) {
        when(params){
            is None -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            is ProductParams -> {

                return@withContext try {

                    val response = productDao.getRemoteProduct(
                        id = params.id.toString(),
                    )

                    withContext(mainDispatcher){
                        if (response.isSuccessful){

                            response.body()?.let {

                                val movieModel: ProductModel = buildProductModel(product = it)

                                return@withContext ResultData.Success(movieModel)
                            }
                        }

                        return@withContext ResultData.Error(failure = ProductFailure.GetProductRemoteError())
                    }

                } catch (e: Exception){
                    return@withContext ResultData.Error(failure = ProductFailure.ProductRemoteError(cause = e))
                }
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }
    }

    override suspend fun saveProduct(product: ProductModel): ResultData<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(params: Params): ResultData<Int> {
        TODO("Not yet implemented")
    }

    private fun buildProductModelList(response: ProductsSearchResponse): List<ProductModel> {

        val responseProduct = response.products

        val productsDto = mutableListOf<ProductDataTransferObject>()

        responseProduct.forEach { product ->

            var image: String? = null

            product.images?.let {
                image = it.first()
            }

            val productDto = ProductDataTransferObject.newInstance(
                id = product.id,
                title = product.title,
                description = product.description,
                brand = product.brand,
                price = product.price,
                image = image,
                thumbnail = product.thumbnail
            )

            productsDto.add(productDto)

        }

        return productsDto.map {
            productModelMapper.transform(it)
        }

    }

    private fun buildProductModel(product: ProductDetailsResponse): ProductModel {

        var image: String? = null

        product.images?.let {
            image = it.first()
        }

        val productDto = ProductDataTransferObject.newInstance(
            id = product.id,
            title = product.title,
            description = product.description,
            brand = product.brand,
            price = product.price,
            image = image,
            thumbnail = product.thumbnail
        )

        return productModelMapper.transform(productDto)

    }
}
