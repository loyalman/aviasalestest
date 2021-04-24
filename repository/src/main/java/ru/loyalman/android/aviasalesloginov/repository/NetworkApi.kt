package ru.loyalman.android.aviasalesloginov.repository

import retrofit2.http.GET
import retrofit2.http.Query
import ru.loyalman.android.aviasalesloginov.models.AutocompleteResponse

interface NetworkApi {

    @GET("autocomplete")
    suspend fun autoComplete(
        @Query("term")
        search: String,
        @Query("lang")
        lang: String = "ru",
    ): AutocompleteResponse

}