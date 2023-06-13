package mc.tfg_tiempo.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
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
            temperatura.text = item.temperatura
            dia.text = item.dia
            val icon =  item.icon
            val iconUrl = "http://openweathermap.org/img/w/$icon.png"
            itemView.post{
                Picasso.get()
                    .load(iconUrl)
                    .into(imagen)
            }

            items.add(card)
        }
    }

}

