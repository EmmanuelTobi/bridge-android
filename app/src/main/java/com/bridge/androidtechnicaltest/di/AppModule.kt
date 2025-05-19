package com.bridge.androidtechnicaltest.di

import com.bridge.androidtechnicaltest.data.api.PupilApiService
import com.bridge.androidtechnicaltest.data.repository.PupilRepositoryImpl
import com.bridge.androidtechnicaltest.domain.PupilRepository
import com.bridge.androidtechnicaltest.presentation.viewmodel.PupilViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    includes(databaseModule)
    single {
        val requestInterceptor = Interceptor { intercept ->
            val original = intercept.request()
            val request = original.newBuilder()
                .header("X-Request-ID", "9575b565-90f3-4904-8854-e48220df6f4c")
                .method(original.method, original.body)
                .build()
            intercept.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl("https://androidtechnicaltestapi-test.bridgeinternationalacademies.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(PupilApiService::class.java)
    }

    single<PupilRepository> {
        PupilRepositoryImpl(get(), get())
    }

    viewModel {
        PupilViewModel(get())
    }

}