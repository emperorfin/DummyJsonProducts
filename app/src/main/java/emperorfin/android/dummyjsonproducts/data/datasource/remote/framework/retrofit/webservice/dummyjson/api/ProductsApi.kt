package emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.api

import emperorfin.android.dummyjsonproducts.data.constant.StringConstants.ERROR_MESSAGE_NOT_YET_IMPLEMENTED
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntity
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.endpoint.products.ProductDetailsResponse
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.endpoint.products.ProductsSearchResponse
import emperorfin.android.dummyjsonproducts.domain.datalayer.dao.IProductsDao
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApi : IProductsDao {

    companion object {
        const val PAGE_LIMIT: Int = IProductsDao.REMOTE_PAGE_LIMIT
    }

    @GET("/products?limit=$PAGE_LIMIT&")
    override suspend fun getRemoteProducts(@Query("skip") skip: String): Response<ProductsSearchResponse>

    @GET("/products/{id}")
    override suspend fun getRemoteProduct(@Path("id") id: String): Response<ProductDetailsResponse>

    override suspend fun countAllProducts(): Int = throw IllegalStateException(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)

    override suspend fun getProduct(id: String): ProductEntity = throw IllegalStateException(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)

    override suspend fun getProducts(name: String): List<ProductEntity> = throw IllegalStateException(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)

    override suspend fun getProducts(): List<ProductEntity> = throw IllegalStateException(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)

    override suspend fun insertProduct(product: ProductEntity): Long = throw IllegalStateException(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)

    override suspend fun deleteProduct(id: String): Int = throw IllegalStateException(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
    
}