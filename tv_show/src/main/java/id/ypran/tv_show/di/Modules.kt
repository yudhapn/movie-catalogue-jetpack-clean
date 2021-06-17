package id.ypran.tv_show.di

import id.ypran.tv_show.ui.SeriesDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(searchComponent)
}

val viewModelModule: Module = module {
    viewModel { SeriesDetailViewModel() }
}


val searchComponent = listOf(viewModelModule)