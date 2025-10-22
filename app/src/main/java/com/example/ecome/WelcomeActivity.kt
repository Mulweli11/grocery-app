package com.example.ecome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class WelcomeActivity : AppCompatActivity() {

    private lateinit var backgroundImage: ImageView
    private lateinit var btnGetStarted: Button
    private lateinit var btnAlreadyHaveAccount: Button

    private val handler = Handler(Looper.getMainLooper())
    private val imageUrls = mutableListOf<String>()
    private var currentIndex = 0
    private val slideDelay = 4000L // 4 seconds per image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        backgroundImage = findViewById(R.id.backgroundImage)
        btnGetStarted = findViewById(R.id.btnGetStarted)
        btnAlreadyHaveAccount = findViewById(R.id.btnAlreadyhaveaccount)

        fetchGroceryImages()

        btnGetStarted.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        btnAlreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun fetchGroceryImages() {
        val apiKey = "52380452-a06f228ef26eeeb8f154b1cd5"
        // Search specifically for fresh produce / grocery photos
        val url =
            "https://pixabay.com/api/?key=$apiKey&q=fresh+produce&image_type=photo&per_page=20"

        val request = Request.Builder().url(url).get().build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API_ERROR", "Failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use { res ->
                    if (!res.isSuccessful) {
                        Log.e("API_ERROR", "Unexpected code $res")
                        return
                    }

                    val body = res.body() // OkHttp 5 getter
                    if (body != null) {
                        val jsonData = body.string()
                        try {
                            val jsonObject = JSONObject(jsonData)
                            val hitsArray = jsonObject.getJSONArray("hits")
                            imageUrls.clear()
                            for (i in 0 until hitsArray.length()) {
                                val imageUrl =
                                    hitsArray.getJSONObject(i).getString("largeImageURL")
                                imageUrls.add(imageUrl)
                            }
                            runOnUiThread {
                                if (imageUrls.isNotEmpty()) startSlideshow()
                            }
                        } catch (e: Exception) {
                            Log.e("JSON_ERROR", "Parse error: ${e.message}")
                        }
                    }
                }
            }
        })
    }

    private fun startSlideshow() {
        handler.post(object : Runnable {
            override fun run() {
                if (imageUrls.isNotEmpty()) {
                    val imageUrl = imageUrls[currentIndex]
                    Glide.with(this@WelcomeActivity)
                        .load(imageUrl)
                        .centerCrop()
                        .into(backgroundImage)

                    currentIndex = (currentIndex + 1) % imageUrls.size
                    handler.postDelayed(this, slideDelay)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
