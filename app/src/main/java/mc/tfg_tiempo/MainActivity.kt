package mc.tfg_tiempo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mc.tfg_tiempo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    val enlace: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(enlace.root)


    }
}
