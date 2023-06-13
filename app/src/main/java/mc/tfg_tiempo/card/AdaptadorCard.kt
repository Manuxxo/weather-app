package mc.tfg_tiempo.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import mc.tfg_tiempo.R

//class AdaptadorCard: RecyclerView



class AdaptadorCard ( private val data: List<DataCard>) : RecyclerView.Adapter<AdaptadorCard.Holder>() {

    private val items: MutableList<CardView>

    init {
        this.items = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dia: TextView = itemView.findViewById(R.id.cardDia)
        val temperatura: TextView = itemView.findViewById(R.id.cardTemperatura)
        val card: CardView = itemView.findViewById(R.id.card)
        val imagen : ImageView = itemView.findViewById(R.id.imageViewIcon)

        fun bind(item: DataCard){
            temperatura.text = item.temperatura.toInt().toString() +" º"
            dia.text = item.dia

            itemView.post{

                imagen.setImageResource(when(item.icon){
                    "01d" -> R.drawable.ic_sunny
                    "01n" -> R.drawable.moon
                    "02d" -> R.drawable.ic_cloudy
                    "02n" -> R.drawable.cloudy_moon
                    "03d" -> R.drawable.ic_sunnycloudy
                    "03n" -> R.drawable.ic_very_cloudy
                    "04d" -> R.drawable.ic_very_cloudy
                    "04n" -> R.drawable.ic_very_cloudy
                    "09d" -> R.drawable.ic_rainy
                    "09n" -> R.drawable.ic_rainy
                    "10d" -> R.drawable.ic_rainshower
                    "10n" -> R.drawable.ic_rainshower
                    "11d" -> R.drawable.ic_rainythunder
                    "11n" -> R.drawable.ic_rainythunder
                    "13d" -> R.drawable.ic_snowy
                    "13n" -> R.drawable.ic_snowy
                    "50d" -> R.drawable.ic_pressure
                    "50d" -> R.drawable.ic_pressure
                    else -> R.drawable.background
                })

               /* Picasso.get()
                    .load(iconUrl)
                    .into(imagen)*/

            }

            items.add(card)
        }
    }

}

