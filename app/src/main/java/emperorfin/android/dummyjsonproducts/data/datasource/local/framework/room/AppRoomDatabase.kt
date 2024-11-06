package emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.dao.ProductsDao
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entity.product.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract val mProductsDao: ProductsDao

    companion object {

        private const val DATABASE_NAME = "database_app"

        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getInstance(context: Context): AppRoomDatabase{

            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppRoomDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }

        }

    }
}