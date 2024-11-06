package emperorfin.android.dummyjsonproducts.ui.extention

import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow


inline fun <T> LazyGridScope.paging(
    items: List<T>,
    newProductsLength: StateFlow<Int>,
    threshold: Int = 4,
    crossinline fetch: () -> Unit,
    crossinline itemContent: @Composable (index: Int, item: T) -> Unit,
) {
    val _newProductsLength = newProductsLength.value

    itemsIndexed(items) { index, item ->

        itemContent(index, item)

        if ((index + threshold + 1) >= _newProductsLength) {

            fetch()

        }
    }
}