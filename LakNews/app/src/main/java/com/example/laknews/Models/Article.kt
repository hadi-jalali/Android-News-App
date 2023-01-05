package com.example.laknews.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
//entity was given to be able to access the articles which make it a table
@Entity(
    tableName = "articles"

)
data class Article(
    //setting the if as th primary key of the table
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : Serializable