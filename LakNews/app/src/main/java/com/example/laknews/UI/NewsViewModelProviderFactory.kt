package com.example.laknews.UI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.laknews.Repository.NewsRepository
/**
 * An  class created to be able ro define how our viewModel(NewsViewModel) should be created
 *this allows us to pass in variables in the constructor of the viewModel
 * @author Hadi Jalali
 * @since 10/11/2020
 */


class NewsViewModelProviderFactory (val newsRepository: NewsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}