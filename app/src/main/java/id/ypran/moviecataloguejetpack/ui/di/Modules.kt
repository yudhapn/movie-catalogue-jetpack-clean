package id.ypran.moviecataloguejetpack.ui.di

import android.app.Application
import android.net.ConnectivityManager
import androidx.room.Room
import id.ypran.core.BuildConfig
import id.ypran.core.createNetworkClient
import id.ypran.core.datasource.local.CatalogueDao
import id.ypran.core.datasource.local.CatalogueDb
import id.ypran.core.datasource.local.MovieRemoteKeysDao
import id.ypran.core.datasource.mediator.MoviesRemoteMediator
import id.ypran.core.datasource.mediator.SeriesRemoteMediator
import id.ypran.core.datasource.remote.MovieApi
import id.ypran.core.datasource.remote.SeriesApi
import id.ypran.core.movie.data.repository.MoviesRepositoryImpl
import id.ypran.core.movie.domain.repository.MoviesRepository
import id.ypran.core.movie.domain.usecase.GetMoviesUseCase
import id.ypran.core.series.data.repository.SeriesRepositoryImpl
import id.ypran.core.series.domain.repository.SeriesRepository
import id.ypran.core.series.domain.usecase.GetTopRatedSeriesUseCase
import id.ypran.moviecataloguejetpack.ui.home.HomeViewModel
import id.ypran.moviecataloguejetpack.ui.movies.MoviesViewModel
import id.ypran.moviecataloguejetpack.ui.series.SeriesViewModel
import id.ypran.moviecataloguejetpack.ui.util.ConnectivityTracker
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

val viewModelModule: Module = module {
    viewModel { MoviesViewModel(get()) }
    viewModel { SeriesViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
}

val useCaseModule: Module = module {
    single { GetMoviesUseCase(get()) }
    single { GetTopRatedSeriesUseCase(get()) }
}

val databaseModule: Module = module {
    fun provideDatabase(application: Application): CatalogueDb =
        Room.databaseBuilder(application, CatalogueDb::class.java, "catalogue-db")
            .fallbackToDestructiveMigration()
            .build()
    single { provideDatabase(androidApplication()) }
}

val localDataSourceModule: Module = module {
    fun provideCatalogueDao(database: CatalogueDb): CatalogueDao = database.catalogueDao

    fun provideMovieRemoteKeysDao(database: CatalogueDb): MovieRemoteKeysDao =
        database.movieRemoteKeysDao
    single { provideCatalogueDao(get()) }
    single { provideMovieRemoteKeysDao(get()) }
}

val remoteDataSourceModule: Module = module {
    single { movieApi }
    single { seriesApi }
}

val repositoryModule: Module = module {
    single {
        MoviesRepositoryImpl(moviesRemoteMediator = get(), catalogueDb = get()) as MoviesRepository
    }
    single {
        SeriesRepositoryImpl(seriesRemoteMediator = get(), catalogueDb = get()) as SeriesRepository
    }
}

val pagingSourceModule: Module = module {
    single {
        MoviesRemoteMediator(movieApi = movieApi, key = TMDBApiKey, catalogueDb = get())
    }
    single {
        SeriesRemoteMediator(seriesApi = seriesApi, key = TMDBApiKey, catalogueDb = get())
    }
}

val connectivityModule: Module = module {
    single { (connectivityManager: ConnectivityManager) -> ConnectivityTracker(connectivityManager) }
}

private const val TMDBApiKey = BuildConfig.TMDB_API_KEY
private const val BASE_URL = "https://api.themoviedb.org/3/"
private val retrofit: Retrofit = createNetworkClient(BASE_URL)

private val movieApi: MovieApi = retrofit.create(MovieApi::class.java)
private val seriesApi: SeriesApi = retrofit.create(SeriesApi::class.java)

val appComponent =
    listOf(
        databaseModule,
        localDataSourceModule,
        remoteDataSourceModule,
        pagingSourceModule,
        repositoryModule,
        viewModelModule,
        useCaseModule,
        connectivityModule
    )