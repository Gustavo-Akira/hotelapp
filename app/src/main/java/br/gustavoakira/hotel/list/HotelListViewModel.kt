package br.gustavoakira.hotel.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import br.gustavoakira.hotel.common.SingleLiveEvent
import br.gustavoakira.hotel.model.Hotel
import br.gustavoakira.hotel.repository.interfaces.HotelRepository

class HotelListViewModel(
    private val repository: HotelRepository
) : ViewModel(){
    var hotelIdSelected: Long = -1
    private val searchTerm = MutableLiveData<String>()
    private val hotels = Transformations.switchMap(searchTerm){
        term->repository.search(term)
    }
    private val inDeleteMode = MutableLiveData<Boolean>().apply {
        value=false
    }
    private val selectedItems = mutableListOf<Hotel>()
    private val selectionCount = MutableLiveData<Int>()
    private val selectedHotels = MutableLiveData<List<Hotel>>().apply {
        value = selectedItems
    }
    private val deletedItems = mutableListOf<Hotel>()
    private val showDeletedMessage = SingleLiveEvent<Int>()
    private val showDetailsCommand = SingleLiveEvent<Hotel>()

    fun isDeleteMode(): LiveData<Boolean> = inDeleteMode

    fun getHotels(): LiveData<List<Hotel>>? = hotels

    fun selectionCount(): LiveData<Int> = selectionCount

    fun selectedHotels(): MutableLiveData<List<Hotel>> = selectedHotels

    fun showDeletedMessage(): LiveData<Int> = showDeletedMessage

    fun showDetailsCommand(): LiveData<Hotel> = showDetailsCommand

    fun selectedHotel(hotel: Hotel){
        if(inDeleteMode.value == true){
            toggleHotelSelected(hotel)
            if(selectedItems.size == 0){
                inDeleteMode.value = false
            }else{
                selectionCount.value = selectedItems.size
                selectedHotels.value = selectedItems
            }
        }else{
            showDetailsCommand.setValue(hotel)
        }
    }

    fun search(term: String = ""){
        searchTerm.value = term
    }

    fun setInDeleteMode(deleteMode: Boolean){
        if(!deleteMode){
            selectionCount.value = 0
            selectedItems.clear()
            selectedHotels.value = selectedItems
            showDeletedMessage.setValue(selectedItems.size)
        }
        inDeleteMode.value = deleteMode
    }

    fun deleteSelected(){
        repository.remove(*selectedItems.toTypedArray())
        deletedItems.clear()
        deletedItems.addAll(selectedItems)
        setInDeleteMode(false)
        showDeletedMessage.setValue(deletedItems.size)
    }

    fun undoDelete(){
        if(deletedItems.isNotEmpty()){
            for(hotel in deletedItems){
                hotel.id = 0L
                repository.save(hotel)
            }
        }
    }

    private fun toggleHotelSelected(hotel: Hotel){
        val existing = selectedItems.find { it.id == hotel.id }
        if(existing == null){
            selectedItems.add(hotel)
        }else{
            selectedItems.removeAll { it.id == hotel.id }
        }
    }

    fun getSearchTerm(): LiveData<String> = searchTerm
}