// Widuch Karol
// 303178
// udało się zrealizować wszystkie punkty

package com.example.karolwiduch_wt1400

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CurrenciesSingleton.prepareSingleton(applicationContext)

        if(!CurrenciesSingleton.loaded) {
            makeRequest()
            CurrenciesSingleton.loaded = true
        }

    }

    fun showCurrenciesList(view: View) {
        val intent = Intent(this@MainActivity, CurrenciesListActivity::class.java).apply {}
        startActivity(intent)
    }

    fun showGold(view: View) {
        val intent = Intent(this@MainActivity, GoldActivity::class.java).apply {}
        startActivity(intent)
    }

    fun showConverter(view: View) {
        val intent = Intent(this@MainActivity, ConverterActivity::class.java).apply {}
        startActivity(intent)
    }

    private fun makeRequest() {
        val queue = CurrenciesSingleton.getQueue()
        val urlA = "http://api.nbp.pl/api/exchangerates/tables/A?format=json"
        val urlB = "http://api.nbp.pl/api/exchangerates/tables/B?format=json"

        val currenciesRatesRequestA = JsonArrayRequest(Request.Method.GET, urlA, null,
                { responseA ->
                    val currenciesRatesRequestB = JsonArrayRequest(Request.Method.GET, urlB, null,
                            { responseB ->
                                CurrenciesSingleton.loadData(responseA, responseB)
                            },
                            { _ -> val intent = Intent(this@MainActivity, MainActivity::class.java).apply {}
                                startActivity(intent)
                            }
                    )
                    queue.add(currenciesRatesRequestB)
                },
                { _ -> val intent = Intent(this@MainActivity, MainActivity::class.java).apply {}
                    startActivity(intent)
                }
        )
        queue.add(currenciesRatesRequestA)
    }
}