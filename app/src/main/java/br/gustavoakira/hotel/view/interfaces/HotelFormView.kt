package br.gustavoakira.hotel.view.interfaces

import br.gustavoakira.hotel.model.Hotel

interface HotelFormView {
    fun showHotel(hotel:Hotel)
    fun errorInvalidHotel()
    fun errorSaveHotel()
}