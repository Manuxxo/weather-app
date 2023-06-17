package mc.tfg_tiempo.api_clima

import com.google.android.gms.maps.model.LatLng
import mc.tfg_tiempo.interfaces.RespuestaWeather
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ClimaApiCliente {

    private val apiKey: String = "API_KEY_WEATHERMAP"
    private val cliente = OkHttpClient()

    fun getClimaActualCoordenada(latLong: LatLng, callback: RespuestaWeather) {
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${latLong.latitude}&lon=${latLong.longitude}&units=metric&appid=$apiKey&lang=es"

        // Crea una solicitud HTTP utilizando la URL proporcionada
        val request = Request.Builder()
            .url(url)
            .build()

        // Realiza la llamada asíncrona a la API y maneja las respuestas y errores utilizando el callback po rla intefaz
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
        // Analiza la respuesta JSON de la API y devuelve un objeto DataWeather
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
