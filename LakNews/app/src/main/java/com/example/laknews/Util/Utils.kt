package com.example.laknews.Util

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.laknews.Models.AppUser
import com.example.laknews.UI.LoginActivity
import com.example.laknews.UI.NewsActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    val TAG = "Utils"
    fun DateToTimeFormat(oldstringDate: String?): String? {
        val p = PrettyTime(Locale(country))
        var isTime: String? = null
        try {
            val sdf = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.ENGLISH
            )
            val date = sdf.parse(oldstringDate)
            isTime = p.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return isTime
    }

    fun DateFormat(oldstringDate: String?): String? {
        val newDate: String
        val dateFormat = SimpleDateFormat("E, d MMM yyyy", Locale(country))
        newDate = try {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldstringDate)
            dateFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            oldstringDate!!
        }
        return newDate
    }

    val country: String
        get() {
            val locale = Locale.getDefault()
            val country = locale.country.toString()
            return country.toLowerCase()
        }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val user = it.getValue(AppUser::class.java)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun Context.login() {
        val intent = Intent(this, NewsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    fun Context.logout() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
    fun getInterests(uid: String): String? {
        var interest: String? = null
        val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)
            .child("interest")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                interest = snapshot.getValue().toString()
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




