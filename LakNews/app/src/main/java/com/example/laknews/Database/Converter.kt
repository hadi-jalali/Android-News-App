package com.example.laknews.Database

import androidx.room.TypeConverter
import com.example.laknews.Models.Source

/**
 * An class to convert source object to String and vise-versa to be used in the room database
 *
 * @author Hadi Jalali
 * @since 10/11/2020
 */


class Converter {
    //convert from source to string
    @TypeConverter
    fun sourceToString(source: Source): String{
        return source.name
    }
    //convert from string to source
    @TypeConverter
    fun stringToSource(sourcename: String) : Source {
        return Source(sourcename,sourcename)
    }
}