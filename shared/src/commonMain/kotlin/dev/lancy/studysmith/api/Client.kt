package dev.lancy.studysmith.api

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient

object Client {
    const val BASE_URL = "https://zzhfettpqjqqendyuyho.supabase.co"
    const val ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6In" +
        "p6aGZldHRwcWpxcWVuZHl1eWhvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTAxMDY0OTAsImV4cCI6MjA2NT" +
        "Y4MjQ5MH0.3crnU7cCfOuDuP-cylXcKOFpLJA529IVwfIgUF1jCNQ"

    val supabase = createSupabaseClient(
        supabaseUrl = BASE_URL,
        supabaseKey = ANON_KEY,
    ) {
        install(Auth) {
            scheme = "studysmith"
            host = "callback"
            flowType = FlowType.PKCE
        }
    }

    val auth = supabase.auth
}
