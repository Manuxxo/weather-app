package mc.tfg_tiempo.api_clima

import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ClimaApiCliente {

    private val apiKey: String = "95b1f5bbf3915b3c1c96eaf1e6c348e3"
    private val cliente = OkHttpClient()

    fun getClimaActual(city: String, callback: respuestaWeather) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$apiKey&lang=es"

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
        fun climaParser(json: String): DataWeather {
            val jsonObject = JSONObject(json)

            val mainObject = jsonObject.getJSONObject("main")
            val temperatura = mainObject.getDouble("temp")
            val sensacionTerminca = mainObject.getInt("feels_like")
            val tempMaxima = mainObject.getInt("temp_max")
            val tempMinima = mainObject.getInt("temp_min")
            val humedad = mainObject.getInt("humidity")

            val jsonArray = jsonObject.getJSONArray("weather")
            val arrayDescription = jsonArray.getJSONObject(0)
            val icon = arrayDescription.getString("icon")
            val estado = arrayDescription.getString("description")
            val vientoArray = jsonObject.getJSONObject("wind")
            val viento = vientoArray.getDouble("speed")

            return DataWeather(temperatura, humedad, icon, sensacionTerminca, tempMaxima, tempMinima, viento, estado.capitalize())
        }
    }
}

interface respuestaWeather {
    fun onResponse(response: Response)
    fun onError()
}
