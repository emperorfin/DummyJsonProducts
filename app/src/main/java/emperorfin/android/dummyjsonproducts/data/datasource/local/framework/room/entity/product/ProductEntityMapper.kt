package emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product

import emperorfin.android.dummyjsonproducts.domain.model.product.ProductModel
import javax.inject.Inject

class ProductEntityMapper @Inject constructor() {

    fun transform(product: ProductModel): ProductEntity {

        val id: Long = product.id
        val title: String = product.title
        val description: String = product.description
        val brand: String = product.brand
        val price: Double = product.price
        val thumbnail: String = product.thumbnail
        val image: String = product.image

        return ProductEntity.newInstance(
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