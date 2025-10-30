// File: com.example.ecome/OrderConfirmationActivity.kt

package com.example.ecome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class OrderConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        val orderTotal = intent.getDoubleExtra("ORDER_TOTAL", 0.0)
        val itemCount = intent.getIntExtra("ITEM_COUNT", 0)

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))
        val totalFormatted = currencyFormat.format(orderTotal)

        findViewById<TextView>(R.id.confTotalText).text = totalFormatted
        findViewById<TextView>(R.id.confItemCountText).text = "$itemCount item(s)"

        findViewById<Button>(R.id.backToHomeButton).setOnClickListener {
            // Navigate back to the main shopping screen
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}