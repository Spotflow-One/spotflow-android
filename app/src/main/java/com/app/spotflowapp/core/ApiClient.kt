package com.app.spotflowapp.core

import com.app.spotflowapp.models.AuthorizePaymentRequestBody
import com.app.spotflowapp.models.PaymentRequestBody
import com.app.spotflowapp.models.PaymentResponseBody
import com.app.spotflowapp.models.Rate
import com.app.spotflowapp.models.ValidatePaymentRequestBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface PaymentApi {

    @POST("api/v1/payments")
     fun createPayment(@Body paymentRequestBody: PaymentRequestBody): Call<PaymentResponseBody?>?

    @POST("api/v1/payments/authorize")
    fun authorizePayment(@Body body: AuthorizePaymentRequestBody?): Call<PaymentResponseBody?>?

    @POST("api/v1/payments/validate")
      fun validatePayment(@Body body: ValidatePaymentRequestBody?): Call<PaymentResponseBody?>?

    @GET("api/v1/payments/verify")
      fun verifyPayment(
        @Query("merchantId") merchantId: String?,
        @Query("reference") reference: String?
    ): Call<PaymentResponseBody?>?


    @GET("api/v1/payments/rates")
     fun getRate(
        @Query("from") from: String?,
        @Query("to") to: String?
    ): Call<Rate?>?
}



class AuthInterceptor(private val authToken: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        if (authToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer $authToken")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

object ApiClient {
    private const val BASE_URL = "http://dev-api.spotflow.one"

    private fun getHttpClient(authToken: String?): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(authToken))
            .build()
    }

    fun getRetrofit(authToken: String?): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getHttpClient(authToken))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}



