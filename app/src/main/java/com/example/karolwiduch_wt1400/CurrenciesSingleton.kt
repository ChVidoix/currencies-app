package com.example.karolwiduch_wt1400

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import com.blongho.country_data.Country
import com.blongho.country_data.World
import org.json.JSONArray

object CurrenciesSingleton {

    private lateinit var queue: RequestQueue
    private var data: Array<CurrencyDetails>? = null
    private lateinit var countries: List<Country>
    var loaded: Boolean = false
    private var aLength: Int = 0

    fun prepareSingleton(context: Context){
        queue = newRequestQueue(context)
        World.init(context)
        countries = World.getAllCountries().distinctBy { it.currency.code }

    }

    fun getQueue(): RequestQueue {
        return queue
    }

    internal fun loadData(responseA: JSONArray?, responseB: JSONArray?) {
        responseA?.let {
            responseB?.let {
                val currenciesA = responseA.getJSONObject(0).getJSONArray("rates")
                val currenciesB = responseB.getJSONObject(0).getJSONArray("rates")

                val currenciesCountA = currenciesA.length()
                val currenciesCountB = currenciesB.length()
                aLength = currenciesCountA

                val data = arrayOfNulls<CurrencyDetails>(currenciesCountA + currenciesCountB)

                for (i in 0 until currenciesCountA + currenciesCountB) {

                    if (i < currenciesCountA) {
                        val currencyCode = currenciesA.getJSONObject(i).getString("code")
                        val currencyRate = currenciesA.getJSONObject(i).getDouble("mid")

                        val url = "http://api.nbp.pl/api/exchangerates/rates/A/${currencyCode}/last/2/?format=json"
                        val currenciesValuesYesterdayRequest = JsonObjectRequest(Request.Method.GET, url, null,
                                { response ->
                                    val rates = response.getJSONArray("rates")
                                    val diff = rates.getJSONObject(1).getDouble("mid") - rates.getJSONObject(0).getDouble("mid")
                                    data[i] = CurrencyDetails(currencyCode, currencyRate, getFlag(currencyCode), diff)
                                },
                                { _ -> TODO("ERROR") }
                        )
                        this.queue.add(currenciesValuesYesterdayRequest)
                    } else {
                        val currencyCode = currenciesB.getJSONObject(i - currenciesCountA).getString("code")
                        val currencyRate = currenciesB.getJSONObject(i - currenciesCountA).getDouble("mid")

                        val url = "http://api.nbp.pl/api/exchangerates/rates/B/${currencyCode}/last/2/?format=json"
                        val currenciesValuesYesterdayRequest = JsonObjectRequest(Request.Method.GET, url, null,
                                { response ->
                                    val rates = response.getJSONArray("rates")
                                    val diff = rates.getJSONObject(1).getDouble("mid") - rates.getJSONObject(0).getDouble("mid")
                                    data[i] = CurrencyDetails(currencyCode, currencyRate, getFlag(currencyCode), diff)
                                },
                                { _ -> TODO("ERROR") }
                        )
                        this.queue.add(currenciesValuesYesterdayRequest)
                    }
                }
                this.data = data as Array<CurrencyDetails>
            }
        }
    }

    fun getCurrency(position: Int): CurrencyDetails {
        return data!![position]
    }

    fun getALength(): Int {
        return aLength
    }

    private fun getFlag(code: String): Int {
        return countries.find { it.currency.code == code }?.flagResource ?: World.getWorldFlag()
    }

    fun getData(): Array<CurrencyDetails> {
        return data ?: emptyArray()
    }

    fun getRate(code: String): Double? {
        return data!!.find { it.currencyCode == code}?.rate
    }

    fun getCurrenciesNames(): List<String?> {
        val names = mutableListOf<String>()

        for(i in data!!.indices) {
            names += (data!![i].currencyCode)
        }

        return names
    }

}