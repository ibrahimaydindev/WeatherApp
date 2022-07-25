package com.example.weatherapp

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.ActivityMainBinding
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        weatherTask().execute()
    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {
        private val city: String = "ankara,tr"
        private val api: String = "789a790859ae3d020479a223dc3eb53d"

        override fun onPreExecute() {
            super.onPreExecute()
            binding.loader.visibility = View.VISIBLE
            binding.mainContainer.visibility = View.GONE
            binding.errorText.visibility = View.GONE


        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String? = try {
                URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$api")
                    .readText(Charsets.UTF_8)
            } catch (e: Exception) {
                null
            }
            return response

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updateAt: Long = jsonObj.getLong("dt")
                val updatedAtText =
                    "updated at :" + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updateAt * 1000)
                    )
                val temp = main.getString("temp") + "°C"
                val tempMin = "Min Temp:" + main.getString("tempMin") + "°C"
                val tempMax = "Max Temp:" + main.getString("tempMax") + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name") + "," + sys.getString("country")

                binding.address.text = address
                binding.updatedAt.text = updatedAtText
                binding.status.text = weatherDescription.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                binding.temp.text = temp
                binding.tempMin.text = tempMin
                binding.tempMax.text = tempMax
                binding.sunrise.text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                binding.sunset.text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
                binding.wind.text = windSpeed
                binding.pressure.text = pressure
                binding.humidity.text = humidity

                binding.loader.visibility = View.GONE
                binding.mainContainer.visibility = View.VISIBLE

            } catch (e: Exception) {
                binding.loader.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE

            }
        }

    }


}