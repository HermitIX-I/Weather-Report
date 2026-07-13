package com.rql.weatherreport.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.rql.weatherreport.network.dto.Location
import com.rql.weatherreport.network.dto.Place
import com.rql.weatherreport.network.dto.Regeocode
import com.rql.weatherreport.network.repository.CaiyunRepository
import com.rql.weatherreport.network.repository.GaodeRepository

class PlaceFragmentViewModel: ViewModel() {

    private val caiyunRepository = CaiyunRepository()

    private val gaodeRepository = GaodeRepository()

    private val _query = MutableLiveData<String>()
    val palceList: LiveData<List<Place>> = _query.switchMap { value->
        val list = MutableLiveData<List<Place>>()
        // 通过关键字去彩云上去查
        caiyunRepository.searchPlaces(value.toString()){response ->
            if(response.status=="ok"){
                list.postValue(response.places)
            }
        }
        list
    }
    fun setQuery(query:String){
        _query.postValue(query)
    }

    private var _locationMessage = MutableLiveData<String>()
    val locationMessage: LiveData<String>
        get() =  _locationMessage
    fun changeLocationMessage(msg:String){
        _locationMessage.postValue(msg)
    }

    private var _location =  MutableLiveData<Location>()
    val regeocode: LiveData<Regeocode> = _location.switchMap { location ->
        var result = MutableLiveData<Regeocode>()
        // 根据经纬反查地址信息
        gaodeRepository.getAddressByLocation(location.lng,location.lat){response ->
            if(response.info=="OK"){
                result.postValue(response.regeocode)
            }
        }
        result
    }
    fun changeLocation(lng: String,lat:String){
        _location.postValue(Location(lng,lat))
    }

}