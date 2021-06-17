package id.ypran.search.di

import android.app.Application
import androidx.room.Room
import id.ypran.core.BuildConfig
import id.ypran.core.createNetworkClient
import id.ypran.search.data.repository.SearchCatalogueRepositoryImpl
import id.ypran.search.datasource.local.SearchCatalogueDao
import id.ypran.search.datasource.local.SearchCatalogueDb
import id.ypran.search.datasource.local.entity.SearchRemoteKeysDao
import id.ypran.search.datasource.mediator.SearchRemoteMediator
import id.ypran.search.datasource.remote.SearchApi
import id.ypran.search.domain.repository.SearchCatalogueRepository
import id.ypran.search.domain.usecase.SearchCatalogueByTitleUseCase
import id.ypran.search.ui.SearchCatalogueViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(searchComponent)
}

val viewModelModule: Module = module {
    viewModel { SearchCatalogueViewModel(get()) }
}

val useCaseModule: Module = module {
    single { SearchCatalogueByTitleUseCase(get()) }
}

val repositoryModule: Module = module {
    single { SearchCatalogueRepositoryImpl(get()) as SearchCatalogueRepository }
}

val pagingSourceModule: Module = module {
    factory { (query: String) ->
        SearchRemoteMediator(
            searchApi = get(),
            searchCatalogueDb = get(),
            key = TMDBApiKey,
            query = query
        )
    }
}

val databaseModule: Module = module {
    fun provideDatabase(application: Application): SearchCatalogueDb = Room.databaseBuilder(
        application,
        SearchCatalogueDb::class.java,
        "search-catalogue-db"
    )
        .fallbackToDestructiveMigration()
        .build()
    single { provideDatabase(androidApplication()) }
}

val localSourceModule: Module = module {
    fun provideSearchCatalogueDao(database: SearchCatalogueDb): SearchCatalogueDao =
        database.searchCatalogueDao

    fun provideSearchRemoteKeysDao(database: SearchCatalogueDb): SearchRemoteKeysDao =
        database.searchRemoteKeysDao

    single { provideSearchCatalogueDao(get()) }
    single { provideSearchRemoteKeysDao(get()) }
}

val remoteSourceModule: Module = module {
    single { searchApi }
}

private const val TMDBApiKey = BuildConfig.TMDB_API_KEY
private const val BASE_URL = "https://api.themoviedb.org/3/"
private val retrofit: Retrofit = createNetworkClient(BASE_URL)

private val searchApi: SearchApi = retrofit.create(SearchApi::class.java)

val searchComponent = listOf(
    viewModelModule,
    useCaseModule,
    repositoryModule,
    pagingSourceModule,
    databaseModule,
    localSourceModule,
    remoteSourceModule
)