package emperorfin.android.dummyjsonproducts.domain.datalayer.datasource

import emperorfin.android.dummyjsonproducts.domain.model.product.ProductModel
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.ResultData
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product.Params

interface ProductsDataSource {

    suspend fun countAllProducts(params: Params): ResultData<Int>

    suspend fun getProducts(params: Params): ResultData<List<ProductModel>>

    suspend fun getProduct(params: Params): ResultData<ProductModel>

    suspend fun saveProduct(product: ProductModel): ResultData<Long>

    suspend fun deleteProduct(params: Params): ResultData<Int>
    
}