package mc.tfg_tiempo.autocompletar

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import okhttp3.internal.wait

class AutocompletarAdapter(context: Context) : ArrayAdapter<AutocompletePrediction>(context, android.R.layout.simple_dropdown_item_1line) {

    private lateinit var placesClient: PlacesClient

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = super.getView(position, convertView, parent)
        val item = getItem(position)
        val textView = row.findViewById<TextView>(android.R.id.text1)
        textView.text = item?.getFullText(null)
        return row
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

                placesClient.findAutocompletePredictions(request)?.addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                    for (prediction in response.autocompletePredictions) {
                        resultList.add(prediction)
                    }
                    filterResults.values = resultList
                    filterResults.count = resultList.size
                    publishResults(constraint, filterResults)
                }?.addOnFailureListener { exception: Exception ->
                    // Manejar la excepción en caso de error
                    println(exception.toString())
                }

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                if (results != null && results.count > 0) {
                    for (prediction in results.values as ArrayList<*>) {
                        add(prediction as AutocompletePrediction)
                        notifyDataSetChanged()
                    }
                } else {
                    notifyDataSetInvalidated()
                }
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue as AutocompletePrediction).getFullText(null)
            }
        }
    }
    fun setPlacesClient(client: PlacesClient) {
        placesClient = client
    }
}
