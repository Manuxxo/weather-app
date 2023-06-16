package mc.tfg_tiempo.map

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import mc.tfg_tiempo.R
import mc.tfg_tiempo.databinding.FragmentMapsBinding

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map:GoogleMap
    private lateinit var enlace: FragmentMapsBinding
    private var markerAnterior: Marker? = null

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
            null
        )
        markerAnterior = marker
    }

}