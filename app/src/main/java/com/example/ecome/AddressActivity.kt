package com.example.ecome

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ecome.HomeActivity
import kotlinx.coroutines.*
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class AddressActivity : AppCompatActivity() {

    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var etCity: EditText
    private lateinit var etState: EditText
    private lateinit var etPostalCode: EditText
    private lateinit var etCountry: EditText
    private lateinit var btnFindAddress: Button

    // Base URL for OpenStreetMap Nominatim
    private val NOMINATIM_BASE_URL =
        "https://nominatim.openstreetmap.org/search?format=json&limit=1&addressdetails=1&q="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        // Initialize views
        etPhone = findViewById(R.id.etPhone)
        etAddress = findViewById(R.id.etAddress)
        etCity = findViewById(R.id.etCity)
        etState = findViewById(R.id.etState)
        etPostalCode = findViewById(R.id.etPostalCode)
        etCountry = findViewById(R.id.etCountry)
        btnFindAddress = findViewById(R.id.btnFindAddress)

        btnFindAddress.setOnClickListener {
            val address = etAddress.text.toString().trim()
            if (address.isEmpty()) {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
            } else {
                findAddressDetails(address)
            }
        }
    }

    private fun findAddressDetails(address: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString())
                val url = URL(NOMINATIM_BASE_URL + encodedAddress)

                // Use HttpURLConnection for safer and clearer networking
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("User-Agent", "EcomeApp/1.0 (contact@example.com)") // Required
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }

                    val jsonArray = JSONArray(response)

                    if (jsonArray.length() > 0) {
                        val result = jsonArray.getJSONObject(0)
                        val components = result.optJSONObject("address")

                        if (components != null) {
                            val city = components.optString("city",
                                components.optString("town", components.optString("village", "")))
                            val state = components.optString("state", "")
                            val postcode = components.optString("postcode", "")
                            val country = components.optString("country", "")

                            withContext(Dispatchers.Main) {
                                etCity.setText(city)
                                etState.setText(state)
                                etPostalCode.setText(postcode)
                                etCountry.setText(country)

                                Toast.makeText(
                                    this@AddressActivity,
                                    "Address details filled successfully!",
                                    Toast.LENGTH_LONG
                                ).show()

                                // Navigate to HomeActivity
                                val intent = Intent(this@AddressActivity, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            showError("No detailed address info found.")
                        }
                    } else {
                        showError("Address not found. Try being more specific.")
                    }
                } else {
                    showError("API error: $responseCode")
                }

                connection.disconnect()

            } catch (e: Exception) {
                e.printStackTrace()
                showError("Network Error: ${e.message}")
            }
        }
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@AddressActivity, message, Toast.LENGTH_LONG).show()
        }
    }
}
