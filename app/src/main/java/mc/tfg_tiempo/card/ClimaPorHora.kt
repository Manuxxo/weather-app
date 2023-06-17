package mc.tfg_tiempo.card

import com.google.android.gms.maps.model.LatLng
import mc.tfg_tiempo.interfaces.RespuestaWeather
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.Calendar

class ClimaPorHora {

    private val apiKey: String = "API_KEY_WEATHERMAP"
    private val cliente = OkHttpClient()

    fun getPronosticoPorHoras(latLongitud: LatLng, callback: RespuestaWeather) {
        val url = "https://api.openweathermap.org/data/2.5/forecast?lat=${latLongitud.latitude}&lon=${latLongitud.longitude}&units=metric&appid=$apiKey&lang=es"

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

                val weatherItem = DataCard(hour, icon, temperatura)
                weatherList.add(weatherItem)
            }

            return weatherList
        }

        private fun getHoras(timestamp: Long): String {
            val tiempo = timestamp * 1000
            val calendario = Calendar.getInstance()
            calendario.timeInMillis = tiempo
            val hora = calendario.get(Calendar.HOUR_OF_DAY)
            val minuto = calendario.get(Calendar.MINUTE)
            return String.format("%02d:%02d", hora, minuto)
        }
    }
}






