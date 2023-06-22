package mc.tfg_tiempo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.libraries.places.api.Places
import mc.tfg_tiempo.api_clima.InformacionClimaFragment
import mc.tfg_tiempo.databinding.ActivityMainBinding
import mc.tfg_tiempo.interfaces.PasaDataFragment
import mc.tfg_tiempo.map.MapsFragment


class MainActivity : AppCompatActivity(), PasaDataFragment {

    val enlace: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(applicationContext, "API_KEY_MAPS_AQUI")
        setContentView(enlace.root)

        enlace.bottomNavigation.setOnItemSelectedListener{
            if (it.itemId == R.id.mapBottom ){
                cambioFragment(MapsFragment())
                enlace.bottomNavigation.menu.findItem(R.id.mapBottom).isEnabled = false
                enlace.bottomNavigation.menu.findItem(R.id.inicioBottom).isEnabled = true
            } else if (it.itemId == R.id.inicioBottom){
                cambioFragment(InformacionClimaFragment())
                enlace.bottomNavigation.menu.findItem(R.id.mapBottom).isEnabled = true
                enlace.bottomNavigation.menu.findItem(R.id.inicioBottom).isEnabled = false
            } else {
                Toast.makeText(this,enlace.containerNavegacion.id.toString() , Toast.LENGTH_LONG).show()
            }
            true
        }
    }

    private fun cambioFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
        .replace(enlace.containerNavegacion.id,fragment)
        .commit()
    }

    override fun pasaDato(latitud: Double, longitud: Double) {
        val infoFragment = InformacionClimaFragment()
        val bundle = Bundle()
        bundle.putDouble("latitud",latitud)
        bundle.putDouble("longitud",longitud)
        infoFragment.arguments = bundle
        cambioFragment(infoFragment)
        enlace.bottomNavigation.menu.findItem(R.id.mapBottom).isEnabled = true
        enlace.bottomNavigation.menu.findItem(R.id.inicioBottom).isEnabled = false
    }
}
