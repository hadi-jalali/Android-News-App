package com.example.laknews.Repository

import android.app.DownloadManager
import com.example.laknews.Api.RetrofitInstance
import com.example.laknews.Models.Article
import com.example.laknews.Database.ArticleDB

/**
 * A class to get the data from database and newsapi tretrofit and request different queries
 *
 * @author Hadi Jalali
 * @since 10/11/2020
 */


class NewsRepository (val db : ArticleDB) {
    //getting the breaking news by calling the breakingNews function from newsapi
    suspend fun getBreakingNews(countryCode: String, pagenumber:Int) = RetrofitInstance.
    api.getBreakingNews(countryCode,pagenumber)

    //getting the searched news by calling the breakingNews function from newsapi
    suspend fun searchNews(searchQuery: String, pagenumber:Int) = RetrofitInstance.
    api.searchForNews(searchQuery,pagenumber)

    //getting the searched news by calling the breakingNews function from newsapi
    suspend fun getSelectedNews(category: String,countryCode: String, pagenumber: Int) = RetrofitInstance.
    api.getSelectedNews(category,countryCode,pagenumber)

    suspend fun updateAndInsert(article: Article) = db.articleDao().updateAndInsert(article)

    fun getSavedNews() = db.articleDao().getAllArticle()

    suspend fun deleteArticle(article: Article) = db.articleDao().deleteArticle(article)

}