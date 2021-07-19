package br.gustavoakira.hotel.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import br.gustavoakira.hotel.R
import br.gustavoakira.hotel.databinding.FragmentHotelFormBinding
import br.gustavoakira.hotel.model.Hotel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception

class HotelFormFragment: DialogFragment(){
    private val hotelFormModelView: HotelFormModelView by viewModel()
    private var hotel:Hotel? = null
    private var _binding: FragmentHotelFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHotelFormBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hotelId = arguments?.getLong(EXTRA_HOTEL_ID,0) ?: 0
        if(hotelId > 0){
            hotelFormModelView.loadHotel(hotelId).observe(viewLifecycleOwner, Observer { hotel ->
                this.hotel = hotel
                showHotel(hotel)
            })
        }
        binding.edtAddress.setOnEditorActionListener { _, actionId, _ -> handleKeyboardEvent(actionId) }
        dialog?.setTitle(R.string.action_new_hotel)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    private fun showHotel(hotel: Hotel) {
        binding.edtName.setText(hotel.name)
        binding.edtAddress.setText(hotel.address)
        binding.rtbRating.rating = hotel.rating
    }

    private fun errorInvalidHotel() {
        Toast.makeText(requireContext(),R.string.error_invalid_hotel, Toast.LENGTH_SHORT).show()
    }

    private fun errorSaveHotel() {
        Toast.makeText(requireContext(),R.string.error_not_found_hotel,Toast.LENGTH_SHORT).show()
    }

    private fun handleKeyboardEvent(actionId: Int): Boolean{
       if (EditorInfo.IME_ACTION_DONE == actionId){
           saveHotel()
           return true
       }
        return false
    }

    private fun saveHotel(){
        val hotel = Hotel()
        val hotelId = arguments?.getLong(EXTRA_HOTEL_ID,0) ?: 0
        hotel.id = hotelId
        hotel.name = binding.edtName.text.toString()
        hotel.address = binding.edtAddress.text.toString()
        hotel.rating = binding.rtbRating.rating
        try{
            if(hotelFormModelView.saveHotel(hotel)){
                dialog?.dismiss()
            }else{
                errorInvalidHotel()
            }
        }catch (e: Exception){
            errorSaveHotel()
        }
    }

    fun open(fm: FragmentManager){
        if(fm.findFragmentByTag(DIALOG_TAG) == null){
            show(fm, DIALOG_TAG)
        }
    }
    companion object{
        private const val DIALOG_TAG = "editDialog"
        private const val EXTRA_HOTEL_ID= "hotel_id"

        fun newInstance(hotelId: Long = 0) = HotelFormFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_HOTEL_ID, hotelId)
            }
        }
    }
}