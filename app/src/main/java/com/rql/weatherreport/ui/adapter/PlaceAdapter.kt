package com.rql.weatherreport.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rql.weatherreport.R
import com.rql.weatherreport.common.PlaceUtils
import com.rql.weatherreport.network.dto.Location
import com.rql.weatherreport.network.dto.Place
import com.rql.weatherreport.ui.activity.MainActivity
import com.rql.weatherreport.ui.activity.WeatherActivity
import com.rql.weatherreport.ui.fragment.PlaceFragment

class PlaceAdapter(val placeFragment: PlaceFragment,val placeList: List<Place>)
    : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    // 为了保持每一条上的控件
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        val holder = ViewHolder(view)
        view.setOnClickListener {
            // 判断目前的碎片 是在哪一个Activity 上
            // 1 获得碎片目前所在的Acxtivity

            val index = holder.absoluteAdapterPosition
            val place = placeList[index]
            val lng = place.location.lng
            val lat = place.location.lat
            val placeName = place.name

            val activity = placeFragment.activity
            if(activity is MainActivity){
                WeatherActivity.runActivity(placeFragment.requireActivity(), lng = lng,lat=lat,placeName=placeName)
                placeFragment.activity?.finish()
            }else{
                val weatherActivity = activity as WeatherActivity
                weatherActivity.lng = lng
                weatherActivity.lat = lat
                weatherActivity.placeName = placeName
                // 让 weatherActivity 重新差一次
                weatherActivity.viewModel.changeLocation(lng,lat)
                // weatherActivity关闭侧滑菜单
                weatherActivity.viewBinding.drawerlayout.closeDrawers()
            }
            PlaceUtils.savePlace(Place(name=placeName, location = Location(lng,lat), address = placeName))


        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount() = placeList.size

}