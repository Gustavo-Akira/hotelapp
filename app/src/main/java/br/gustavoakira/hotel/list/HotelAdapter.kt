package br.gustavoakira.hotel.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TextView
import br.gustavoakira.hotel.R
import br.gustavoakira.hotel.databinding.ItemHotelBinding
import br.gustavoakira.hotel.model.Hotel

class HotelAdapter(context: Context, hotels: List<Hotel>): ArrayAdapter<Hotel>(context,0,hotels) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val hotel = getItem(position)
        val viewHolder = if(convertView == null){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hotel,parent,false)
            val holder = ViewHolder(ItemHotelBinding.bind(view))
            view.tag = holder
            holder
        }else{
            convertView.tag as ViewHolder
        }
        viewHolder.txtName.text = hotel?.name
        viewHolder.rtbRatingBar.rating = hotel?.rating ?: 0f
        return viewHolder.view.root
    }
    class ViewHolder(val view: ItemHotelBinding){
        val txtName: TextView = view.txtName
        val rtbRatingBar: RatingBar = view.rtbRating
    }

}