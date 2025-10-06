package com.example.addressautocomplete

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import com.example.ecome.R
class AddressActivity : AppCompatActivity() {

    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var etCity: EditText
    private lateinit var etState: EditText
    private lateinit var etPostalCode: EditText
    private lateinit var etCountry: EditText
    private lateinit var btnFindAddress: Button

    private val apiKey = "YOUR_OPENCAGE_API_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        etPhone = findViewById(R.id.etPhone)
        etAddress = findViewById(R.id.etAddress)
        etCity = findViewById(R.id.etCity)
        etState = findViewById(R.id.etState)
        etPostalCode = findViewById(R.id.etPostalCode)
        etCountry = findViewById(R.id.etCountry)
        btnFindAddress = findViewById(R.id.btnFindAddress)

        btnFindAddress.setOnClickListener {
            val address = etAddress.text.toString().trim()
            if (address.isNotEmpty()) {
                findAddressDetails(address)
            } else {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun findAddressDetails(address: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url =
                    "https://api.opencagedata.com/geocode/v1/json?q=${address.replace(" ", "+")}&key=$apiKey"
                val response = URL(url).readText()
                val jsonObject = JSONObject(response)
                val results = jsonObject.getJSONArray("results")

                if (results.length() > 0) {
                    val components = results.getJSONObject(0).getJSONObject("components")

                    val city = components.optString("city", components.optString("town", ""))
                    val state = components.optString("state", "")
                    val postcode = components.optString("postcode", "")
                    val country = components.optString("country", "")

                    withContext(Dispatchers.Main) {
                        etCity.setText(city)
                        etState.setText(state)
                        etPostalCode.setText(postcode)
                        etCountry.setText(country)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AddressActivity, "Address not found", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddressActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
