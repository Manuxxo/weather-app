package mc.tfg_tiempo.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.HandlerCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import mc.tfg_tiempo.R
import mc.tfg_tiempo.databinding.FragmentMapsBinding
import java.util.concurrent.TimeUnit

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map:GoogleMap
    private lateinit var enlace: FragmentMapsBinding
    private var markerAnterior: Marker? = null

    companion object{
        private const val PERMISSION_REQUEST_CODE = 1001
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        enlace = FragmentMapsBinding.inflate(inflater, container, false)
        creaMapa()
        return enlace.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMapClickListener {
            var coordinadas = LatLng(it.latitude,it.longitude)
            createMarker(coordinadas)
        }
        activaLocation()
    }

    private fun creaMapa(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun createMarker(coordinadas: LatLng){
        markerAnterior?.remove()
        val marker = map.addMarker(MarkerOptions().position(coordinadas))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinadas, 18f),
            4000,
            object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    // La animación de la cámara termina
                    val handler = HandlerCompat.createAsync(requireActivity().mainLooper)
                    handler.postDelayed({
                        showConfirmationDialog()
                    }, TimeUnit.MILLISECONDS.toMillis(1000))                }

                override fun onCancel() {
                    // La animación de la cámara ha sido cancelada
                    val handler = HandlerCompat.createAsync(requireActivity().mainLooper)
                    handler.postDelayed({
                        showConfirmationDialog()
                    }, TimeUnit.MILLISECONDS.toMillis(1000))

                }
            }
        )
        markerAnterior = marker
    }

    private fun compruebaPermisoLocation(): Boolean{
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun activaLocation(){
        if (!::map.isInitialized) return
        if (compruebaPermisoLocation()){
            map.isMyLocationEnabled = true
        } else{
            pidePermiso()
        }
    }

    private fun pidePermiso(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(requireContext(), "Acepta los permisos en ajustes", Toast.LENGTH_LONG).show()
        } else{
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
            } else{
                Toast.makeText(requireContext(), "Acepta los permisos en ajustes2", Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())

        // Configurar el título y el mensaje del diálogo
        builder.setTitle("Confirmación")
            .setMessage("¿Quieres elegir esta ubicación?")

        // Configurar el botón positivo y su acción
        builder.setPositiveButton("Sí") { dialog, which ->
            // Acción a realizar si se selecciona "Sí"
            // Puedes agregar tu lógica aquí
        }

        // Configurar el botón negativo y su acción
        builder.setNegativeButton("No") { dialog, which ->

        }

        // Mostrar el diálogo
        builder.create().show()
    }

}