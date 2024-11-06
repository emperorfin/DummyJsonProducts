package emperorfin.android.dummyjsonproducts.domain.exception

import androidx.annotation.StringRes
import emperorfin.android.dummyjsonproducts.domain.exception.Failure.FeatureFailure
import emperorfin.android.dummyjsonproducts.R

sealed class ProductFailure(
    @StringRes open val message: Int,
    open val cause: Throwable?
) : FeatureFailure() {

    class ProductListNotAvailableMemoryError(
        @StringRes override val message: Int = R.string.error_memory_product_list_not_available,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class ProductListNotAvailableLocalError(
        @StringRes override val message: Int = R.string.error_local_product_list_not_available,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class ProductListNotAvailableRemoteError(
        @StringRes override val message: Int = R.string.error_remote_product_list_not_available,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class ProductMemoryError(
        @StringRes override val message: Int = R.string.error_memory_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class ProductLocalError(
        @StringRes override val message: Int = R.string.error_local_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class ProductRemoteError(
        @StringRes override val message: Int = R.string.error_remote_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class GetProductMemoryError(
        @StringRes override val message: Int = R.string.error_memory_get_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class GetProductLocalError(
        @StringRes override val message: Int = R.string.error_local_get_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class GetProductRemoteError(
        @StringRes override val message: Int = R.string.error_remote_get_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class InsertProductMemoryError(
        @StringRes override val message: Int = R.string.error_memory_insert_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class InsertProductLocalError(
        @StringRes override val message: Int = R.string.error_local_insert_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class InsertProductRemoteError(
        @StringRes override val message: Int = R.string.error_remote_insert_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class UpdateProductMemoryError(
        @StringRes override val message: Int = R.string.error_memory_update_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class UpdateProductLocalError(
        @StringRes override val message: Int = R.string.error_local_update_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class UpdateProductRemoteError(
        @StringRes override val message: Int = R.string.error_remote_update_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class DeleteProductMemoryError(
        @StringRes override val message: Int = R.string.error_memory_delete_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class DeleteProductLocalError(
        @StringRes override val message: Int = R.string.error_local_delete_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class DeleteProductRemoteError(
        @StringRes override val message: Int = R.string.error_remote_delete_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class NonExistentProductDataMemoryError(
        @StringRes override val message: Int = R.string.error_memory_non_existent_product_data,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class NonExistentProductDataLocalError(
        @StringRes override val message: Int = R.string.error_local_non_existent_product_data,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class NonExistentProductDataRemoteError(
        @StringRes override val message: Int = R.string.error_remote_non_existent_product_data,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    // For Repositories
    class GetProductRepositoryError(
        @StringRes override val message: Int = R.string.error_repository_get_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class InsertProductRepositoryError(
        @StringRes override val message: Int = R.string.error_repository_insert_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class DeleteProductRepositoryError(
        @StringRes override val message: Int = R.string.error_repository_delete_product,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)

    class ProductListNotAvailableRepositoryError(
        @StringRes override val message: Int = R.string.error_repository_product_list_not_available,
        override val cause: Throwable? = null
    ) : ProductFailure(message = message, cause = cause)
}