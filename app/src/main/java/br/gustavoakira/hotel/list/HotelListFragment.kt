package br.gustavoakira.hotel.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.ListFragment
import br.gustavoakira.hotel.R
import br.gustavoakira.hotel.model.Hotel
import br.gustavoakira.hotel.repository.MemoryRepository
import br.gustavoakira.hotel.repository.interfaces.HotelRepository
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class HotelListFragment: ListFragment(), HotelListView, AdapterView.OnItemLongClickListener, ActionMode.Callback{
    private val presenter: HotelListPresenter by inject{ parametersOf(this) }
    private var actionMode: ActionMode? = null

    fun search(text: String){
        presenter.searchHotels(text)
    }

    fun clearSearch(){
        presenter.searchHotels("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.searchHotels("")
        listView.onItemLongClickListener = this
    }

    override fun showHotels(hotels: List<Hotel>) {
        val adapter = HotelAdapter(requireContext(), hotels)
        listAdapter = adapter
    }

    override fun showHotelDetails(hotel: Hotel) {
        if(activity is OnHotelClick){
            val listener = activity as OnHotelClick
            listener.onHotelClick(hotel)
        }
    }

    override fun showDeleteMode() {
        val appCompatActivity = (activity as AppCompatActivity)
        actionMode = appCompatActivity.startSupportActionMode(this)
        listView.onItemLongClickListener = null
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    override fun hideDeleteMode() {
        listView.onItemLongClickListener = this
        for(i in 0 until listView.count){
            listView.setItemChecked(i, false)
        }
        listView.post {
            actionMode?.finish()
            listView.choiceMode = ListView.CHOICE_MODE_NONE
        }
    }

    override fun showSelectedHotels(hotels: List<Hotel>) {
        listView.post {
            for(i in 0 until listView.count){
                val hotel = listView.getItemAtPosition(i) as Hotel
                if(hotels.find { it.id == hotel.id } != null){
                    listView.setItemChecked(i,true)
                }
            }
        }
    }

    override fun updateSelectionCountText(count: Int) {
        view?.post {
            actionMode?.title = resources.getQuantityString(R.plurals.list_hotel_selected, count, count)
        }
    }

    override fun showMessageHotelDeleted(count: Int) {
        Snackbar.make(listView,getString(R.string.message_hotels_deleted,count),Snackbar.LENGTH_LONG).setAction(R.string.undo){
            presenter.undoDelete()
        }.show()
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val hotel = l?.getItemAtPosition(position) as Hotel
        presenter.selectHotel(hotel)
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
            presenter.showDeleteMode()
            presenter.selectHotel(hotel)
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
            presenter.deleteSelected { hotels->
                if(activity is OnHotelDeletedListener){
                    (activity as OnHotelDeletedListener).onHotelsDeleted(hotels)
                }
            }
            return true
        }
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
        presenter.hideDeleteMode()
    }
    interface OnHotelDeletedListener{
        fun onHotelsDeleted(hotels:List<Hotel>)
    }
}