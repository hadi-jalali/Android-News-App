package com.example.laknews.Api

import com.example.laknews.Util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * An class that allow us to connect to newsApi interface and request from anywhere in the code
 *
 * @author Hadi Jalali
 * @since 10/11/2020
 */
class RetrofitInstance {
    companion object{

        private val retrofit by lazy {
            //used to check which responses we do to be able to debug
            val logging=HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(logging).build()
            //setting up the retrofit here
            Retrofit.Builder().baseUrl(BASE_URL)
                //A kotlin extension was used to convert Gson to kotlin
                .addConverterFactory(GsonConverterFactory.create()).client(client).build()
        }
        //creation of retrofit
        val api by lazy{
            retrofit.create(NewsApi::class.java)
        }
    }
}