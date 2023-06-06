package mc.tfg_tiempo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import mc.tfg_tiempo.R
import okhttp3.Response

import android.widget.Button
import mc.tfg_tiempo.WeatherApiClient
import mc.tfg_tiempo.WeatherCallback


class MainActivity : AppCompatActivity() {

    private lateinit var weatherApiClient: WeatherApiClient
    private lateinit var textViewTemperature: TextView
    private lateinit var textViewHumidity: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherApiClient = WeatherApiClient("95b1f5bbf3915b3c1c96eaf1e6c348e3")

        textViewTemperature = findViewById(R.id.textViewTemperature)
        textViewHumidity = findViewById(R.id.textViewHumidity)

        val buttonGetWeather = findViewById<Button>(R.id.buttonGetWeather)
        buttonGetWeather.setOnClickListener {
            val city = "Sevilla"
            weatherApiClient.getCurrentWeather(city, object : WeatherCallback {
                override fun onResponse(response: Response) {

                    val body = response.body?.string()
                    if (response.isSuccessful && !body.isNullOrEmpty()) {
                        val weatherData = WeatherApiClient.parseWeatherData(body)
                        val temperature = weatherData.temperature.toInt()
                        val humidity = weatherData.humidity

                        runOnUiThread {
                            textViewTemperature.text = "Temperatura: $temperature °C"
                            textViewHumidity.text = "Humedad: $humidity %"
                        }
                    } else {
                        onError()
                    }
                }

                override fun onError() {
                    runOnUiThread {
                        textViewTemperature.text = "Error al obtener el clima"
                        textViewHumidity.text = ""
                        println("aaaaaaaaaaaaa")
                    }
                }
            })
        }
    }
}
