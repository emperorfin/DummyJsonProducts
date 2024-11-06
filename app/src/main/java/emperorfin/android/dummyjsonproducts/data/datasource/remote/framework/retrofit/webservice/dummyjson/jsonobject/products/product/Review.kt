package emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.jsonobject.products.product

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Review(
    @SerializedName("rating")
    val rating: Int?,
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("reviewerName")
    val reviewerName: String?,
    @SerializedName("reviewerEmail")
    val reviewerEmail: String?
) : Serializable
