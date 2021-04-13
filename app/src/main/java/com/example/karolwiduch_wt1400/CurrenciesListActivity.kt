package com.example.karolwiduch_wt1400

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest

class CurrenciesListActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CurrenciesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencies_list)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.currencyHeader)
        actionBar.setDisplayHomeAsUpEnabled(true)

        recycler = findViewById(R.id.currenciesListRecyclerID)
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        adapter = CurrenciesListAdapter(CurrenciesSingleton.getData(), this)
        recycler.adapter = adapter
        val data = CurrenciesSingleton.getData()
        adapter.dataSet = data
        adapter.notifyDataSetChanged()
    }
}