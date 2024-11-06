package emperorfin.android.dummyjsonproducts.domain.datalayer.repository

import emperorfin.android.dummyjsonproducts.domain.model.product.ProductModel
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.ResultData
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product.Params

interface IProductsRepository {

    suspend fun countAllProducts(params: Params, countRemotely: Boolean = false): ResultData<Int>

    suspend fun getProducts(params: Params, forceUpdate: Boolean = false): ResultData<List<ProductModel>>

    suspend fun getProduct(params: Params, forceUpdate: Boolean = false): ResultData<ProductModel>

    suspend fun saveProduct(product: ProductModel, saveRemotely: Boolean = false): ResultData<Long>

    suspend fun deleteProduct(params: Params, deleteRemotely: Boolean = false): ResultData<Int>
    
}