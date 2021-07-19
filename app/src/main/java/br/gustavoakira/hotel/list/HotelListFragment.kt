package br.gustavoakira.hotel.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Observer
import br.gustavoakira.hotel.R
import br.gustavoakira.hotel.model.Hotel
import com.google.android.material.snackbar.Snackbar

import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HotelListFragment: ListFragment(), AdapterView.OnItemLongClickListener, ActionMode.Callback{
    private val hotelListViewModel: HotelListViewModel by sharedViewModel()
    private var actionMode: ActionMode? = null

    fun search(text: String){
        hotelListViewModel.search(text)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hotelListViewModel.showDetailsCommand().observe(viewLifecycleOwner, Observer {
            hotel ->
            if(hotel != null){
                showHotelDetails(hotel)
            }
        })
        hotelListViewModel.isDeleteMode().observe(viewLifecycleOwner, Observer {
            deleteMode->
            if(deleteMode == true){
                showDeleteMode()
            }else{
                hideDeleteMode()
            }
        })
        hotelListViewModel.selectedHotels().observe(viewLifecycleOwner, Observer {
            hotels->
            if(hotels != null){
                showSelectedHotels(hotels)
            }
        })
        hotelListViewModel.selectionCount().observe(viewLifecycleOwner, Observer {
            count ->
            if(count != null){
                updateSelectionCountText(count)
            }
        })
        hotelListViewModel.showDeletedMessage().observe(viewLifecycleOwner, Observer {
            count->
            if(count != null && count > 0){
                showMessageHotelDeleted(count)
            }
        })
        hotelListViewModel.getHotels()?.observe(viewLifecycleOwner, Observer {
            hotels->
            if(hotels != null){
                showHotels(hotels)
            }
        })
        if(hotelListViewModel.getHotels()?.value == null){
            search("")
        }
        listView.onItemLongClickListener = this
    }

    private fun showHotels(hotels: List<Hotel>) {
        val adapter = HotelAdapter(requireContext(), hotels)
        listAdapter = adapter
    }

    private fun showHotelDetails(hotel: Hotel) {
        if(activity is OnHotelClick){
            val listener = activity as OnHotelClick
            listener.onHotelClick(hotel)
        }
    }

    private fun showDeleteMode() {
        val appCompatActivity = (activity as AppCompatActivity)
        actionMode = appCompatActivity.startSupportActionMode(this)
        listView.onItemLongClickListener = null
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

     fun hideDeleteMode() {
        listView.onItemLongClickListener = this
        for(i in 0 until listView.count){
            listView.setItemChecked(i, false)
        }
        listView.post {
            actionMode?.finish()
            listView.choiceMode = ListView.CHOICE_MODE_NONE
        }
    }

    private fun showSelectedHotels(hotels: List<Hotel>) {
        listView.post {
            for(i in 0 until listView.count){
                val hotel = listView.getItemAtPosition(i) as Hotel
                if(hotels.find { it.id == hotel.id } != null){
                    listView.setItemChecked(i,true)
                }
            }
        }
    }

    private fun updateSelectionCountText(count: Int) {
        view?.post {
            actionMode?.title = resources.getQuantityString(R.plurals.list_hotel_selected, count, count)
        }
    }

    private fun showMessageHotelDeleted(count: Int) {
        Snackbar.make(listView,getString(R.string.message_hotels_deleted,count),Snackbar.LENGTH_LONG).setAction(R.string.undo){
            hotelListViewModel.undoDelete()
        }.show()
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val hotel = l?.getItemAtPosition(position) as Hotel
        hotelListViewModel.selectedHotel(hotel)
    }


    interface OnHotelClick{
        fun onHotelClick(hotel: Hotel)
    }

    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        val consumed = (actionMode == null)
        if(consumed){
            val hotel = parent?.getItemAtPosition(position) as Hotel
            hotelListViewModel.setInDeleteMode(true)
            hotelListViewModel.selectedHotel(hotel)
        }
        return consumed
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        activity?.menuInflater?.inflate(R.menu.hotel_delete_list, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if(item?.itemId == R.id.action_delete){
            hotelListViewModel.deleteSelected()
            return true
        }
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
        hotelListViewModel.setInDeleteMode(false)
    }

}