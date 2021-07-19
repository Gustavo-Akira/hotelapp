package br.gustavoakira.hotel

import android.app.Application
import br.gustavoakira.hotel.di.androidModule
import org.koin.android.ext.android.startKoin
import org.koin.standalone.StandAloneContext

class HotelApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(androidModule))
    }

    override fun onTerminate() {
        super.onTerminate()
        StandAloneContext.stopKoin()
    }
}