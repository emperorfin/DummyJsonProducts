package emperorfin.android.dummyjsonproducts.domain.model.product

import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product.ProductModelParams

data class ProductModel private constructor(
    override val id: Long,
    override val title: String,
    override val description: String,
    override val brand: String,
    override val price: Double,
    override val thumbnail: String,
    override val image: String
) : ProductModelParams {

    companion object {

        fun newInstance(
            id: Long,
            title: String,
            description: String,
            brand: String,
            price: Double,
            thumbnail: String,
            image: String
        ): ProductModel {
            return ProductModel(
                id = id,
                title = title,
                description = description,
                brand = brand,
                price = price,
                thumbnail = thumbnail,
                image = image
            )
        }

    }

}
