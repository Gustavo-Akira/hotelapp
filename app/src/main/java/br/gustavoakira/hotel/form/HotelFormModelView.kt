package br.gustavoakira.hotel.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.gustavoakira.hotel.model.Hotel
import br.gustavoakira.hotel.repository.interfaces.HotelRepository

class HotelFormModelView(
    private val repository: HotelRepository
):ViewModel(){
    private val validator by lazy{HotelValidator()}
    fun loadHotel(id:Long): LiveData<Hotel>{
      return repository.hotelById(id)
    }
    fun saveHotel(hotel: Hotel): Boolean{
        return validator.validate(hotel).also {
            validated->
            if(validated) repository.save(hotel)
        }
    }
}