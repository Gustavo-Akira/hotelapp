package br.gustavoakira.hotel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import br.gustavoakira.hotel.model.Hotel
import br.gustavoakira.hotel.view.HotelDetailsFragment
import br.gustavoakira.hotel.view.HotelListFragment

class MainActivity : AppCompatActivity(), HotelListFragment.OnHotelClick, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    private var lastSearchString: String = ""
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onHotelClick(hotel: Hotel) {
        if(isTablet()){
            showDetailsFragment(hotel.id)
        }else {
            showDetailsActivity(hotel.id)
        }
    }

    private fun isTablet() = resources.getBoolean(R.bool.tablet)

    private fun showDetailsActivity(hotelId: Long){
        HotelDetailsActivity.open(this, hotelId)
    }

    private fun showDetailsFragment(hotelId: Long){
        val fragment = HotelDetailsFragment.newInstance(hotelId)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.details, fragment, HotelDetailsFragment.TAG_DETAILS)
            commit()
        }
    }

    private val listFragment: HotelListFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragment_list) as HotelListFragment
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }
}