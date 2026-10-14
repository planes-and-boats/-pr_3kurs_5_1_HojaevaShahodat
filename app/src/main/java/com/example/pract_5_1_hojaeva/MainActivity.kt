package com.example.pract_5_1_hojaeva

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import org.json.JSONObject

private val apiKey = "534e4c43bba8aba7611e9ce9cf5af1cc"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etCity = findViewById<EditText>(R.id.etCity)
        val btnGetWeather = findViewById<Button>(R.id.btnGetWeather)
        val tvCity = findViewById<TextView>(R.id.tvCityName)
        val tvTemp = findViewById<TextView>(R.id.tvTemp)
        val tvPressure = findViewById<TextView>(R.id.tvPressure)
        val tvWind = findViewById<TextView>(R.id.tvWind)

        btnGetWeather.setOnClickListener {
            val city = etCity.text.toString().trim()
            if (city.isNotEmpty()) {

                val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric&lang=ru"

                val queue = Volley.newRequestQueue(this)

                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    { response ->
                        Log.d("WeatherApp", "Ответ от сервера: $response")

                        try {
                            val mainObject = JSONObject(response)

                            val cityName = mainObject.getString("name")

                            val mainData = mainObject.getJSONObject("main")
                            val temp = mainData.getString("temp")
                            val pressure = mainData.getString("pressure")

                            val windData = mainObject.getJSONObject("wind")
                            val windSpeed = windData.getString("speed")

                            tvCity.text = getString(R.string.weather_city, cityName)
                            tvTemp.text = getString(R.string.weather_temp, temp)
                            tvPressure.text = getString(R.string.weather_pressure, pressure)
                            tvWind.text = getString(R.string.weather_wind, windSpeed)

                        } catch (e: Exception) {
                            Log.d("WeatherApp", "Ошибка внутри try-catch: ", e)
                            Toast.makeText(this, getString(R.string.error_parse_json), Toast.LENGTH_LONG).show()
                        }
                    },
                    { error ->
                        Toast.makeText(this, getString(R.string.error_network_or_key), Toast.LENGTH_LONG).show()
                    }
                )
                queue.add(stringRequest)
            } else {
                Toast.makeText(this, getString(R.string.error_empty_field), Toast.LENGTH_SHORT).show()
            }
        }

    }
}