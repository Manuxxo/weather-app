package mc.tfg_tiempo.api_clima

data class DataWeather(val temperatura: Double, val humedad: Int, val icon:String, val sensacionTermica: Int, val tempMaxima: Int, val tempMinima: Int, val presion: Int, val estado: String)
