package com.example.karolwiduch_wt1400

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class CurrenciesListAdapter(internal var dataSet: Array<CurrencyDetails>, private val context: Context) : RecyclerView.Adapter<CurrenciesListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val currencyCodeTextView: TextView = view.findViewById(R.id.currencyCodeTextView)
        val rateTextView: TextView = view.findViewById(R.id.rateTextView)
        val flagView: ImageView = view.findViewById(R.id.flagView)
        val arrowView: ImageView = view.findViewById(R.id.arrowView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.currency, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currency = this.dataSet[position]
        holder.currencyCodeTextView.text = currency.currencyCode
        holder.rateTextView.text = String.format("%.2f", currency.rate)
        holder.flagView.setImageResource(currency.flag)

        when {
            currency.diff > 0 -> {
                holder.arrowView.setImageResource(R.drawable.up)
            }
            currency.diff < 0 -> {
                holder.arrowView.setImageResource(R.drawable.down)
            }
            else -> {
                holder.arrowView.setImageResource(R.drawable.still)
            }
        }

        holder.itemView.setOnClickListener { goToDetails(position) }
    }

    private fun goToDetails(position: Int) {
        val intent = Intent(this.context, CurrencyDetailsActivity::class.java).apply {
            putExtra("position", position)
        }
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
