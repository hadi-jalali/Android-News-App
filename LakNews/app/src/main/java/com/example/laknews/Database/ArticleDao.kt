package com.example.laknews.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.laknews.Models.Article

/**
 * An interface to request different queries from the local saved articles database
 *
 * @author Hadi Jalali
 * @since 10/11/2020
 */

@Dao
interface ArticleDao {
    /**
     * Insert an specific article
     * @param onConflict: an stragedy to determin what happens if the article
     * already exist which is to replace in this case
     * @param article: the article that is being inserted to the database
     * @return the id of the inserted article
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAndInsert(article: Article): Long
    /**
     * Get all the articles
     * @param an sql query to get all the element of the articles tabl
     * @return a updatable list of all the saved articles
     */
    @Query("SELECT * FROM articles")
    fun getAllArticle():LiveData<List<Article>>

    /**
     * Delete an article from the database
     * @param article: The article that is going to bedeleted
     */
    @Delete
    suspend fun deleteArticle(article: Article)


}