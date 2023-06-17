package mc.tfg_tiempo.interfaces

import okhttp3.Response

interface RespuestaWeather {
    fun onResponse(response: Response)
    fun onError()
}