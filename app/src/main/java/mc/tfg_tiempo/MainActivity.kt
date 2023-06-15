package mc.tfg_tiempo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarMenu
import mc.tfg_tiempo.api_clima.InformacionClimaFragment
import mc.tfg_tiempo.databinding.ActivityMainBinding
import mc.tfg_tiempo.databinding.FragmentInformacionClimaBinding
import mc.tfg_tiempo.map.MapsFragment


class MainActivity : AppCompatActivity() {

    val enlace: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(enlace.root)

        enlace.bottomNavigation.setOnItemSelectedListener{
            val currentFragment = supportFragmentManager.findFragmentById(R.id.container_navegacion)
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
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(enlace.containerNavegacion.id,fragment)
        fragmentTransaction.commit()
    }
}
