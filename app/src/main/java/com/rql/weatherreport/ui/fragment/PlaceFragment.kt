package com.rql.weatherreport.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.rql.weatherreport.common.BaseActivity
import com.rql.weatherreport.common.PlaceUtils
import com.rql.weatherreport.databinding.FragmentPlaceBinding
import com.rql.weatherreport.network.dto.Place
import com.rql.weatherreport.ui.activity.MainActivity
import com.rql.weatherreport.ui.activity.WeatherActivity
import com.rql.weatherreport.ui.adapter.PlaceAdapter
import com.rql.weatherreport.ui.viewmodel.PlaceFragmentViewModel

class PlaceFragment: Fragment() {

    var lng:String = ""
    var lat:String = ""
    var placeName:String = ""

    // recleview 的 绑定的shujuyuan
    var placeList = mutableListOf<Place>()
    lateinit var viewBinding: FragmentPlaceBinding
    val viewModel: PlaceFragmentViewModel by lazy {
        ViewModelProvider(this).get(PlaceFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPlaceBinding.inflate(layoutInflater,container,false)
        // 布局管理器
        val layoutManager = LinearLayoutManager(this.activity)
        viewBinding.recyclerView.layoutManager = layoutManager
        viewBinding.recyclerView.adapter = PlaceAdapter(this,placeList)

        return viewBinding.root
    }

    // 碎片的事件 最好卸载 onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 注册事件
        initEvent()
        // 注册观察
        initObserver()
    }

    private fun initObserver() {
        // 观察viewmodel 的地址的集合
        viewModel.palceList.observe(requireActivity()){places ->
            placeList.clear()
            places.forEach { placeList.add(it) }
            viewBinding.recyclerView.adapter?.notifyDataSetChanged()
            viewBinding.recyclerView.visibility = View.VISIBLE
        }
        // 观察 位置信息是否发生改变
        viewModel.locationMessage.observe(requireActivity()){message->
            BaseActivity.showAlterDialog(this.requireActivity(),"注意",message)
        }
        // 观察地址详情是否发生改变
        viewModel.regeocode.observe(requireActivity()){regeocode ->
            // 获得当前碎片所在的activity
            val activity = this.activity
            // 记住用户的最后选择
            val place = Place(name = regeocode.address, location = com.rql.weatherreport.network.dto.Location(lng,lat), address = "")
            PlaceUtils.savePlace(place)
            if(activity is MainActivity){
                WeatherActivity.runActivity(requireActivity(),lng,lat,regeocode.address)
                activity.finish()
            }else{
                val weatherActivity = activity as WeatherActivity
                weatherActivity.lng = lng
                weatherActivity.lat = lat
                weatherActivity.placeName = regeocode.address
                weatherActivity.viewModel.changeLocation(lng,lat)
                weatherActivity.viewBinding.drawerlayout.closeDrawers()
            }
        }
    }

    private fun initEvent() {
        // 字符窜输入改变事件
        viewBinding.searchPlaceEdit.addTextChangedListener { value->
            // 用改变之后的字符传去查
            viewModel.setQuery(value.toString())
        }
        // 点击浮动button 事件
        viewBinding.fab.setOnClickListener {
            // 用户同意权限，重新获取位置
            // getRealTimeLocation()
            getRealTimeLocationWithNativeAPI()
        }
    }

    private lateinit var locationManager: LocationManager

    // 注册一个用于请求定位权限的活动结果处理器
    // 这是AndroidX Activity Result API的用法，用于处理权限请求的回调
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()  // 使用系统提供的权限请求合约
    ) { isGranted ->  // 权限请求的回调结果，isGranted为true表示权限被授予
        if (isGranted) {
            // 权限被授予，调用方法获取实时位置
            getRealTimeLocationWithNativeAPI()
        } else {
            // 权限被拒绝，显示提示信息
            // Toast.makeText(requireActivity(), "定位权限被拒绝", Toast.LENGTH_SHORT).show()
            viewModel.changeLocationMessage("需要定位权限才能获取实时位置")
        }
    }

    private fun getRealTimeLocationWithNativeAPI() {
        // 检查是否拥有精确位置权限
        // ACCESS_FINE_LOCATION允许应用获取精确的位置信息
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),  // 获取当前活动的上下文
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED  // 权限未被授予
        ) {
            // 权限未授予，发起权限请求
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return  // 退出方法，等待权限请求结果
        }

        // 初始化位置管理器，通过系统服务获取LocationManager实例
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // 创建位置监听器，用于处理位置变化事件
        val locationListener = object : LocationListener {
            // 当位置发生变化时调用
            override fun onLocationChanged(location: Location) {
                // 处理新的位置信息
                handleLocationResult(location)
                // 移除监听器，只获取一次位置后停止监听
                //locationManager.removeUpdates(this)
            }

            // 当定位提供者被禁用时调用
            override fun onProviderDisabled(provider: String) {
                Toast.makeText(requireActivity(), "定位服务已关闭", Toast.LENGTH_SHORT).show()
            }

            // 当定位提供者被启用时调用
            override fun onProviderEnabled(provider: String) {}

            // 当定位提供者状态变化时调用
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        // 请求位置更新
        try {
            // 优先使用GPS定位，如果可用的话
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,  // 最小时间间隔：5秒，超过此时长才可能触发更新
                    10f,   // 最小距离：10米，移动超过此距离才可能触发更新
                    locationListener  // 位置监听器
                )
            } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                // 如果GPS不可用，使用网络定位作为备选
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,  // 最小时间间隔：5秒
                    10f,   // 最小距离：10米
                    locationListener  // 位置监听器
                )
            }

            // 获取最后已知的位置信息，作为快速响应（可能不是最新的）
            val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)  // GPS获取失败则尝试网络

            // 如果存在最后已知位置，处理该位置信息
            lastLocation?.let {
                handleLocationResult(lastLocation)
            }

        } catch (e: SecurityException) {
            // 捕获权限异常，理论上不会走到这里，因为前面已经做了权限检查
            Toast.makeText(requireActivity(), "定位权限异常", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLocationResult(location: Location) {

        // 从Location对象中获取纬度
        val latitude = location.latitude
        // 从Location对象中获取经度
        val longitude = location.longitude
        // 拿精度和维度 去反查地址
        lng = longitude.toString()
        lat = latitude.toString()
        //
        viewModel.changeLocation(lng,lat)


    }
}
