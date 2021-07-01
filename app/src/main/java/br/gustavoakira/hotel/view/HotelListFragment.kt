package br.gustavoakira.hotel.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import br.gustavoakira.hotel.model.Hotel
import br.gustavoakira.hotel.presenter.HotelListPresenter
import br.gustavoakira.hotel.repository.MemoryRepository
import br.gustavoakira.hotel.view.interfaces.HotelListView

class HotelListFragment: ListFragment(), HotelListView{
    private val presenter = HotelListPresenter(this, MemoryRepository)

    fun search(text: String){
        presenter.searchHotels(text)
    }

    fun clearSearch(){
        presenter.searchHotels("")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.searchHotels("")
    }

    override fun showHotels(hotels: List<Hotel>) {
        val adapter = ArrayAdapter<Hotel>(requireContext(), android.R.layout.simple_list_item_1, hotels)
        listAdapter = adapter
    }

    override fun showHotelDetails(hotel: Hotel) {
        if(activity is OnHotelClick){
            val listener = activity as OnHotelClick
            listener.onHotelClick(hotel)
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val hotel = l?.getItemAtPosition(position) as Hotel
        presenter.showHotelDetails(hotel)
    }

    interface OnHotelClick{
        fun onHotelClick(hotel: Hotel)
    }

}