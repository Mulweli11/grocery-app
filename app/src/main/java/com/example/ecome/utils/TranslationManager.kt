package com.example.ecome.utils

object TranslationManager {

    enum class Language(val code: String) {
        ENGLISH("en"),
        ZULU("zu"),
        AFRIKAANS("af"),
        XHOSA("xh")
    }

    private var currentLanguage = Language.ENGLISH

    fun setLanguage(language: Language) {
        currentLanguage = language
    }

    fun getTranslation(key: String): String {
        val translations = mapOf(
            "title_account" to mapOf(
                "en" to "Your Account",
                "zu" to "I-akhawunti yakho",
                "af" to "Jou Rekening",
                "xh" to "I-Akhawunti yakho"
            ),
            "signed_in_as" to mapOf(
                "en" to "Signed in as:",
                "zu" to "Ungene njenge:",
                "af" to "Teken in as:",
                "xh" to "Ungene njenge:"
            ),
            "sign_out" to mapOf(
                "en" to "Sign Out",
                "zu" to "Phuma",
                "af" to "Teken uit",
                "xh" to "Phuma"
            ),
            "guest_message" to mapOf(
                "en" to "You are viewing this as a guest.",
                "zu" to "Ubuka lokhu njenge-Guest.",
                "af" to "Jy kyk dit as 'n gas.",
                "xh" to "Ujongile oku njenge-Guest."
            ),
            "signed_out" to mapOf(
                "en" to "Successfully signed out.",
                "zu" to "Ukuphuma kwenziwe ngempumelelo.",
                "af" to "Suksesvol uitgeskakel.",
                "xh" to "Uphume ngempumelelo."
            ),
            "already_signed_out" to mapOf(
                "en" to "You are already signed out.",
                "zu" to "Usuphume kakade.",
                "af" to "Jy is reeds uitgeteken.",
                "xh" to "Usuphume kakade."
            )
        )

        return translations[key]?.get(currentLanguage.code) ?: key
    }
}
