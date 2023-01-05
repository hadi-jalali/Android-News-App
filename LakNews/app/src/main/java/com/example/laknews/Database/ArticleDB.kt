package com.example.laknews.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.laknews.Models.Article

/**
 * An abstrac class for our room database containing the saved articles
 *
 * @author Hadi Jalali
 * @since 10/11/2020
 */

@Database(
    entities = [Article::class],
    version = 1
)
//because room only accepts primitive data types a type converter is used
@TypeConverters(Converter::class)
abstract class ArticleDB : RoomDatabase(){
    abstract fun articleDao():ArticleDao

    companion object {
        //volatile is used to make sure other threads see the changes of the instances variable immediately
        @Volatile
        private var instance:ArticleDB?=null
        private val LOCK = Any()

        // lock the class so other threads cannot access it other that this thread
        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance?:createDB(context).also{ instance=it}
        }
        /**
         * Create a Room database of article objects
         * @param context: context of the used application
         * @return a room database
         */
        private fun createDB(context: Context) = Room.databaseBuilder(context.applicationContext,ArticleDB::class.java
            ,"articleDB").build()

    }
}