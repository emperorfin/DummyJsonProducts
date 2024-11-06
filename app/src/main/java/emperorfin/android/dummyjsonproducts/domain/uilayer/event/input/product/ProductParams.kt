package emperorfin.android.dummyjsonproducts.domain.uilayer.event.input.product

import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product.ProductModelParams

data class ProductParams(
    override val id: Long,
    override val title: String? = null,
    override val description: String? = null,
    override val brand: String? = null,
    override val price: Double? = null,
    override val thumbnail: String? = null,
    override val image: String? = null,
    val otherArgs: Map<String, String>? = null
) : ProductModelParams
