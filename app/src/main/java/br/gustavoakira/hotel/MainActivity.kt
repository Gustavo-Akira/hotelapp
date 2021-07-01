package br.gustavoakira.hotel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
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
        searchView?.setOnQueryTextListener(null)
        val fragment = HotelDetailsFragment.newInstance(hotelId)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.details, fragment, HotelDetailsFragment.TAG_DETAILS)
            commit()
        }
    }

    private val listFragment: HotelListFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragment_list) as HotelListFragment
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_SEARCH_TERM,lastSearchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        lastSearchString = savedInstanceState.getString(EXTRA_SEARCH_TERM) ?: ""
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        listFragment.search(query.toString())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        lastSearchString = newText ?: ""
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.hotel, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.setOnActionExpandListener(this)
        searchView = searchItem?.actionView as SearchView
        searchView?.queryHint = getString(R.string.hint_search)
        searchView?.setOnQueryTextListener(this)
        if(lastSearchString.isNotEmpty()){
            Handler(Looper.getMainLooper()).post{
                val query =lastSearchString
                searchItem.expandActionView()
                searchView?.setQuery(query, true)
                searchView?.clearFocus()
            }
        }
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        lastSearchString = ""
        listFragment.clearSearch()
        return true
    }

    companion object{
        const val EXTRA_SEARCH_TERM = "lastSearch"
    }
}