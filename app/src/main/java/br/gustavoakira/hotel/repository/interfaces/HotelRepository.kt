package br.gustavoakira.hotel.repository.interfaces

import androidx.lifecycle.LiveData
import br.gustavoakira.hotel.model.Hotel

interface HotelRepository {
    fun save(hotel: Hotel)
    fun remove(vararg hotels: Hotel)
    fun hotelById(id: Long): LiveData<Hotel>
    fun search(term: String): LiveData<List<Hotel>>
}