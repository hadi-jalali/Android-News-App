package com.example.laknews.setting

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.laknews.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.profile_fragment.*
/*
class ProfileSettingActivity: AppCompatActivity() {

   val TAG = "ProfileSettings"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_fragment)
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        var uri:String
        val uid = currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users").child("$uid")
            .child("profileImageUrl")
        if (currentUser != null) {
            text_phone.text = if (currentUser.phoneNumber.isNullOrEmpty()) "Add Number" else currentUser.phoneNumber
        }
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                uri = snapshot.getValue().toString()
                setUrl(uri)
                Log.w(TAG,"count:${snapshot.getValue()}")
                children.forEach {
                    println(it.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }
        })
        currentUser?.let { user ->
            text_phone.text = if (user.phoneNumber.isNullOrEmpty()) "Add Number" else user.phoneNumber

            if (user.isEmailVerified) {
                text_not_verified.visibility = View.INVISIBLE
            } else {
                text_not_verified.visibility = View.VISIBLE
            }
        }
    }

    fun setUrl(uri: String){
        val targetImageView = findViewById<ImageView>(R.id.image_profile_view)
        Glide.with(this).load(uri).centerCrop().fallback(R.drawable.profile).transform(
            CircleCrop()
        ).into(targetImageView)
        Log.w(TAG,"uri is :$uri")
    }
    }


*/