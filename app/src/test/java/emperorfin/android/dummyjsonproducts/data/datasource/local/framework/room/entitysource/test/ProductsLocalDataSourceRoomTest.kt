package emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entitysource.test

import emperorfin.android.dummyjsonproducts.data.constant.StringConstants.ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED
import emperorfin.android.dummyjsonproducts.data.constant.StringConstants.ERROR_MESSAGE_NOT_YET_IMPLEMENTED
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.dao.FakeProductsDao
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.dao.FakeProductsDao.Companion.NUMBER_OF_PRODUCTS_RETRIEVED_2
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.dao.FakeProductsDao.Companion.TABLE_ROW_ID
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntityMapper
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entitysource.ProductsLocalDataSourceRoom
import emperorfin.android.dummyjsonproducts.domain.exception.ProductFailure
import emperorfin.android.dummyjsonproducts.domain.model.product.ProductModel
import emperorfin.android.dummyjsonproducts.domain.model.product.ProductModelMapper
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.input.product.None
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.input.product.ProductParams
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.ResultData
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product.Params
import emperorfin.android.dummyjsonproducts.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsEqual.equalTo
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException


internal class ProductsLocalDataSourceRoomTest {

    private companion object {

        const val NUM_OF_PRODUCTS_MINUS_1: Int = -1
        const val NUM_OF_PRODUCTS_0: Int = 0
        const val NUM_OF_PRODUCTS_DELETED_1: Int = 1

        const val IS_EXCEPTION_TRUE: Boolean = true
        const val IS_PRODUCTS_LIST_EMPTY_TRUE: Boolean = true

        val PARAMS_NONE: None = None()
        val PARAMS_PRODUCT: ProductParams = ProductParams(id = 1L, title = "Calvin Klein CK One")
        val PARAMS_BAD: BadParams = BadParams()

    }

    private lateinit var productsDao: FakeProductsDao

    // Class under test
    private lateinit var productsLocalDataSource: ProductsLocalDataSourceRoom

    @JvmField
    @Rule
    val expectedException: ExpectedException = ExpectedException.none()

    @Before
    fun createLocalDataSource() {

        productsDao = FakeProductsDao()

        productsLocalDataSource = ProductsLocalDataSourceRoom(
            productDao = productsDao,
            ioDispatcher = Dispatchers.Unconfined,
            productEntityMapper = ProductEntityMapper(),
            productModelMapper = ProductModelMapper(),
        )
    }

    @Test
    fun countAllProducts_ProductsMoreThanZero() = runTest {
        val numberOfProductsExpected: Int = NUMBER_OF_PRODUCTS_RETRIEVED_2

        val params = PARAMS_NONE

        val numberOfProductsResultData: ResultData.Success<Int> = productsLocalDataSource
            .countAllProducts(params = params) as ResultData.Success

        assertThat(numberOfProductsResultData.data, IsEqual(numberOfProductsExpected))
    }

    @Test
    fun countAllProducts_NonExistentProductDataError() = runTest {

        productsDao = productsDao.copy(numberOfProductsRetrieved = NUM_OF_PRODUCTS_0)

        productsLocalDataSource = productsLocalDataSource.copy(
            productDao = productsDao
        )

        val params = PARAMS_NONE

        val errorResultData: ResultData.Error = productsLocalDataSource
            .countAllProducts(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(ProductFailure.NonExistentProductDataLocalError::class.java))
    }

    @Test
    fun countAllProducts_GeneralError() = runTest {

        productsDao = productsDao.copy(numberOfProductsRetrieved = NUM_OF_PRODUCTS_MINUS_1)

        productsLocalDataSource = productsLocalDataSource.copy(
            productDao = productsDao
        )

        val params = PARAMS_NONE

        val errorResultData: ResultData.Error = productsLocalDataSource
            .countAllProducts(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(ProductFailure.ProductLocalError::class.java))
    }

    @Test
    fun countAllProducts_ExceptionThrown() = runTest {

        productsDao = productsDao.copy(isCountAllProductsException = IS_EXCEPTION_TRUE)

        productsLocalDataSource = productsLocalDataSource.copy(
            productDao = productsDao
        )

        val params = PARAMS_NONE

        val errorResultData: ResultData.Error = productsLocalDataSource
            .countAllProducts(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(ProductFailure.ProductLocalError::class.java))
    }

    @Test
    fun countAllProducts_IllegalArgumentExceptionThrown() = runTest {

        val params = ProductParams(id = 1L)

        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED))

        productsLocalDataSource.countAllProducts(params = params)
    }

    @Test
    fun countAllProducts_NotImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_NOT_YET_IMPLEMENTED))

        productsLocalDataSource.countAllProducts(params = params)
    }

    @Test
    fun getProducts_ProductListNotEmpty() = runTest {

        val product1: ProductModel = ProductModel.newInstance(
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

        val product2: ProductModel = ProductModel.newInstance(
            id = 2,
            title = "Calvin Klein CK One",
            description = "CK One by Calvin Klein is a classic unisex fragrance, known for its fresh and clean scent. It's a versatile fragrance suitable for everyday wear.",
            brand = "Calvin Klein",
            price = 49.99,
            thumbnail = "https://cdn.dummyjson.com/products/images/fragrances/Calvin%20Klein%20CK%20One/thumbnail.png",
            image = "https://cdn.dummyjson.com/products/images/fragrances/Calvin%20Klein%20CK%20One/1.png",
        )

        val productsModel = listOf(product1, product2)

        val params = PARAMS_PRODUCT

        val productsModelResultData: ResultData.Success<List<ProductModel>> = productsLocalDataSource
            .getProducts(params = params) as ResultData.Success

        assertThat(productsModelResultData.data, IsEqual(productsModel))
    }

    @Test
    fun getProducts_ProductListNotAvailableLocalError() = runTest {

        productsDao = productsDao.copy(isGetProductsEmptyList = IS_PRODUCTS_LIST_EMPTY_TRUE)

        productsLocalDataSource = productsLocalDataSource.copy(
            productDao = productsDao
        )

        val params = PARAMS_PRODUCT

        val errorResultData: ResultData.Error = productsLocalDataSource
            .getProducts(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(ProductFailure.ProductListNotAvailableLocalError::class.java))
    }

    @Test
    fun getProducts_ExceptionThrown() = runTest {

        productsDao = productsDao.copy(isGetProductsException = IS_EXCEPTION_TRUE)

        productsLocalDataSource = productsLocalDataSource.copy(
            productDao = productsDao
        )

        val params = PARAMS_PRODUCT

        val errorResultData: ResultData.Error = productsLocalDataSource
            .getProducts(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(ProductFailure.ProductLocalError::class.java))
    }

    @Test
    fun getProducts_NotImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_NOT_YET_IMPLEMENTED))

        productsLocalDataSource.getProducts(params = params)
    }

    @Test
    fun saveProduct_ProductSavedSuccessfully() = runTest {
        val tableRowIdExpected: Long = TABLE_ROW_ID

        val productModel = ProductModel.newInstance(
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

        val tableRowIdResultData: ResultData.Success<Long> = productsLocalDataSource
            .saveProduct(product = productModel) as ResultData.Success

        assertThat(tableRowIdResultData.data, IsEqual(tableRowIdExpected))
    }

    @Test
    fun saveProduct_ProductInsertError() = runTest {

        productsDao = productsDao.copy(insertedProductTableRowId = -1L)

        productsLocalDataSource = productsLocalDataSource.copy(
            productDao = productsDao
        )

        val errorMessageExpected: Int = R.string.error_local_insert_product

        val productModel = ProductModel.newInstance(
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

        val errorResultData: ResultData.Error = productsLocalDataSource
            .saveProduct(product = productModel) as ResultData.Error

        val errorMessageActual: Int = (errorResultData.failure as ProductFailure.InsertProductLocalError).message

        assertThat(errorMessageActual, IsEqual(errorMessageExpected))
        assertThat(errorResultData.failure, IsInstanceOf(ProductFailure.InsertProductLocalError::class.java))
    }

    @Test
    fun deleteProduct_ProductDeletedSuccessfully() = runTest {
        val numOfProductsDeletedExpected: Int = NUM_OF_PRODUCTS_DELETED_1

        val params = PARAMS_PRODUCT

        val numOfProductsDeletedResultData: ResultData.Success<Int> = productsLocalDataSource
            .deleteProduct(params = params) as ResultData.Success

        assertThat(numOfProductsDeletedResultData.data, IsEqual(numOfProductsDeletedExpected))
    }


    @Test
    fun deleteProduct_ErrorDeletingProduct() = runTest {

        productsDao = productsDao.copy(numberOfProductsDeleted = NUM_OF_PRODUCTS_MINUS_1)

        productsLocalDataSource = productsLocalDataSource.copy(
            productDao = productsDao
        )

        val params = PARAMS_PRODUCT

        val errorMessageExpected: Int = R.string.error_local_delete_product

        val errorResultData: ResultData.Error = productsLocalDataSource
            .deleteProduct(params = params) as ResultData.Error

        val errorMessageActual: Int = (errorResultData.failure as ProductFailure.DeleteProductLocalError).message

        assertThat(errorMessageActual, IsEqual(errorMessageExpected))
        assertThat(errorResultData.failure, IsInstanceOf(ProductFailure.DeleteProductLocalError::class.java))
    }

    @Test
    fun deleteProduct_ExceptionThrown() = runTest {

        productsDao = productsDao.copy(isDeleteProductException = IS_EXCEPTION_TRUE)

        productsLocalDataSource = productsLocalDataSource.copy(
            productDao = productsDao
        )

        val params = PARAMS_PRODUCT

        val errorResultData: ResultData.Error = productsLocalDataSource
            .deleteProduct(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(ProductFailure.DeleteProductLocalError::class.java))
    }

    @Test
    fun deleteProduct_IllegalArgumentExceptionThrown() = runTest {

        val params = PARAMS_NONE

        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED))

        productsLocalDataSource.deleteProduct(params = params)
    }

    @Test
    fun deleteProduct_NotImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_NOT_YET_IMPLEMENTED))

        productsLocalDataSource.deleteProduct(params = params)
    }

}

private class BadParams : Params