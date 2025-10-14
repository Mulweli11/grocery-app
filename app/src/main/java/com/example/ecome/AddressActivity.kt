package com.example.ecome
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ecome.HomeActivity
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.example.ecome.R


class AddressActivity : AppCompatActivity() {

    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var etCity: EditText
    private lateinit var etState: EditText
    private lateinit var etPostalCode: EditText
    private lateinit var etCountry: EditText
    private lateinit var btnFindAddress: Button

    // Nominatim is a free public API for open-source geocoding.

    private val NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org/search?format=json&limit=1&addressdetails=1&q="

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
        // Use Dispatchers.IO for network operation
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Encode the address for safe URL construction
                val encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString())
                val url = NOMINATIM_BASE_URL + encodedAddress


                val connection = URL(url).openConnection().apply {
                    setRequestProperty("User-Agent", "YourAppName/1.0") // IMPORTANT: Use a unique name
                }
                val response = connection.getInputStream().bufferedReader().use { it.readText() }

                val jsonArray = org.json.JSONArray(response)

                if (jsonArray.length() > 0) {
                    val result = jsonArray.getJSONObject(0)
                    val components = result.optJSONObject("address")

                    if (components != null) {

                        val city = components.optString("city", components.optString("town", components.optString("village", "")))
                        val state = components.optString("state", "")
                        // Nominatim uses "postcode" directly
                        val postcode = components.optString("postcode", "")
                        val country = components.optString("country", "")

                        withContext(Dispatchers.Main) {
                            etCity.setText(city)
                            etState.setText(state)
                            etPostalCode.setText(postcode)
                            etCountry.setText(country)

                            // 1. Show Success Toast
                            Toast.makeText(this@AddressActivity, "Address details filled successfully!", Toast.LENGTH_LONG).show()

                            // 2. Navigate to HomeActivity
                            val intent = Intent(this@AddressActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish() // Close AddressActivity so the user can't navigate back easily
                        }
                    } else {
                        // Handle case where address details node is missing
                        handleError("Address components not found in response.")
                    }
                } else {
                    handleError("Address not found by geocoding service.")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                handleError("Network Error: ${e.message}")
            }
        }
    }

    // Utility function to handle errors on the Main thread
    private suspend fun handleError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@AddressActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}