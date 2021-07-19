package br.gustavoakira.hotel.di


import br.gustavoakira.hotel.details.HotelDetailsModelView
import br.gustavoakira.hotel.form.HotelFormModelView
import br.gustavoakira.hotel.list.HotelListViewModel
import br.gustavoakira.hotel.repository.interfaces.HotelRepository
import br.gustavoakira.hotel.repository.room.HotelDatabase
import br.gustavoakira.hotel.repository.room.RoomRepository
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


val androidModule = module {
    single { this }
    single {
        RoomRepository(HotelDatabase.getDatabase(context = get())) as HotelRepository
    }
    viewModel {
        HotelListViewModel(repository = get())
    }
    viewModel {
        HotelDetailsModelView(repository = get())
    }
    viewModel {
        HotelFormModelView(repository = get())
    }
}