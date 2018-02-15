package com.think360.sotaknights.fragments

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

import com.nightonke.jellytogglebutton.JellyToggleButton
import com.nightonke.jellytogglebutton.State;
import com.think360.sotaknights.R
import com.think360.sotaknights.activities.SotaKnightActivity
import com.think360.sotaknights.api.AppController
import com.think360.sotaknights.api.data.Responce
import com.think360.sotaknights.api.interfaces.ApiService
import com.think360.sotaknights.databinding.MapFragmentBinding
import com.think360.sotaknights.util.ConnectivityReceiver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by think360 on 28/09/17.
 */


class MapFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    @Inject
    internal lateinit var apiService: ApiService
    lateinit var locationRequest: LocationRequest
    private lateinit var mapFragmentBindingm : MapFragmentBinding
    private var mLastLocation: Location? = null
    private var googleMap: GoogleMap? = null
    private var isMapReady = false
    private var mGoogleApiClient: GoogleApiClient? = null
    private var status : Boolean = false
    companion object {

        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mapFragmentBindingm = DataBindingUtil.inflate<MapFragmentBinding>(inflater,R.layout.map_fragment, container, false)
        (activity.application as AppController).getComponent().inject(this@MapFragment)
        status = false
        if(AppController.getSharedPref().getInt("status",7)==1){
            mapFragmentBindingm.jellyToggleButton.setChecked(true)
            mapFragmentBindingm.tvMapSwitch!!.setText("Available")
        }else{
            mapFragmentBindingm.jellyToggleButton.setChecked(false)
            mapFragmentBindingm.tvMapSwitch!!.setText("Disable")
        }



        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()

            locationRequest  = LocationRequest.create()
            locationRequest.setInterval(30*1000 )
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

            // **************************
            builder.setAlwaysShow(true) // this is the key ingredient
            // **************************

            val result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build())
            result.setResultCallback { result ->
               val status = result.status
               /*    val state = result
                           .locationSettingsStates*/
                when (status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> {
                        Log.d("","")
                    }
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        try {
                            Log.d("","")
                            status.startResolutionForResult(activity, 1000)


                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        Log.d("","")

                    }
                }

            }

            mapFragmentBindingm.mapview!!.onCreate(savedInstanceState)
            mapFragmentBindingm.mapview!!.getMapAsync(this)
            mapFragmentBindingm. jellyToggleButton!!.setOnStateChangeListener(mOnStateChangeListener)
      /*    mapFragmentBindingm. jellyToggleButton!!.setOnStateChangeListener( { fl: Float, state: State, jellyToggleButton: JellyToggleButton ->
                when(state){

                State.LEFT ->{
                        mapFragmentBindingm.tvMapSwitch!!.setText("Disable")
                        SotaKnightActivity.sotaKnightActivity!!.tvAvailibility!!.setStrokeWidth(1);
                        SotaKnightActivity.sotaKnightActivity!!.tvAvailibility!!.setStrokeColor("#E60000")
                        SotaKnightActivity.sotaKnightActivity!!.tvAvailibility!!.setSolidColor("#E60000")
                        availableDisableStatus(0)
                    }
                    State.RIGHT ->{
                        mapFragmentBindingm.tvMapSwitch!!.setText("Available")
                        SotaKnightActivity.sotaKnightActivity!!.tvAvailibility!!.setStrokeWidth(1);
                        SotaKnightActivity.sotaKnightActivity!!.tvAvailibility!!.setStrokeColor("#2BAA12")
                        SotaKnightActivity.sotaKnightActivity!!.tvAvailibility!!.setSolidColor("#2BAA12")
                        availableDisableStatus(1)
                    }
                }

            })*/

        }


        return mapFragmentBindingm.root
    }
    val mOnStateChangeListener = JellyToggleButton.OnStateChangeListener { fl: Float, state: State, jellyToggleButton: JellyToggleButton ->

            when (state) {

                State.LEFT -> {
                    mapFragmentBindingm.tvMapSwitch!!.setText("Disable")
                    SotaKnightActivity.sotaKnightActivity!!.tvAvailibility!!.setStrokeWidth(1);
                    SotaKnightActivity.sotaKnightActivity!!.tvAvailibility!!.setStrokeColor("#E60000")
                    SotaKnightActivity.sotaKnightActivity!!.tvAvailibility!!.setSolidColor("#E60000")
                    availableDisableStatus(0)
                }
                State.RIGHT -> {
                    mapFragmentBindingm.tvMapSwitch!!.setText("Available")
                    SotaKnightActivity.sotaKnightActivity!!.tvAvailibility!!.setStrokeWidth(1);
                    SotaKnightActivity.sotaKnightActivity!!.tvAvailibility!!.setStrokeColor("#2BAA12")
                    SotaKnightActivity.sotaKnightActivity!!.tvAvailibility!!.setSolidColor("#2BAA12")
                    availableDisableStatus(1)
                }
                else -> {
                }
            }


    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        this.isMapReady = true

    }

    override fun onResume() {
        super.onResume()

        mapFragmentBindingm.  mapview!!.onResume()
    }

    override fun onStart() {
        mGoogleApiClient!!.connect()
        super.onStart()
        mapFragmentBindingm.mapview!!.onStart()
    }

    override fun onStop() {

        mapFragmentBindingm. mapview!!.onStop()
        super.onStop()

    }

    override fun onPause() {
        super.onPause()
        mapFragmentBindingm. mapview!!.onPause()
    }

    override fun onDestroy() {
        mapFragmentBindingm. mapview!!.onDestroy()

        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapFragmentBindingm.mapview!!.onLowMemory()

    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { return }
        LocationServices.FusedLocationApi.requestLocationUpdates(
              mGoogleApiClient, locationRequest, this)

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        if (mLastLocation != null) {
            if (isMapReady && googleMap != null) {
                AppController.getSharedPref().edit().putString("lat",mLastLocation!!.latitude.toString()).apply()
                AppController.getSharedPref().edit().putString("lang",mLastLocation!!.longitude.toString()).apply()
                googleMap!!.isMyLocationEnabled = true
                mapFragmentBindingm.llMapSwitch.visibility = View.VISIBLE
                cameraZoom(LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude), "")
                if ( mapFragmentBindingm.jellyToggleButton.isChecked) {
                    sendSotaCurrentLocation(mLastLocation!!.latitude.toString(), mLastLocation!!.longitude.toString())
                }
            }
        }


    }

    fun cameraZoom(latLng: LatLng, placeName: String) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18f)
        googleMap!!.moveCamera(cameraUpdate)
    }

    override fun onConnectionSuspended(p0: Int) {}
    override fun onConnectionFailed(p0: ConnectionResult) {}
/*    override fun onMarkerClick(p0: Marker?): Boolean { return true }*/

    private fun sendSotaCurrentLocation(latitude: String, longitude: String) {
        if(ConnectivityReceiver.isConnected()){
            apiService.sendSotaCurrentLocation(AppController.getSharedPref().getString("user_id","null"), latitude, longitude)
                    .enqueue(object : Callback<Responce> {
                        override fun onResponse(call: Call<Responce>, response: Response<Responce>) {
                        }
                        override fun onFailure(call: Call<Responce>, t: Throwable) {
                        }
                    })
        }else{
            Toast.makeText(AppController.getInstance().applicationContext, "No internet connection!", Toast.LENGTH_SHORT).show()
        }

              }
    private fun availableDisableStatus(status : Int) {
        AppController.getSharedPref().edit().putInt("status",status).apply()
        if(ConnectivityReceiver.isConnected()){
            apiService.sotaAvailableStatus(AppController.getSharedPref().getString("user_id","null"), AppController.getSharedPref().getString("lat","null"), AppController.getSharedPref().getString("lang","null"),status.toString())
                    .enqueue(object : Callback<Responce> {
                        override fun onResponse(call: Call<Responce>, response: Response<Responce>) {}
                        override fun onFailure(call: Call<Responce>, t: Throwable) {
                        }
                    })
        }else{
            Toast.makeText(AppController.getInstance().applicationContext, "No internet connection!", Toast.LENGTH_SHORT).show()
        }

    }

    override  fun onLocationChanged(location: Location) {
        AppController.getSharedPref().edit().putString("lat",location.latitude.toString()).apply()
        AppController.getSharedPref().edit().putString("lang",location.longitude.toString()).apply()
        if ( mapFragmentBindingm.jellyToggleButton.isChecked) {
            sendSotaCurrentLocation(location.latitude.toString(), location.longitude.toString())   // Log.d("","ava"  +"   lat  "+ AppController.getSharedPref().getString("lat","")+"   lang   "+AppController.getSharedPref().getString("lang",""))
        }

    }



}





