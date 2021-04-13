package com.example.karolwiduch_wt1400

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONObject

class CurrencyDetailsActivity : AppCompatActivity() {
    private lateinit var weekChart: LineChart
    private lateinit var monthChart: LineChart
    private var queue: RequestQueue = CurrenciesSingleton.getQueue()
    private lateinit var url: String
    private lateinit var todayValue: TextView
    private lateinit var yesterdayValue: TextView
    private lateinit var currencyValues: Array<Double?>
    private lateinit var currencyDates: Array<String?>
    private lateinit var currencyWeekDates: Array<String?>
    private var currencyCount: Int = 0
    private lateinit var currencyCode: String
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_details)

        val position = intent.getIntExtra("position", 0)
        currencyCode = CurrenciesSingleton.getCurrency(position).currencyCode

        val actionBar = supportActionBar
        actionBar!!.title = "Szczegóły: $currencyCode"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val table: String = if (position < CurrenciesSingleton.getALength()) "A" else "B"
        url = "http://api.nbp.pl/api/exchangerates/rates/${table}/${currencyCode}/last/30/?format=json"
        todayValue = findViewById(R.id.todayRateTextView)
        yesterdayValue = findViewById(R.id.yesterdayRateTextView)
        monthChart = findViewById(R.id.monthChart)
        weekChart = findViewById(R.id.weekChart)
        getData()
    }

    private fun getData() {
        val currencyValuesRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                loadData(response)
                createCharts()
            },
            { _ -> val intent = Intent(this, MainActivity::class.java).apply {}
                startActivity(intent)
            }
        )
        queue.add(currencyValuesRequest)
    }

    private fun loadData(response: JSONObject) {
        response.let {
            name = response.getString("currency").toString()
            supportActionBar!!.title = "Szczegóły: $name"
            val rates = response.getJSONArray("rates")
            currencyCount = rates.length()
            currencyDates = arrayOfNulls(currencyCount)
            currencyValues = arrayOfNulls(currencyCount)
            for (i in 0 until currencyCount) {
                currencyDates[i] = rates.getJSONObject(i).getString("effectiveDate")
                currencyValues[i] = rates.getJSONObject(i).getDouble("mid")
            }
            currencyWeekDates = currencyDates.copyOfRange(currencyCount-7, currencyCount)
        }
    }

    private fun createCharts() {
        todayValue.text = String.format("Cena $currencyCode dzisiaj: %,.2f", currencyValues[currencyCount-1])
        yesterdayValue.text = String.format("Cena $currencyCode wczoraj: %,.2f", currencyValues[currencyCount-2])

        val entriesMonth = ArrayList<Entry>()

        for (i in 0 until currencyCount) {
            entriesMonth.add(Entry(i.toFloat(), currencyValues[i]?.toFloat()!!))
        }

        val dataSet = LineDataSet(entriesMonth, "Ceny $currencyCode z 30 dni")
        val lineData = LineData(dataSet)
        monthChart.data = lineData
        this.monthChart.xAxis.valueFormatter = IndexAxisValueFormatter()
        monthChart.invalidate()

        val entriesWeek = ArrayList<Entry>()

        for (i in currencyCount - 7 until currencyCount) {
            entriesWeek.add(Entry(i.toFloat(), currencyValues[i]?.toFloat()!!))
        }

        val weekDataSet = LineDataSet(entriesWeek, "Ceny $currencyCode z 7 dni")
        val weekLineData = LineData(weekDataSet)
        weekChart.data = weekLineData
        weekChart.xAxis.valueFormatter = IndexAxisValueFormatter()
        weekChart.invalidate()
    }
}