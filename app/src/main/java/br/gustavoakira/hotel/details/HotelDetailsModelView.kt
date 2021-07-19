package br.gustavoakira.hotel.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.gustavoakira.hotel.model.Hotel
import br.gustavoakira.hotel.repository.interfaces.HotelRepository

class HotelDetailsModelView(private val repository: HotelRepository): ViewModel() {
    fun loadHotelDetails(id:Long): LiveData<Hotel>{
        return repository.hotelById(id)
    }


}