package mc.tfg_tiempo

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import mc.tfg_tiempo.databinding.ActivityMainBinding
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    val enlace: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(enlace.root)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    /////////////////////////////////////////////////////////////////////////////////
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

       when(item.itemId){
            R.id.salir ->{
                val builder = AlertDialog.Builder(this)
                builder.setMessage("¿Seguro que quiere salir?")
                    .setTitle("Confirmación")
                    .setPositiveButton(android.R.string.ok){ _, _ ->
                        exitProcess(0)
                    }
                    .setNegativeButton(android.R.string.cancel){ _, _ -> }
                builder.show()
            }
          /*  R.id.informacion ->{
                findNavController(enlace.contenedorArriba.id).navigate(R.id.action_inicioFragment_to_infoFragment)
                desactivar()
            }*/
        }
        return super.onOptionsItemSelected(item)
    }
}
