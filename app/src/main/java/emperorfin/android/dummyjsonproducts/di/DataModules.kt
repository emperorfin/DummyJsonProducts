package emperorfin.android.dummyjsonproducts.di

import android.content.Context
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import emperorfin.android.dummyjsonproducts.BuildConfig
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.AppRoomDatabase
import emperorfin.android.dummyjsonproducts.data.datasource.local.framework.room.entitysource.ProductsLocalDataSourceRoom
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.dtosource.ProductsRemoteDataSourceRetrofit
import emperorfin.android.dummyjsonproducts.data.datasource.remote.framework.retrofit.webservice.dummyjson.api.ProductsApi
import emperorfin.android.dummyjsonproducts.data.repository.ProductsRepository
import emperorfin.android.dummyjsonproducts.domain.datalayer.dao.IProductsDao
import emperorfin.android.dummyjsonproducts.domain.datalayer.datasource.ProductsDataSource
import emperorfin.android.dummyjsonproducts.domain.datalayer.repository.IProductsRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ProductsLocalDataSource

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ProductsRemoteDataSource

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class LocalProductsDao

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class RemoteProductsDao

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    @ProductsLocalDataSource
    abstract fun bindProductsLocalDataSourceRoom(dataSource: ProductsLocalDataSourceRoom): ProductsDataSource

    @Singleton
    @Binds
    @ProductsRemoteDataSource
    abstract fun bindProductsRemoteDataSourceRetrofit(dataSource: ProductsRemoteDataSourceRetrofit): ProductsDataSource
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindProductsRepository(repository: ProductsRepository): IProductsRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesAppDatabase(
        @ApplicationContext context: Context
    ): AppRoomDatabase {
        return AppRoomDatabase.getInstance(context)
    }

    @Provides
    @LocalProductsDao
    fun provideProductsDao(database: AppRoomDatabase): IProductsDao = database.mProductsDao
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

//    @Provides
//    @Singleton
//    fun provideOkHttpClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(RequestInterceptor())
//            .build()
//    }

    @Provides
    @Singleton
    fun provideRetrofit(okhHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
//            .client(okhHttpClient)
            .baseUrl(BuildConfig.DUMMY_JSON_BASE_URL)
//            .addConverterFactory(MoshiConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @RemoteProductsDao
    fun provideProductsApi(retrofit: Retrofit): IProductsDao {
        return retrofit.create(ProductsApi::class.java)
    }
}