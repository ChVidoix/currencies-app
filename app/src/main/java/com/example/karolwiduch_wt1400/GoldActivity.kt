package com.example.karolwiduch_wt1400

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONArray


class GoldActivity : AppCompatActivity() {

    private lateinit var goldValue: TextView
    private lateinit var chart: LineChart
    private lateinit var queue: RequestQueue
    private lateinit var url: String
    private lateinit var goldValues: Array<Double?>
    private lateinit var goldDates: Array<String?>
    private var goldCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.goldHeader)
        actionBar.setDisplayHomeAsUpEnabled(true)

        url = "http://api.nbp.pl/api/cenyzlota/last/30?format=json"
        queue = newRequestQueue(applicationContext)
        goldValue = findViewById(R.id.currentGoldValueTextView)
        chart = findViewById(R.id.monthChart)

        getData()
    }

    private fun getData() {
        val goldValuesRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                loadData(response)
                createChart()
            },
            { _ -> val intent = Intent(this, MainActivity::class.java).apply {}
                startActivity(intent)
            }
        )
        queue.add(goldValuesRequest)
    }

    private fun loadData(response: JSONArray?) {
        response?.let {
            goldCount = response.length()
            goldDates = arrayOfNulls(goldCount)
            goldValues = arrayOfNulls(goldCount)

            for (i in 0 until goldCount) {
                goldDates[i] = response.getJSONObject(i).getString("data")
                goldValues[i] = response.getJSONObject(i).getDouble("cena")
            }
        }
    }

    private fun createChart() {
        goldValue.text = String.format("Cena złota: %,.2f zł za gram", goldValues[goldCount - 1])

        val entries = ArrayList<Entry>()

        for (i in 0 until goldCount) {
            entries.add(Entry(i.toFloat(), goldValues[i]?.toFloat()!!))
        }

        val dataSet = LineDataSet(entries, "Ceny złota z 30 dni")
        val lineData = LineData(dataSet)
        chart.data = lineData
        this.chart.xAxis.valueFormatter = IndexAxisValueFormatter(goldDates)
        chart.invalidate()
    }
}