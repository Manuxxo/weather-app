package mc.tfg_tiempo

import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class WeatherApiClient(private val apiKey: String) {

    private val client = OkHttpClient()

    fun getCurrentWeather(city: String, callback: WeatherCallback) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$apiKey"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(response)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError()
            }
        })
    }



    companion object {
        fun parseWeatherData(json: String): WeatherData {
            val jsonObject = JSONObject(json)
            val mainObject = jsonObject.getJSONObject("main")
            val temperature = mainObject.getDouble("temp")
            val humidity = mainObject.getInt("humidity")

            return WeatherData(temperature, humidity)
        }
    }
}

interface WeatherCallback {
    fun onResponse(response: Response)
    fun onError()
}

data class WeatherData(val temperature: Double, val humidity: Int)
