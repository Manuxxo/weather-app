package mc.tfg_tiempo.card

import mc.tfg_tiempo.api_clima.respuestaWeather
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.Calendar

class ClimaPorHora {

    private val apiKey: String = "95b1f5bbf3915b3c1c96eaf1e6c348e3" // Reemplaza con tu API key de OpenWeatherMap
    private val cliente = OkHttpClient()

    fun getPronosticoPorHoras(city: String, callback: respuestaWeather) {
        val url = "https://api.openweathermap.org/data/2.5/forecast?q=$city&units=metric&appid=$apiKey&lang=es"

        val request = Request.Builder()
            .url(url)
            .build()

        cliente.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(response)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError()
            }
        })
    }

    companion object {
        fun pronosticoParser(json: String): MutableList<DataCard>  {
            val jsonObject = JSONObject(json)
            val jsonArray = jsonObject.getJSONArray("list")

            val weatherList = mutableListOf<DataCard>()

            for (i in 0 until jsonArray.length()) {
                val hourlyObject = jsonArray.getJSONObject(i)
                val timestamp = hourlyObject.getLong("dt")
                val mainObject = hourlyObject.getJSONObject("main")
                val temperatura = mainObject.getDouble("temp")

                val weatherArray = hourlyObject.getJSONArray("weather")
                val arrayDescription = weatherArray.getJSONObject(0)
                val icon = arrayDescription.getString("icon")

                val hour = getHoras(timestamp)

                val weatherItem = DataCard(hour, icon, temperatura.toString())
                weatherList.add(weatherItem)
            }

            return weatherList
        }

        private fun getHoras(timestamp: Long): String {
            val time = timestamp * 1000
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            return String.format("%02d:%02d", hour, minute)
        }
    }
}






