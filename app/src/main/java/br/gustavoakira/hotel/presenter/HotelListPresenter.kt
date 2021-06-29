package br.gustavoakira.hotel.presenter

import br.gustavoakira.hotel.model.Hotel
import br.gustavoakira.hotel.repository.interfaces.HotelRepository
import br.gustavoakira.hotel.view.interfaces.HotelListView

class HotelListPresenter(
    private val view: HotelListView,
    private val repository: HotelRepository
) {
    fun searchHotels(term: String){
        repository.search(term){list: List<Hotel> ->
            view.showHotels(list)
        }
    }

    fun showHotelDetails(hotel: Hotel){
        view.showHotelDetails(hotel)
    }
}