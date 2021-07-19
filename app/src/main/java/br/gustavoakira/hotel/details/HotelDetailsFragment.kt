package br.gustavoakira.hotel.details

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.gustavoakira.hotel.R
import br.gustavoakira.hotel.databinding.FragmentHotelDetailsBinding
import br.gustavoakira.hotel.form.HotelFormFragment
import br.gustavoakira.hotel.model.Hotel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HotelDetailsFragment: Fragment(){
    private val hotelDetailsModelView: HotelDetailsModelView by viewModel()
    private var hotel: Hotel? = null
    private var _binding: FragmentHotelDetailsBinding? = null
    private var shareActionProvider: ShareActionProvider? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHotelDetailsBinding.inflate(inflater, container, false)
        val id = arguments?.getLong(EXTRA_HOTEL_ID,-1)?:-1
        hotelDetailsModelView.loadHotelDetails(id).observe(viewLifecycleOwner, Observer { hotel->
            if(hotel != null){
                showHotelDetails(hotel)
            }else{
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.remove(this)
                    ?.commit()
                errorHotelNotFound()
            }
        })
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun showHotelDetails(hotel: Hotel) {
        this.hotel = hotel
        binding.txtName.text = hotel.name
        binding.txtAddress.text = hotel.address
        binding.rtbRating.rating = hotel.rating
    }

    private fun errorHotelNotFound() {
        binding.txtName.text = getString(R.string.error_not_found_hotel)
        binding.txtAddress.visibility = View.GONE
        binding.rtbRating.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.hotel_details,menu)
        val shareItem = menu.findItem(R.id.action_share)
        shareActionProvider = MenuItemCompat.getActionProvider(shareItem) as? ShareActionProvider
        setShareIntent()
    }

    private fun setShareIntent(){
        val text = getString(R.string.shared_text, hotel?.name, hotel?.rating)
        shareActionProvider?.setShareIntent(Intent(Intent.ACTION_SEND).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            type="text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_edit){
            HotelFormFragment.newInstance(hotel?.id?:0).open(parentFragmentManager)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object{
        const val EXTRA_HOTEL_ID = "hotelId"
        const val TAG_DETAILS = "tagDetalhe"

        fun newInstance(id: Long) = HotelDetailsFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_HOTEL_ID,id)
            }
        }
    }

}