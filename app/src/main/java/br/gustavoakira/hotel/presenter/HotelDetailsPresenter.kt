package br.gustavoakira.hotel.presenter

import br.gustavoakira.hotel.repository.interfaces.HotelRepository
import br.gustavoakira.hotel.view.interfaces.HotelDetailsView

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