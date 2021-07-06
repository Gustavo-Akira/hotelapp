package br.gustavoakira.hotel.form

import br.gustavoakira.hotel.model.Hotel
import br.gustavoakira.hotel.repository.interfaces.HotelRepository
import java.lang.Exception

class HotelFormPresenter(
    private val view: HotelFormView,
    private val repository: HotelRepository
) {
    private val validator: HotelValidator = HotelValidator()

    fun loadHotel(id: Long){
        repository.hotelById(id){hotel ->
            if(hotel != null){
                view.showHotel(hotel)
            }
        }
    }

    fun saveHotel(hotel:Hotel): Boolean{
        return if(validator.validate(hotel)){
            try {
                repository.save(hotel)
                true
            }catch (e: Exception){
                view.errorInvalidHotel()
                false
            }
        }else{
            view.errorInvalidHotel()
            false
        }
    }
}