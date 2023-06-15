package mc.tfg_tiempo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Splash : AppCompatActivity(){

    private val splashTimeOut: Long = 3000
    private lateinit var splashImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashImage = findViewById(R.id.splash)

        // Definir la animación de transparencia (desvanecimiento)
        val fadeSalida = AlphaAnimation(1f, 0f)
        val fadeEntrada = AlphaAnimation(0f, 1f)
        fadeSalida.duration = 2200 // Duración de la animación en milisegundos
        fadeEntrada.duration = 1000 // Duración de la animación en milisegundos

        fadeEntrada.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                splashImage.startAnimation(fadeSalida)
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })

        // Listener para ocultar la vista de bienvenida y mostrar el FragmentContainerView al finalizar la animación
        fadeSalida.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                splashImage.visibility = ImageView.GONE
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })

        // Inicia la animación
        splashImage.startAnimation(fadeEntrada)

        Handler().postDelayed({
            // Lógica para abrir el fragmento deseado después de la pantalla de inicio
            val intent = Intent(this, MainActivity::class.java) // Reemplaza "MainActivity" con la actividad principal de tu aplicación
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            finish() // Cierra la actividad de la pantalla de inicio para que no se pueda volver a ella presionando el botón Atrás
        }, splashTimeOut)
    }
}