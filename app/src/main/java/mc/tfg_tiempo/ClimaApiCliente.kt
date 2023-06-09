package mc.tfg_tiempo

import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ClimaApiCliente(private val apiKey: String) {

    private val cliente = OkHttpClient()

    fun getClimaActual(city: String, callback: respuestaWeather) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$apiKey"

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
        fun parseWeatherData(json: String): DatosWeather {
            val jsonObject = JSONObject(json)
            val mainObject = jsonObject.getJSONObject("main")
            val temperatura = mainObject.getDouble("temp")
            val humedad = mainObject.getInt("humidity")

            val icon = "04d"

            return DatosWeather(temperatura, humedad, icon)
        }
    }
}

interface respuestaWeather {
    fun onResponse(response: Response)
    fun onError()
}

data class DatosWeather(val temperatura: Double, val humedad: Int, val icon:String)
