package emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.jsonobject.products.product

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Meta(
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("barcode")
    val barcode: String?,
    @SerializedName("qrCode")
    val qrCode: String?,
) : Serializable
