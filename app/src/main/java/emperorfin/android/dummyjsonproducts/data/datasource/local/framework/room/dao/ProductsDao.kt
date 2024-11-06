package emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import emperorfin.android.dummyjsonproducts.data.constant.StringConstants.ERROR_MESSAGE_NOT_YET_IMPLEMENTED
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntity
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntity.Companion.COLUMN_INFO_DESCRIPTION
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntity.Companion.COLUMN_INFO_TITLE
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntity.Companion.COLUMN_INFO_ID
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntity.Companion.TABLE_NAME
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.endpoint.products.ProductDetailsResponse
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.endpoint.products.ProductsSearchResponse
import emperorfin.android.dummyjsonproducts.domain.datalayer.dao.IProductsDao
import retrofit2.Response

@Dao
interface ProductsDao : IProductsDao {

    @Query("SELECT COUNT(*) FROM $TABLE_NAME")
    override suspend fun countAllProducts(): Int

    @Query("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_INFO_TITLE ASC")
    override suspend fun getProducts(): List<ProductEntity>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_INFO_TITLE LIKE '%' || :name || '%' OR $COLUMN_INFO_DESCRIPTION LIKE '%' || :name || '%' ORDER BY $COLUMN_INFO_TITLE ASC")
    override suspend fun getProducts(name: String): List<ProductEntity>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_INFO_ID = :id")
    override suspend fun getProduct(id: String): ProductEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertProduct(product: ProductEntity): Long

    @Query("DELETE FROM $TABLE_NAME WHERE $COLUMN_INFO_ID = :id")
    override suspend fun deleteProduct(id: String): Int

    override suspend fun getRemoteProduct(id: String): Response<ProductDetailsResponse> =
        throw IllegalStateException(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)

    override suspend fun getRemoteProducts(skip: String): Response<ProductsSearchResponse> =
        throw IllegalStateException(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
    
}