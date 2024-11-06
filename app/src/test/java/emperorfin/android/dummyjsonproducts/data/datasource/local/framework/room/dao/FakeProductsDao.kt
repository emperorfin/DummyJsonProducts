package emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.dao

import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntity
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.endpoint.products.ProductDetailsResponse
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.endpoint.products.ProductsSearchResponse
import emperorfin.android.dummyjsonproducts.domain.datalayer.dao.IFakeProductsDao
import retrofit2.Response


internal data class FakeProductsDao(
    private val isCountAllProductsException: Boolean = false,
    private val numberOfProductsRetrieved: Int = NUMBER_OF_PRODUCTS_RETRIEVED_2,
    private val isGetProductsException: Boolean = false,
    private val isGetProductsEmptyList: Boolean = false,
    private val products: List<ProductEntity> = PRODUCTS,
    private val isGetProductException: Boolean = false,
    private val product: ProductEntity = PRODUCT_1,
    private val insertedProductTableRowId: Long = TABLE_ROW_ID,
    private val isDeleteProductException: Boolean = false,
    private val numberOfProductsDeleted: Int = NUMBER_OF_PRODUCTS_DELETED_1,
) : IFakeProductsDao {

    companion object {

        const val NUMBER_OF_PRODUCTS_RETRIEVED_2: Int = 2
        const val NUMBER_OF_PRODUCTS_DELETED_1: Int = 1

        val PRODUCT_1: ProductEntity = ProductEntity.newInstance(
            id = 1,
            title = "Eyeshadow Palette with Mirror",
            description = "The Eyeshadow Palette with Mirror offers a versatile range of eyeshadow " +
                    "shades for creating stunning eye looks. With a built-in mirror, it's convenient " +
                    "for on-the-go makeup application.",
            brand = "Glamour Beauty",
            price = 19.99,
            thumbnail = "https://cdn.dummyjson.com/products/images/beauty/Eyeshadow%20Palette%20with%20Mirror/thumbnail.png",
            image = "https://cdn.dummyjson.com/products/images/beauty/Eyeshadow%20Palette%20with%20Mirror/1.png",
        )

        val PRODUCT_2: ProductEntity = ProductEntity.newInstance(
            id = 2,
            title = "Calvin Klein CK One",
            description = "CK One by Calvin Klein is a classic unisex fragrance, known for its fresh and clean scent. It's a versatile fragrance suitable for everyday wear.",
            brand = "Calvin Klein",
            price = 49.99,
            thumbnail = "https://cdn.dummyjson.com/products/images/fragrances/Calvin%20Klein%20CK%20One/thumbnail.png",
            image = "https://cdn.dummyjson.com/products/images/fragrances/Calvin%20Klein%20CK%20One/1.png",
        )

        val PRODUCTS: List<ProductEntity> = listOf(PRODUCT_1, PRODUCT_2)

        const val TABLE_ROW_ID: Long = 1L

    }

    override suspend fun countAllProducts(): Int {
        if (isCountAllProductsException) throw Exception()

        return numberOfProductsRetrieved
    }

    override suspend fun getProducts(): List<ProductEntity> {
        if (isGetProductsException) throw Exception()

        if (isGetProductsEmptyList) return emptyList()

        return products
    }

    override suspend fun getProducts(name: String): List<ProductEntity> {
        if (isGetProductsException) throw Exception()

        if (isGetProductsEmptyList) return emptyList()

        return products
    }

    override suspend fun getRemoteProducts(skip: String): Response<ProductsSearchResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getProduct(id: String): ProductEntity {
        if (isGetProductException) throw Exception()

        return product
    }

    override suspend fun getRemoteProduct(id: String): Response<ProductDetailsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertProduct(product: ProductEntity): Long = insertedProductTableRowId

    override suspend fun deleteProduct(id: String): Int {
        if (isDeleteProductException) throw Exception()

        return numberOfProductsDeleted
    }
}
