package emperorfin.android.dummyjsonproducts.ui.model.product

import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product.ProductUiModelParams

data class ProductUiModel private constructor(
    override val id: Long,
    override val title: String,
    override val description: String,
    override val brand: String,
    override val price: Double,
    override val thumbnail: String,
    override val image: String,
    override val priceWithCurrency: String
) : ProductUiModelParams {

    companion object {

        fun newInstance(
            id: Long,
            title: String,
            description: String,
            brand: String,
            price: Double,
            thumbnail: String,
            image: String,
            priceWithCurrency: String,
        ): ProductUiModel {
            return ProductUiModel(
                id = id,
                title = title,
                description = description,
                brand = brand,
                price = price,
                thumbnail = thumbnail,
                image = image,
                priceWithCurrency = priceWithCurrency
            )
        }

    }

}
