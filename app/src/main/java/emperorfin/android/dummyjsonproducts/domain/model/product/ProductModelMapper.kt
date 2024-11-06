package emperorfin.android.dummyjsonproducts.domain.model.product

import emperorfin.android.dummyjsonproducts.domain.constant.DoubleConstants.MINUS_0_0
import emperorfin.android.dummyjsonproducts.domain.constant.StringConstants.EMPTY
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product.ProductDataTransferObjectParams
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product.ProductEntityParams
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product.ProductUiModelParams
import javax.inject.Inject

class ProductModelMapper @Inject constructor()  {

    fun transform(product: ProductDataTransferObjectParams): ProductModel {

        val id: Long = product.id
        val title: String = product.title ?: EMPTY
        val description: String = product.description ?: EMPTY
        val brand: String = product.brand ?: EMPTY
        val price: Double = product.price ?: MINUS_0_0
        val thumbnail: String = product.thumbnail ?: EMPTY
        val image: String = product.image ?: EMPTY

        return ProductModel.newInstance(
            id = id,
            title = title,
            description = description,
            brand = brand,
            price = price,
            thumbnail = thumbnail,
            image = image
        )
    }

    fun transform(product: ProductEntityParams): ProductModel {

        val id: Long = product.id
        val title: String = product.title!!
        val description: String = product.description!!
        val brand: String = product.brand!!
        val price: Double = product.price!!
        val thumbnail: String = product.thumbnail!!
        val image: String = product.image!!

        return ProductModel.newInstance(
            id = id,
            title = title,
            description = description,
            brand = brand,
            price = price,
            thumbnail = thumbnail,
            image = image
        )

    }

    fun transform(product: ProductUiModelParams): ProductModel {

        val id: Long = product.id
        val title: String = product.title!!
        val description: String = product.description!!
        val brand: String = product.brand!!
        val price: Double = product.price!!
        val thumbnail: String = product.thumbnail!!
        val image: String = product.image!!

        return ProductModel.newInstance(
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