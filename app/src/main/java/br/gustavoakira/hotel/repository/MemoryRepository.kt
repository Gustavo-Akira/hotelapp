package br.gustavoakira.hotel.repository

import br.gustavoakira.hotel.model.Hotel
import br.gustavoakira.hotel.repository.interfaces.HotelRepository

object MemoryRepository: HotelRepository{
    private var nextId: Long = 0
    private val hotelList = mutableListOf<Hotel>()

    init{
        save(Hotel(0, "Byanca Beach Hotel", "Rua Mamanguape",4.0f))
        save(Hotel(0, "Grand Hotel Dor", "Av Bernardo",3.5f))
        save(Hotel(0, "Hotel Cool", "Av Conselheiro Angular",4.0f))
        save(Hotel(0, "Hotel Infinito", "Rua Ribeiro de Brito",4.0f))
    }

    override fun save(hotel: Hotel) {
        if(hotel.id == 0L){
            hotel.id = nextId++
            hotelList.add(hotel)
        }else{
            val index = hotelList.indexOfFirst { it.id == hotel.id }
            if(index > -1){
                hotelList[index] = hotel
            }else{
                hotelList.add(hotel)
            }
        }
    }

    override fun remove(vararg hotels: Hotel) {
        hotelList.removeAll(hotels)
    }

    override fun hotelById(id: Long, callback: (Hotel?) -> Unit) {
        callback(hotelList.find { it.id == id })
    }

    override fun search(term: String, callback: (List<Hotel>) -> Unit) {
        callback(
            if(term.isEmpty()) hotelList
            else hotelList.filter {
                it.name.uppercase().contains(term.uppercase())
            }
        )
    }
}