package br.gustavoakira.hotel.common

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import br.gustavoakira.hotel.details.HotelDetailsActivity
import br.gustavoakira.hotel.R
import br.gustavoakira.hotel.databinding.ActivityMainBinding
import br.gustavoakira.hotel.model.Hotel
import br.gustavoakira.hotel.details.HotelDetailsFragment
import br.gustavoakira.hotel.form.HotelFormFragment
import br.gustavoakira.hotel.list.HotelListFragment

class MainActivity : AppCompatActivity(), HotelListFragment.OnHotelClick, HotelListFragment.OnHotelDeletedListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, HotelFormFragment.OnHotelSavedListener {
    private var lastSearchString: String = ""
    private var searchView: SearchView? = null
    private var hotelIdSelected: Long = -1
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityMainBinding.inflate(layoutInflater)
        binding.fabAdd.setOnClickListener{
            listFragment.hideDeleteMode()
            HotelFormFragment.newInstance().open(supportFragmentManager)
        }
        setContentView(binding.root)
    }

    override fun onHotelClick(hotel: Hotel) {
        if(isTablet()){
            hotelIdSelected = hotel.id
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
        outState.putLong(EXTRA_HOTEL_ID_SELECTED, hotelIdSelected)
        outState.putString(EXTRA_SEARCH_TERM,lastSearchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        hotelIdSelected = savedInstanceState.getLong(EXTRA_HOTEL_ID_SELECTED)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK){
            listFragment.search(lastSearchString)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_info -> AboutDialogFragment().show(supportFragmentManager, "sobre")
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onHotelSaved(hotel: Hotel) {
        listFragment.search(lastSearchString)
        val detailsFragment = supportFragmentManager.findFragmentByTag(HotelDetailsFragment.TAG_DETAILS) as? HotelDetailsFragment
        if(detailsFragment != null && hotel.id == hotelIdSelected){
            showDetailsFragment(hotelIdSelected)
        }
    }

    override fun onHotelsDeleted(hotels: List<Hotel>) {
        if(hotels.find { it.id == hotelIdSelected } != null){
            val fragment = supportFragmentManager.findFragmentByTag(HotelDetailsFragment.TAG_DETAILS)
            if(fragment != null){
                supportFragmentManager.beginTransaction().remove(fragment).commit()
            }
        }
    }

    companion object{
        const val EXTRA_SEARCH_TERM = "lastSearch"
        const val EXTRA_HOTEL_ID_SELECTED="lastSelectedId"
    }
}