package emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.endpoint.products

import com.google.gson.annotations.SerializedName
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.jsonobject.products.product.Product
import java.io.Serializable

data class ProductsSearchResponse(
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("skip")
    val skip: Int,
    @SerializedName("limit")
    val limit: Int,
) : Serializable
