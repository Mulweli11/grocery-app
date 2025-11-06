package com.example.ecome

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.ecome.utils.TranslationManager

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val languages = listOf("English", "isiZulu", "Afrikaans", "isiXhosa")
        val listView: ListView = findViewById(R.id.languageListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, languages)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> TranslationManager.setLanguage(TranslationManager.Language.ENGLISH)
                1 -> TranslationManager.setLanguage(TranslationManager.Language.ZULU)
                2 -> TranslationManager.setLanguage(TranslationManager.Language.AFRIKAANS)
                3 -> TranslationManager.setLanguage(TranslationManager.Language.XHOSA)
            }

            // Optionally show a toast
            // Toast.makeText(this, "Language changed", Toast.LENGTH_SHORT).show()
            finish() // Close settings and return
        }
    }
}
