package mc.tfg_tiempo.api_clima

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import mc.tfg_tiempo.card.AdaptadorCard
import mc.tfg_tiempo.card.ClimaPorHora
import mc.tfg_tiempo.card.DataCard
import mc.tfg_tiempo.databinding.FragmentInformacionClimaBinding
import okhttp3.Response

class InformacionClimaFragment : Fragment(), respuestaWeather {

    lateinit var enlace: FragmentInformacionClimaBinding

    private lateinit var climaApiCliente: ClimaApiCliente
    private lateinit var climaPorHora: ClimaPorHora



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        enlace= FragmentInformacionClimaBinding.inflate(inflater, container, false)
        return enlace.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        climaApiCliente = ClimaApiCliente()
        climaPorHora = ClimaPorHora()

        climaPorHora.getPronosticoPorHoras("Sevilla",this)

        enlace.btnGetData.setOnClickListener {
            val city = "Sevilla"
            climaApiCliente.getClimaActual(city, object : respuestaWeather {
                override fun onResponse(response: Response) {

                    val body = response.body?.string()
                    if (response.isSuccessful && !body.isNullOrEmpty()) {
                        val weatherData = ClimaApiCliente.climaParser(body)
                        val temperatura = weatherData.temperatura.toInt()
                        val humedad = weatherData.sensacionTermica
                        val icon = weatherData.icon
                        val estado = weatherData.estado

                        val iconUrl = "http://openweathermap.org/img/w/$icon.png"

                        //esto es para que trabaje en un hilo diferente y sea más seguro que no crashee

                        view.post {
                            enlace.txtTemperatura.text = "$temperatura °"
                            enlace.txtCiudad.text = "Humedad: $humedad %"
                            enlace.txtEstado.text = estado

                            //Picasso agarra un resource desde una web y lo pone en un elemento
                            Picasso.get()
                                .load(iconUrl)
                                .into(enlace.imageViewIcon)
                        }
                    } else {
                        onError()
                    }
                }

                override fun onError() {
                    view.post {
                        enlace.txtTemperatura.text = "Error al obtener el clima"
                        enlace.txtCiudad.text = "Error al obtener los datos"
                    }
                }
            })



        }
    }

    override fun onResponse(response: Response) {
        if (response.isSuccessful) {
            var weatherList : MutableList<DataCard> = ArrayList()
            val responseData = response.body?.string()
            responseData?.let {
                weatherList = ClimaPorHora.pronosticoParser(it)
            }

            val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            enlace.recViewCard.post{
                enlace.recViewCard.layoutManager = layoutManager

            }
            var adapter = AdaptadorCard(weatherList)
            enlace.recViewCard.setHasFixedSize(true)
            enlace.recViewCard.adapter = adapter
            println("Adapter" + adapter)
            println("WeatherList" + weatherList)

        }
    }

    override fun onError() {
    }
}