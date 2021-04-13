package com.example.karolwiduch_wt1400

data class CurrencyDetails(var currencyCode: String, var rate: Double, var flag: Int, var diff: Double = 0.0) {
    init {
        when (currencyCode) {
            "EUR" -> {
                flag =  R.drawable.eu
            }
            "GBP" -> {
                flag = R.drawable.gb
            }
            "USD" -> {
                flag = R.drawable.us
            }
            "CHF" -> {
                flag = R.drawable.ch
            }
            "HKD" -> {
                flag = R.drawable.hk
            }
            else -> {}
        }
    }
}
