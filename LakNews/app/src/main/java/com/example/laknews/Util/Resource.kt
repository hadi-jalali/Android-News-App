package com.example.laknews.Util
/**
 * An generic class with inner classes created to handle our network responses which allow us to differentiate between
 * successful and non-successful response
 *
 * @author Hadi Jalali
 * @since 10/11/2020
 */

sealed class Resource<T>(val data: T?=null, val message: String?=null) {
    //a successful request
    class Success<T>(data: T) : Resource<T>(data)
    //a failed request
    class Error<T>(message: String , data: T?=null) : Resource<T>(data,message)
    //a request which is loading
    class Loading<T> : Resource<T>()
}