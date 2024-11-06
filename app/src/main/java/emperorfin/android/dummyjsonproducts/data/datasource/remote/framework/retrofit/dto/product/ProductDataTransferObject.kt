package emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.dto.product

import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product.ProductDataTransferObjectParams

data class ProductDataTransferObject private constructor(
    override val id: Long,
    override val title: String?,
    override val description: String?,
    override val brand: String?,
    override val price: Double?,
    override val thumbnail: String?,
    override val image: String?
) : ProductDataTransferObjectParams {

    companion object {

        fun newInstance(
            id: Long,
            title: String?,
            description: String?,
            brand: String?,
            price: Double?,
            thumbnail: String?,
            image: String?
        ): ProductDataTransferObject {
            return ProductDataTransferObject(
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
