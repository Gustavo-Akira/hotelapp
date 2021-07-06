package br.gustavoakira.hotel.form

import br.gustavoakira.hotel.model.Hotel

interface HotelFormView {
    fun showHotel(hotel:Hotel)
    fun errorInvalidHotel()
    fun errorSaveHotel()
}