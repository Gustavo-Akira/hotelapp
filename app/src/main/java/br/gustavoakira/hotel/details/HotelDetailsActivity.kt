package br.gustavoakira.hotel.details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.gustavoakira.hotel.R
import br.gustavoakira.hotel.form.HotelFormFragment
import br.gustavoakira.hotel.model.Hotel

class HotelDetailsActivity: AppCompatActivity(){
    private val hotelId: Long by lazy { intent.getLongExtra(EXTRA_HOTEL_ID, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_details)
        if(savedInstanceState == null){
            showHotelDetails()
        }
    }
    fun open(activity: Activity, hotelId: Long){
        activity.startActivityForResult(
            Intent(activity, HotelDetailsActivity::class.java).apply {
                putExtra(EXTRA_HOTEL_ID, hotelId)
            },0
        )
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