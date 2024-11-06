package emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.api

import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntity
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.endpoint.products.ProductDetailsResponse
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.endpoint.products.ProductsSearchResponse
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.jsonobject.products.product.Product
import emperorfin.android.dummyjsonproducts.domain.datalayer.dao.IFakeProductsDao
import okhttp3.ResponseBody
import retrofit2.Response


data class FakeProductsApi(
    private val isGetRemoteProductsException: Boolean = false,
    private val isGetRemoteProductsFailed: Boolean = false,
    private val isGetRemoteProductException: Boolean = false,
    private val isGetRemoteProductFailed: Boolean = false
) : IFakeProductsDao {

    companion object {

        const val ERROR_MESSAGE: String = "Error encountered."

        const val HTTP_STATUS_CODE_404: Int = 404

        val PRODUCT_1: Product = Product(
            id = 1,
            title = "Eyeshadow Palette with Mirror",
            description = "The Eyeshadow Palette with Mirror offers a versatile range of eyeshadow " +
                    "shades for creating stunning eye looks. With a built-in mirror, it's convenient " +
                    "for on-the-go makeup application.",
            brand = "Glamour Beauty",
            price = 19.99,
            thumbnail = "https://cdn.dummyjson.com/products/images/beauty/Eyeshadow%20Palette%20with%20Mirror/thumbnail.png",
            images = listOf("https://cdn.dummyjson.com/products/images/beauty/Eyeshadow%20Palette%20with%20Mirror/1.png"),
            category = null,
            discountPercentage = null,
            rating = null,
            stock = null,
            tags = null,
            sku = null,
            weight = null,
            dimensions = null,
            warrantyInformation = null,
            shippingInformation = null,
            availabilityStatus = null,
            reviews = null,
            returnPolicy = null,
            minimumOrderQuantity = null,
            meta = null,
        )

        val PRODUCT_2: Product = Product(
            id = 2,
            title = "Calvin Klein CK One",
            description = "CK One by Calvin Klein is a classic unisex fragrance, known for its fresh and clean scent. It's a versatile fragrance suitable for everyday wear.",
            brand = "Calvin Klein",
            price = 49.99,
            thumbnail = "https://cdn.dummyjson.com/products/images/fragrances/Calvin%20Klein%20CK%20One/thumbnail.png",
            images = listOf("https://cdn.dummyjson.com/products/images/fragrances/Calvin%20Klein%20CK%20One/1.png"),
            category = null,
            discountPercentage = null,
            rating = null,
            stock = null,
            tags = null,
            sku = null,
            weight = null,
            dimensions = null,
            warrantyInformation = null,
            shippingInformation = null,
            availabilityStatus = null,
            reviews = null,
            returnPolicy = null,
            minimumOrderQuantity = null,
            meta = null,
        )

        val PRODUCTS: List<Product> = listOf(PRODUCT_1, PRODUCT_2)

        fun getSuccessfulRemoteProducts(): Response<ProductsSearchResponse> {
            val responseWrapper = ProductsSearchResponse(
                products = PRODUCTS,
                total = 194,
                skip = 0,
                limit = 30
            )

            val response: Response<ProductsSearchResponse> = Response.success(responseWrapper)

            return response
        }

        fun getFailedRemoteProducts(): Response<ProductsSearchResponse> {
            val responseBody: ResponseBody = ResponseBody.create(null, ERROR_MESSAGE)

            val response: Response<ProductsSearchResponse> = Response.error(HTTP_STATUS_CODE_404, responseBody)

            return response
        }

        fun getSuccessfulRemoteProduct(): Response<ProductDetailsResponse> {
            val responseWrapper = ProductDetailsResponse(
                id = 1,
                title = "Eyeshadow Palette with Mirror",
                description = "The Eyeshadow Palette with Mirror offers a versatile range of eyeshadow " +
                        "shades for creating stunning eye looks. With a built-in mirror, it's convenient " +
                        "for on-the-go makeup application.",
                brand = "Glamour Beauty",
                price = 19.99,
                thumbnail = "https://cdn.dummyjson.com/products/images/beauty/Eyeshadow%20Palette%20with%20Mirror/thumbnail.png",
                images = listOf("https://cdn.dummyjson.com/products/images/beauty/Eyeshadow%20Palette%20with%20Mirror/1.png"),
                category = null,
                discountPercentage = null,
                rating = null,
                stock = null,
                tags = null,
                sku = null,
                weight = null,
                dimensions = null,
                warrantyInformation = null,
                shippingInformation = null,
                availabilityStatus = null,
                reviews = null,
                returnPolicy = null,
                minimumOrderQuantity = null,
                meta = null,
            )

            val response: Response<ProductDetailsResponse> = Response.success(responseWrapper)

            return response
        }

        fun getFailedRemoteProduct(): Response<ProductDetailsResponse> {
            val responseBody: ResponseBody = ResponseBody.create(null, ERROR_MESSAGE)

            val response: Response<ProductDetailsResponse> = Response.error(HTTP_STATUS_CODE_404, responseBody)

            return response
        }

    }

    override suspend fun getRemoteProducts(skip: String): Response<ProductsSearchResponse> {
        if (isGetRemoteProductsException) throw Exception()

        if (isGetRemoteProductsFailed) return getFailedRemoteProducts()

        return getSuccessfulRemoteProducts()
    }

    override suspend fun getRemoteProduct(id: String): Response<ProductDetailsResponse> {
        if (isGetRemoteProductException) throw Exception()

        if (isGetRemoteProductFailed) return getFailedRemoteProduct()

        return getSuccessfulRemoteProduct()
    }

    override suspend fun countAllProducts(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getProducts(): List<ProductEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getProducts(name: String): List<ProductEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getProduct(id: String): ProductEntity {
        TODO("Not yet implemented")
    }

    override suspend fun insertProduct(product: ProductEntity): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(id: String): Int {
        TODO("Not yet implemented")
    }
}
