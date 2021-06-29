package br.gustavoakira.hotel.view.interfaces

import br.gustavoakira.hotel.model.Hotel

interface HotelDetailsView {
    fun showHotelDetails(hotel: Hotel)
    fun errorHotelNotFound()
}