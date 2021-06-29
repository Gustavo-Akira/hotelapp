package br.gustavoakira.hotel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import br.gustavoakira.hotel.view.HotelDetailsFragment

class HotelDetailsActivity: AppCompatActivity(){
    private val hotelId: Long by lazy { intent.getLongExtra(EXTRA_HOTEL_ID, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_details)
        if(savedInstanceState == null){
            showHotelDetails()
        }
    }

    private fun showHotelDetails(){
        val fragment = HotelDetailsFragment.newInstance(hotelId)
        supportFragmentManager
            .beginTransaction()
            .apply {
                replace(R.id.details,fragment)
                addToBackStack(null)
                commit()
            }
    }

    companion object{
        private const val EXTRA_HOTEL_ID = "hotel_id"
        fun open(context:Context, hotelId: Long){
            context.startActivity(Intent(context, HotelDetailsActivity::class.java).apply {
                putExtra(EXTRA_HOTEL_ID, hotelId)
            })
        }
    }

}