package com.example.kjweatherapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Math.ceil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lat = intent.getStringExtra("lat")
        val long = intent.getStringExtra("long")

        supportActionBar?.hide()
        window.statusBarColor = Color.parseColor("#1383C3")

        getJsonData(lat,long)
    }

    private fun getJsonData(lat: String?, long: String?) {

        val API_KEY = "877062013b9e79518c0308aa7996ffcd"

        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}"


        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { response ->

                setValues(response)

            },
            Response.ErrorListener { Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show() })

        queue.add(jsonRequest)
    }

    private fun setValues(response: JSONObject) {

        cityName.text = response.getString("name")

        var lat = response.getJSONObject("coord").getString("lat")
        var long = response.getJSONObject("coord").getString("lon")
        coordinates.text = "${lat},${long}"

        weather.text = response.getJSONArray("weather").getJSONObject(0).getString("main")

        var tempr = response.getJSONObject("main").getString("temp")
        tempr = (((tempr).toFloat() - 273.15)).toInt().toString() + "°C"
        temp.text = tempr

        var mintemp = response.getJSONObject("main").getString("temp_min")
        mintemp = (((mintemp).toFloat() - 273.15)).toInt().toString() + "°C"
        minTemp.text = mintemp

        var maxtemp = response.getJSONObject("main").getString("temp_max")
        maxtemp = (((maxtemp).toFloat() - 273.15)).toInt().toString() + "°C"
        maxTemp.text = maxtemp

        pressure.text = response.getJSONObject("main").getString("pressure")

        humadity.text = response.getJSONObject("main").getString("humidity")

        wind.text = response.getJSONObject("wind").getString("speed")
        degree.text = "Degree : " + response.getJSONObject("wind").getString("deg")+"°"
        //gust.text = "Gust : " + response.getJSONObject("wind").getString("gust")




    }


}