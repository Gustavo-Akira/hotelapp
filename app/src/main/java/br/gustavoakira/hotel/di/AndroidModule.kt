package br.gustavoakira.hotel.di

import br.gustavoakira.hotel.details.HotelDetailsPresenter
import br.gustavoakira.hotel.details.HotelDetailsView
import br.gustavoakira.hotel.form.HotelFormPresenter
import br.gustavoakira.hotel.form.HotelFormView
import br.gustavoakira.hotel.list.HotelListPresenter
import br.gustavoakira.hotel.list.HotelListView
import br.gustavoakira.hotel.repository.interfaces.HotelRepository
import br.gustavoakira.hotel.repository.sqlite.ProviderRepository
import br.gustavoakira.hotel.repository.sqlite.SQLiteRepository
import org.koin.dsl.module.module

val androidModule = module {
    single { this }
    single {
        ProviderRepository(context = get()) as HotelRepository
    }
    factory { (view: HotelListView)->
        HotelListPresenter(
            view,
            repository = get()
        )
    }
    factory { (view: HotelDetailsView)->
        HotelDetailsPresenter(
            repository = get(),
            view
        )
    }
    factory { (view: HotelFormView)->
        HotelFormPresenter(
            view,
            repository = get()
        )
    }
}