package mc.tfg_tiempo.api_clima

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import mc.tfg_tiempo.R
import mc.tfg_tiempo.card.AdaptadorCard
import mc.tfg_tiempo.card.ClimaPorHora
import mc.tfg_tiempo.card.DataCard
import mc.tfg_tiempo.databinding.FragmentInformacionClimaBinding
import okhttp3.Response
import java.io.IOException
import java.util.Locale


/*
startActivity(
                Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK))
 */
class InformacionClimaFragment : Fragment(), respuestaWeather {

    private val PERMISSION_REQUEST_CODE = 1001

    private lateinit var enlace: FragmentInformacionClimaBinding
    private lateinit var climaApiCliente: ClimaApiCliente
    private lateinit var climaPorHora: ClimaPorHora


    private lateinit var localizacion: FusedLocationProviderClient


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        enlace= FragmentInformacionClimaBinding.inflate(inflater, container, false)
        localizacion = LocationServices.getFusedLocationProviderClient(requireContext())
        val blurredBackground = ContextCompat.getDrawable(requireContext(), R.drawable.fondo_difuminado)
        requireActivity().window.decorView.background = blurredBackground

        return enlace.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.getLocaclizacionActual()

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
                var adapter = AdaptadorCard(weatherList)
                enlace.recViewCard.setHasFixedSize(true)
                enlace.recViewCard.adapter = adapter
            }


        }
    }

    override fun onError() {
        Toast.makeText(this.context,"Error al cargar las horas",Toast.LENGTH_LONG).show()
    }

    private fun getLocaclizacionActual() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //No dio los permisos
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

        localizacion.lastLocation
            .addOnSuccessListener { location ->
                // Se obtuvo la ubicación
                if (location != null) {
                    poneDatos(getNombreCiudadPorCoordenada(location.latitude, location.longitude)
                    )
                }
            }
            .addOnFailureListener { exception ->
                // No se pudo obtener la ubicación
                Toast.makeText(requireContext(),"No se pudo obtener la ubicación", Toast.LENGTH_LONG).show()
            }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso concedido, obtener la ubicación
                getLocaclizacionActual()
            } else {
                // Permiso denegado
                poneDatos("Madrid")
                ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
                Toast.makeText(requireContext(),"Denegado", Toast.LENGTH_LONG).show()
            }
        }



    private fun poneDatos(ciudad:String){
        climaApiCliente = ClimaApiCliente()
        climaPorHora = ClimaPorHora()
        climaPorHora.getPronosticoPorHoras(ciudad,this)

        climaApiCliente.getClimaActual(ciudad, object : respuestaWeather {
            override fun onResponse(response: Response) {

                val body = response.body?.string()
                if (response.isSuccessful && !body.isNullOrEmpty()) {
                    val weatherData = ClimaApiCliente.climaParser(body)
                    val temperatura = weatherData.temperatura.toInt()
                    val humedad = weatherData.sensacionTermica
                    val estado = weatherData.estado

                    //esto es para que trabaje en el mismo hilo y sea más seguro que no crashee

                    view!!.post {
                        enlace.txtTemperatura.text = "$temperatura °"
                        enlace.txtCiudad.text = "$ciudad"
                        enlace.txtEstado.text = estado
                        AdaptadorCard.seleccionImagen(enlace.imageViewIcon, weatherData.icon)

                        enlace.progressBar.visibility = View.GONE // Ocultar el progressbar

                        // Mostrar los elementos nuevamente
                        enlace.txtTemperatura.visibility = View.VISIBLE
                        enlace.txtCiudad.visibility = View.VISIBLE
                        enlace.txtEstado.visibility = View.VISIBLE
                        enlace.imageViewIcon.visibility = View.VISIBLE
                        enlace.recViewCard.visibility = View.VISIBLE
                    }
                } else {
                    onError()
                }

            }

            override fun onError() {
                Toast.makeText(requireContext(),"No se pudo obtener los datos del clima", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getNombreCiudadPorCoordenada(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1) as List<Address>
            if (addresses.isNotEmpty()) {
                val cityName: String = addresses[0].locality
                return cityName
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

}