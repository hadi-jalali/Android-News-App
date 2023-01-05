package com.example.laknews.UI


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laknews.Models.Article
import com.example.laknews.Models.NewsResponse
import com.example.laknews.Repository.NewsRepository
import com.example.laknews.Util.Resource
import com.example.laknews.Util.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * A class to call the repository functions and make the liveLists of different newsResponses
 *
 * @author Hadi Jalali
 * @since 10/11/2020
 */


class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {
    val TAG = "NewsViewModel"
    val currentUSer = FirebaseAuth.getInstance().currentUser
    private var interests = getInterests(currentUSer?.uid!!)

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    private var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    val selectedNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var selectedNewsPage = 1
    var selectedNewsResponse: NewsResponse? = null

    init {
        getBreakingNews(Utils.country)
        Log.w(TAG, getInterests(currentUSer!!.uid).toString())
        if (interests == null) {
            getSelectedNews("sports", "gb")
        } else {
            getSelectedNews(interests.toString(), "gb")
        }
    }


    //getting the breaking news and post the response to inform the fragments
    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    fun getSelectedNews(category: String, countryCode: String) = viewModelScope.launch {
        selectedNews.postValue(Resource.Loading())
        val response = newsRepository.getSelectedNews(category, countryCode, selectedNewsPage)
        selectedNews.postValue(handleSelectedNewsResponse(response))
    }

    //check if the response is successful, if it is the return it. I it's not then return the error message
    private fun handleSelectedNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                //increase the pageNumber as we move on to the next page of articles
                selectedNewsPage++
                if (selectedNewsResponse == null) {
                    selectedNewsResponse = resultResponse
                } else {
                    val oldArticles = selectedNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(selectedNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    if(oldArticles != newArticles){

                    }
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.updateAndInsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun getInterests(uid: String): String? {
        var interest: String? = null
        val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)
            .child("interest")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                interest = snapshot.getValue().toString()
                Log.w(TAG, currentUSer!!.uid)
                Log.w(TAG, interest)
            }

            override fun onCancelled(error: DatabaseError) {
                interest = "sports"
                Log.w(TAG, "ffsssss")
            }
        })
        return interest


    }


}