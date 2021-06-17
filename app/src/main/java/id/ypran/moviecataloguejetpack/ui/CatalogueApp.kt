package id.ypran.moviecataloguejetpack.ui

import android.app.Application
import id.ypran.moviecataloguejetpack.ui.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CatalogueApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CatalogueApp)
            modules(appComponent)
        }
    }
}