package mc.tfg_tiempo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mc.tfg_tiempo.databinding.FragmentInformacionClimaBinding
import okhttp3.Response

class InformacionClimaFragment : Fragment() {

    lateinit var enlace: FragmentInformacionClimaBinding

    private lateinit var climaApiCliente: ClimaApiCliente


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        enlace= FragmentInformacionClimaBinding.inflate(inflater, container, false)
        return enlace.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        climaApiCliente = ClimaApiCliente()

        enlace.buttonGetWeather.setOnClickListener {
            val city = "El Puerto de Santa María"
            climaApiCliente.getClimaActual(city, object : respuestaWeather {
                override fun onResponse(response: Response) {

                    val body = response.body?.string()
                    if (response.isSuccessful && !body.isNullOrEmpty()) {
                        val weatherData = ClimaApiCliente.parseWeatherData(body)
                        val temperature = weatherData.temperatura.toInt()
                        val humidity = weatherData.humedad

                        view.post {
                            enlace.textViewTemperature.text = "Temperatura: $temperature °C"
                            enlace.textViewHumidity.text = "Humedad: $humidity %"
                        }
                    } else {
                        onError()
                    }
                }

                override fun onError() {
                    view.post {
                        enlace.textViewTemperature.text = "Error al obtener el clima"
                        enlace.textViewHumidity.text = "Error al obtener los datos"
                    }
                }
            })
        }
    }
}