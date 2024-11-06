package emperorfin.android.dummyjsonproducts.ui.model.product

import emperorfin.android.dummyjsonproducts.domain.model.product.ProductModel
import javax.inject.Inject

class ProductUiModelMapper @Inject constructor()  {

    fun transform(product: ProductModel): ProductUiModel {

        val id: Long = product.id
        val title: String = product.title
        val description: String = product.description
        val brand: String = product.brand
        val price: Double = product.price
        val thumbnail: String = product.thumbnail
        val image: String = product.image

        val priceWithCurrency = "$${product.price}"

        return ProductUiModel.newInstance(
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