package emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entitysource

import emperorfin.android.dummyjsonproducts.R
import emperorfin.android.dummyjsonproducts.data.constant.StringConstants.ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED
import emperorfin.android.dummyjsonproducts.data.constant.StringConstants.ERROR_MESSAGE_NOT_YET_IMPLEMENTED
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntity
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntityMapper
import emperorfin.android.dummyjsonproducts.di.IoDispatcher
import emperorfin.android.dummyjsonproducts.di.LocalProductsDao
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

data class ProductsLocalDataSourceRoom @Inject internal constructor(
    @LocalProductsDao private val productDao: IProductsDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val productEntityMapper: ProductEntityMapper,
    private val productModelMapper: ProductModelMapper
) : ProductsDataSource {

    private companion object {
        const val NUM_OF_PRODUCTS_0: Int = 0
        const val NUM_OF_ROWS_DELETED_1: Int = 1

        const val TABLE_ROW_ID_1: Long = 1L
    }

    override suspend fun countAllProducts(params: Params): ResultData<Int> = withContext(ioDispatcher) {
        when(params){
            is None -> {
                return@withContext try {

                    val numOfProducts: Int = productDao.countAllProducts()

                    if (numOfProducts > NUM_OF_PRODUCTS_0) {
                        return@withContext ResultData.Success(data = numOfProducts)
                    } else if (numOfProducts == NUM_OF_PRODUCTS_0) {
                        return@withContext ResultData.Error(failure = ProductFailure.NonExistentProductDataLocalError())
                    }

                    return@withContext ResultData.Error(failure = ProductFailure.ProductLocalError())

                } catch (e: Exception){
                    return@withContext ResultData.Error(failure = ProductFailure.ProductLocalError(cause = e))
                }
            }
            is ProductParams -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }


    }

    override suspend fun getProducts(params: Params): ResultData<List<ProductModel>> = withContext(ioDispatcher) {

        when(params){
            is None -> {
                return@withContext try {
                    val entityProducts: List<ProductEntity> = productDao.getProducts() as List<ProductEntity>

                    if (entityProducts == null) // Deliberate check but shouldn't do this
                        return@withContext ResultData.Error(failure = ProductFailure.ProductLocalError())
                    else if (entityProducts.isEmpty())
                        return@withContext ResultData.Error(failure = ProductFailure.ProductListNotAvailableLocalError())

                    val modelProducts = entityProducts.map {
                        productModelMapper.transform(it)
                    }

                    return@withContext ResultData.Success(modelProducts)

                } catch (e: Exception){
                    return@withContext ResultData.Error(failure = ProductFailure.ProductLocalError(cause = e))
                }
            }
            is ProductParams -> {
                return@withContext try {

                    var searchQuery = ""

                    if (params.title != null) {
                        searchQuery = params.title
                    } else if (params.description != null) {
                        searchQuery = params.description
                    }

//                    val entityProducts: List<ProductEntity> = productDao.getProducts(params.title!!) as List<ProductEntity>
                    val entityProducts: List<ProductEntity> = if (searchQuery.isNotEmpty()) {
                        productDao.getProducts(searchQuery)
                    } else {
                        productDao.getProducts()
                    }

                    if (entityProducts.isEmpty())
                        return@withContext ResultData.Error(failure = ProductFailure.ProductListNotAvailableLocalError())

                    val modelProducts = entityProducts.map {
                        productModelMapper.transform(it)
                    }

                    return@withContext ResultData.Success(modelProducts)

                } catch (e: Exception){
                    return@withContext ResultData.Error(failure = ProductFailure.ProductLocalError(cause = e))
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

                    val entityProduct: ProductEntity = productDao.getProduct(params.id.toString()) as ProductEntity

//                    if (entityProduct == null)
//                        return@withContext ResultData.Error(failure = ProductFailure.ProductLocalError())

                    val modelProduct = productModelMapper.transform(entityProduct)

                    return@withContext ResultData.Success(modelProduct)

                } catch (e: Exception){
                    return@withContext ResultData.Error(failure = ProductFailure.ProductLocalError(cause = e))
                }
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }
    }

    override suspend fun saveProduct(product: ProductModel): ResultData<Long> = withContext(ioDispatcher){

        val entityProduct = productEntityMapper.transform(product)

        // TODO: Put this in a try/catch block and then write test cases for it.
        val tableRowId: Long = productDao.insertProduct(entityProduct)

        if (tableRowId < TABLE_ROW_ID_1)
            return@withContext ResultData.Error(
                ProductFailure.InsertProductLocalError(message = R.string.error_local_insert_product)
            )

        return@withContext ResultData.Success(tableRowId)
    }

    override suspend fun deleteProduct(params: Params): ResultData<Int> = withContext(ioDispatcher) {
        when(params){
            is None -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }

            is ProductParams -> {
                return@withContext try {

                    val numOfProductDeleted: Int = productDao.deleteProduct(params.id.toString())

                    if (numOfProductDeleted < NUM_OF_ROWS_DELETED_1) {
                        return@withContext ResultData.Error(failure = ProductFailure.DeleteProductLocalError(R.string.error_local_delete_product))
                    }

                    return@withContext ResultData.Success(numOfProductDeleted)

                } catch (e: Exception){
                    return@withContext ResultData.Error(failure = ProductFailure.DeleteProductLocalError(cause = e))
                }
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }
    }

}
