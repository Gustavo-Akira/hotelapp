package br.gustavoakira.hotel.list

import br.gustavoakira.hotel.model.Hotel

interface HotelListView {
    fun showHotels(hotels: List<Hotel>)
    fun showHotelDetails(hotel: Hotel)
    fun showDeleteMode()
    fun hideDeleteMode()
    fun showSelectedHotels(hotels: List<Hotel>)
    fun updateSelectionCountText(count: Int)
    fun showMessageHotelDeleted(count: Int)
}