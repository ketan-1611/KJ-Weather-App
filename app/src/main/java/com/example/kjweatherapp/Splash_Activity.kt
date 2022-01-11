package com.example.kjweatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import java.security.Permission
import android.location.LocationRequest as LocationRequest1

class Splash_Activity : AppCompatActivity() {

      lateinit var mfusedlocation:FusedLocationProviderClient
      private var myRequestCode = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        mfusedlocation = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()


    }

    // location permission -> denied
    // location permission denied through setting
    // gps off
    //permission lena

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (CheckPermission()){
            if (LocationEnable()){
                mfusedlocation.lastLocation.addOnCompleteListener {
                    task->
                    var location:Location?=task.result

                    if (location==null) {
                        NewLoation()
                    }
                    else {
                        Log.i("Location",location.longitude.toString())
                        Handler(Looper.getMainLooper()).postDelayed({
                            var intent = Intent(this,MainActivity::class.java)
                            intent.putExtra("lat",location.latitude.toString())
                            intent.putExtra("long",location.longitude.toString())
                            startActivity(intent)
                            finish()
                        },2000)
                    }

                }
            }
            else{
                Toast.makeText(this,"Please turn on your Gps Location",Toast.LENGTH_SHORT).show()
            }
        }else
        {
            RequestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun NewLoation() {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        mfusedlocation = LocationServices.getFusedLocationProviderClient(this)
        mfusedlocation.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())


    }

    private val locationCallback= object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation: Location = p0.lastLocation
        }
    }

    private fun LocationEnable(): Boolean {
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
     }

    private fun RequestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION),myRequestCode)
    }

    private fun CheckPermission(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)== PERMISSION_GRANTED
        ){
            return true
        }

        return false
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==myRequestCode)
        {
            if (grantResults.isNotEmpty() && grantResults[0]== PERMISSION_GRANTED)
            {
                getLastLocation()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }







}