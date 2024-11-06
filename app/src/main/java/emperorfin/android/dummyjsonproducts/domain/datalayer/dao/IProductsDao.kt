package emperorfin.android.dummyjsonproducts.domain.datalayer.dao

import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntity
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.endpoint.products.ProductDetailsResponse
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.endpoint.products.ProductsSearchResponse
import retrofit2.Response

interface IProductsDao {

    companion object {
        const val REMOTE_PAGE_LIMIT: Int = 15
        const val REMOTE_NUM_OF_PRODUCTS_TO_SKIP = REMOTE_PAGE_LIMIT
        const val REMOTE_TOTAL_PRODUCTS = 194
    }

    suspend fun countAllProducts(): Int

    suspend fun getProducts(): List<ProductEntity>

    suspend fun getProducts(name: String): List<ProductEntity>

    suspend fun getRemoteProducts(skip: String): Response<ProductsSearchResponse>

    suspend fun getProduct(id: String): ProductEntity

    suspend fun getRemoteProduct(id: String): Response<ProductDetailsResponse>

    suspend fun insertProduct(product: ProductEntity): Long

    suspend fun deleteProduct(id: String): Int
    
}