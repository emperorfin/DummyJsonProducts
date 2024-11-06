package emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntity.Companion.COLUMN_INFO_ID
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntity.Companion.TABLE_NAME
import emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product.ProductEntityParams

@Entity(
    tableName = TABLE_NAME,
    primaryKeys = [COLUMN_INFO_ID]
)
data class ProductEntity(
    @ColumnInfo(name = COLUMN_INFO_ID)
    override val id: Long,
    @ColumnInfo(name = COLUMN_INFO_TITLE)
    override val title: String,
    @ColumnInfo(name = COLUMN_INFO_DESCRIPTION)
    override val description: String,
    @ColumnInfo(name = COLUMN_INFO_BRAND)
    override val brand: String,
    @ColumnInfo(name = COLUMN_INFO_PRICE)
    override val price: Double,
    @ColumnInfo(name = COLUMN_INFO_THUMBNAIL)
    override val thumbnail: String,
    @ColumnInfo(name = COLUMN_INFO_IMAGE)
    override val image: String
) : ProductEntityParams {

    companion object {

        const val TABLE_NAME = "table_products"

        const val COLUMN_INFO_ID = "id"
        const val COLUMN_INFO_TITLE = "title"
        const val COLUMN_INFO_DESCRIPTION = "description"
        const val COLUMN_INFO_BRAND = "brand"
        const val COLUMN_INFO_PRICE = "price"
        const val COLUMN_INFO_THUMBNAIL = "thumbnail"
        const val COLUMN_INFO_IMAGE = "image"

        fun newInstance(
            id: Long,
            title: String,
            description: String,
            brand: String,
            price: Double,
            thumbnail: String,
            image: String
        ): ProductEntity {
            return ProductEntity(
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
