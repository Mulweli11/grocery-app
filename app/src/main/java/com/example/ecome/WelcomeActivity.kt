package com.example.ecome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class WelcomeActivity : AppCompatActivity() {

    private lateinit var backgroundImage: ImageView
    private lateinit var btnGetStarted: Button
    private lateinit var btnAlreadyHaveAccount: Button

    private val handler = Handler(Looper.getMainLooper())
    private var imageUrls = listOf<String>()
    private var currentIndex = 0
    private val slideDelay = 4000L // 4 seconds per image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        backgroundImage = findViewById(R.id.backgroundImage)
        btnGetStarted = findViewById(R.id.btnGetStarted)
        btnAlreadyHaveAccount = findViewById(R.id.btnAlreadyhaveaccount)

        // Load image URLs from JSON
        imageUrls = loadImagesFromJson() ?: emptyList()

        // Start slideshow if images exist
        if (imageUrls.isNotEmpty()) startImageSlideshow()

        //  Navigate to RegisterActivity
        btnGetStarted.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        //  Navigate to LoginActivity
        btnAlreadyHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadImagesFromJson(): List<String>? {
        return try {
            val inputStream = assets.open("images.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonText = reader.readText()
            reader.close()

            val jsonObject = JSONObject(jsonText)
            val jsonArray = jsonObject.getJSONArray("images")
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
            list
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun startImageSlideshow() {
        handler.post(object : Runnable {
            override fun run() {
                val imageUrl = imageUrls[currentIndex]
                backgroundImage.animate()
                    .alpha(0f)
                    .setDuration(800)
                    .withEndAction {
                        Glide.with(this@WelcomeActivity)
                            .load(imageUrl)
                            .centerCrop()
                            .into(backgroundImage)

                        backgroundImage.animate()
                            .alpha(1f)
                            .setDuration(800)
                            .start()
                    }.start()

                currentIndex = (currentIndex + 1) % imageUrls.size
                handler.postDelayed(this, slideDelay)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
