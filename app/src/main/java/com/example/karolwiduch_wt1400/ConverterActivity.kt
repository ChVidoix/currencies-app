package com.example.karolwiduch_wt1400

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class ConverterActivity : AppCompatActivity() {

    private lateinit var convertButton: TextView
    private lateinit var result: TextView
    private lateinit var amountEditText: EditText
    private lateinit var fromCurrency: Spinner
    private lateinit var toCurrency: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.exchangeHeader)
        actionBar.setDisplayHomeAsUpEnabled(true)

        fromCurrency = findViewById(R.id.currencyFromSpinner)
        toCurrency = findViewById(R.id.currencyToSpinner)

        val currencies = mutableListOf<String?>("PLN")
        currencies.addAll(CurrenciesSingleton.getCurrenciesNames())

        val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromCurrency.adapter = spinnerAdapter
        spinnerAdapter.addAll(currencies)
        toCurrency.adapter = spinnerAdapter
        spinnerAdapter.notifyDataSetChanged()

        convertButton = findViewById(R.id.convertButton)
        convertButton.setOnClickListener {convert()}

        result = findViewById(R.id.convertResultTextView)
        amountEditText = findViewById(R.id.amountEditText)
    }

    private fun convert() {

        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

        var currencyFromRate = 0.0
        var currencyToRate = 0.0
        currencyFromRate = if (fromCurrency.selectedItem.toString() == "PLN") {
            1.0
        } else {
            CurrenciesSingleton.getRate(fromCurrency.selectedItem.toString())!!
        }
        currencyToRate = if ( toCurrency.selectedItem.toString() == "PLN") {
            1.0
        } else {
            CurrenciesSingleton.getRate(toCurrency.selectedItem.toString())!!
        }

        val amount = amountEditText.text.toString().toDouble()
        val calculated = "$amount ${fromCurrency.selectedItem}\nto\n${String.format("%.2f", amount * currencyFromRate / currencyToRate)} ${toCurrency.selectedItem}"
        result.text = calculated
        }
}