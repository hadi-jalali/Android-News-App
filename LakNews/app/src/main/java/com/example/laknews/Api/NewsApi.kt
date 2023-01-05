package com.example.laknews.Api

import com.example.laknews.Models.NewsResponse
import com.example.laknews.Util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
/**
 * An interface to request different queries from the api
 *
 * @author Hadi Jalali
 * @since 10/11/2020
 */

interface NewsApi {
    val apiKey : String
    /**
     * get the breaking news
     * @param countryCode: a String containing 2 character code of a country
     * @param pageNumber: the number of the page we are currently on starting at 1
     * @param apiKey a String containing the key for NewsApi
     * @return a class of Response
     */
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "gb",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey:String=API_KEY
    ): Response<NewsResponse>
    /**
     * get the breaking news
     * @param query: a String containing the keyword to search for in the articles
     * @param pageNumber: the number of the page we are currently on starting at 1
     * @param apiKey a String containing the key for NewsApi
     * @return a class of Response
     */
    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        query:String,
        @Query("page")
        pageNumber: Int=1,
        @Query("apiKey")
        apiKey:String= API_KEY
    ): Response<NewsResponse>
    /**
     * get the breaking news
     * @param query: a String containing the keyword to search for in articles
     * @param pageNumber: the number of the page we are currently on starting at 1
     * @param apiKey a String containing the key for NewsApi
     * @param sortBy: keyword to sort the articles based on (I sore them by added date)
     * @return a class of Response
     */
    @GET ("v2/top-headlines")
    suspend fun getSelectedNews(
        @Query("category")
        category: String?=null,
        @Query("country")
        country: String="gb",
        @Query("page")
        pageNumber: Int=1,
        @Query("sortBy")
        sortBy:String = "publishedAt",
        @Query("apiKey")
        apiKey:String= API_KEY

        ): Response<NewsResponse>
}