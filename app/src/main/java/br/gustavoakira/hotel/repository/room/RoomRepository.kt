package br.gustavoakira.hotel.repository.room

import androidx.lifecycle.LiveData
import br.gustavoakira.hotel.model.Hotel
import br.gustavoakira.hotel.repository.interfaces.HotelRepository

class RoomRepository(database: HotelDatabase): HotelRepository {
    private val hotelDao: HotelDao = database.hotelDao()
    override fun save(hotel: Hotel) {
        if(hotel.id == 0L){
            val id = hotelDao.insert(hotel)
            hotel.id = id
        }else{
            hotelDao.update(hotel)
        }
    }

    override fun remove(vararg hotels: Hotel) {
        hotelDao.delete(*hotels)
    }

    override fun hotelById(id: Long): LiveData<Hotel> {
        return hotelDao.hotelById(id)
    }

    override fun search(term: String): LiveData<List<Hotel>>{
        return hotelDao.search(term)
    }

}