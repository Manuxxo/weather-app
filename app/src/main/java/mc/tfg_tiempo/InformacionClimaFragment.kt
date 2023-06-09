package mc.tfg_tiempo

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import mc.tfg_tiempo.databinding.FragmentInformacionClimaBinding
import okhttp3.Response
import java.lang.Exception

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
        climaApiCliente = ClimaApiCliente("95b1f5bbf3915b3c1c96eaf1e6c348e3")

        enlace.buttonGetWeather.setOnClickListener {
            val city = "El Puerto de Santa María"
            climaApiCliente.getClimaActual(city, object : respuestaWeather {
                override fun onResponse(response: Response) {

                    val body = response.body?.string()
                    if (response.isSuccessful && !body.isNullOrEmpty()) {
                        val weatherData = ClimaApiCliente.parseWeatherData(body)
                        val temperatura = weatherData.temperatura.toInt()
                        val humedad = weatherData.humedad
                        val icon = weatherData.icon

                        val iconUrl = "http://openweathermap.org/img/w/$icon.png"

                        //esto es para que trabaje en un hilo diferente y sea más seguro que no crashe

                        view.post {
                            enlace.textViewTemperature.text = "Temperatura: $temperatura °C"
                            enlace.textViewHumidity.text = "Humedad: $humedad %"

                            //Picasso agarra un resource desde una web y lo pone en un elemento
                            Picasso.get()
                                .load(iconUrl)
                                .into(object : Target{
                                    override fun onBitmapLoaded(
                                        bitmap: Bitmap?,
                                        from: Picasso.LoadedFrom?
                                    ) {
                                        enlace.imageViewIcon.setImageBitmap(bitmap)
                                    }

                                    override fun onBitmapFailed(
                                        e: Exception?,
                                        errorDrawable: Drawable?
                                    ) {

                                    }

                                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                                    }

                                })
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