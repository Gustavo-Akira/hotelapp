package br.gustavoakira.hotel.details

import br.gustavoakira.hotel.model.Hotel

interface HotelDetailsView {
    fun showHotelDetails(hotel: Hotel)
    fun errorHotelNotFound()
}