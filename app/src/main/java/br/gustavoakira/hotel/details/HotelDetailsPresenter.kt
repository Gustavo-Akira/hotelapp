package br.gustavoakira.hotel.details

import br.gustavoakira.hotel.repository.interfaces.HotelRepository

class HotelDetailsPresenter(
    private val repository: HotelRepository,
    private val view: HotelDetailsView
) {
    fun loadHotelDetails(id: Long){
        repository.hotelById(id){hotel ->
            if(hotel != null){
                view.showHotelDetails(hotel)
            }else{
                view.errorHotelNotFound()
            }
        }
    }
}