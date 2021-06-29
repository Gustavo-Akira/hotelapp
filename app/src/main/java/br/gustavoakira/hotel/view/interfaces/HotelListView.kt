package br.gustavoakira.hotel.view.interfaces

import br.gustavoakira.hotel.model.Hotel

interface HotelListView {
    fun showHotels(hotels: List<Hotel>)
    fun showHotelDetails(hotel: Hotel)
}