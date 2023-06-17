package mc.tfg_tiempo.autocompletar

import android.content.Context
import android.location.Geocoder
import android.os.AsyncTask
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import java.io.IOException
import java.util.Locale
import java.util.concurrent.ExecutionException

class AutocompletarAdapter(context: Context) : ArrayAdapter<AutocompletePrediction>(context, android.R.layout.simple_dropdown_item_1line) {

    private lateinit var placesClient: PlacesClient
    private var geocoder: Geocoder? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Obtiene la vista del elemento en la posición especificada
        val fila = super.getView(position, convertView, parent)
        val item = getItem(position)
        val textView = fila.findViewById<TextView>(android.R.id.text1)
        // Establece el texto en el TextView utilizando el AutocompletePrediction correspondiente
        textView.text = item?.getFullText(null)
        return fila
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val resultList = ArrayList<AutocompletePrediction>()

                // Realizar una solicitud de autocompletado a Google Places API
                val request = FindAutocompletePredictionsRequest.builder()
                    .setQuery(constraint.toString())
                    .build()

                placesClient.findAutocompletePredictions(request).addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                    // Agregar cada predicción de autocompletado al resultado
                    for (prediction in response.autocompletePredictions) {
                        resultList.add(prediction)
                    }
                    filterResults.values = resultList
                    filterResults.count = resultList.size
                    // Publicar los resultados para mostrar en la interfaz de usuario
                    publishResults(constraint, filterResults)
                }?.addOnFailureListener { exception: Exception ->
                    // Manejar la excepción en caso de error
                    println(exception.toString())
                }

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // Limpiar los resultados anteriores
                clear()
                if (results != null && results.count > 0) {
                    // Agregar los nuevos resultados al adaptador y notificar los cambios
                    for (prediction in results.values as ArrayList<*>) {
                        add(prediction as AutocompletePrediction)
                        notifyDataSetChanged()
                    }
                } else {
                    // Notificar que no hay resultados válidos
                    notifyDataSetInvalidated()
                }
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                // Convertir el valor del resultado en una cadena legible para su visualización
                return (resultValue as AutocompletePrediction).getFullText(null)
            }
        }
    }

    // Establecer el cliente de Places utilizado para las solicitudes de autocompletado
    fun setPlacesClient(client: PlacesClient) {
        placesClient = client
        geocoder = Geocoder(context, Locale.getDefault())
    }

    fun getLatLng(position: Int): LatLng? {
        val item = getItem(position)
        val placeId = item?.placeId

        val geoDataClient = Places.createClient(context)
        val placeFields = listOf(Place.Field.LAT_LNG)

        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        try {
            val responseTask = geoDataClient.fetchPlace(request)
            val response = Tasks.await(responseTask)
            val place = response?.place
            val latLng = place?.latLng
            if (latLng != null) {
                val latitude = latLng.latitude
                val longitude = latLng.longitude
                return LatLng(latitude, longitude)
            }
        } catch (e: ApiException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        return null
    }




}

