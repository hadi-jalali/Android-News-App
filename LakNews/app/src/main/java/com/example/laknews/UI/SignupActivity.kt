package com.example.laknews.UI

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.laknews.Models.AppUser
import com.example.laknews.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.signup_layout.*
import java.util.*


class SignupActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var email: String
    lateinit var preferences: String
    var photoUri: Uri? = null
    val TAG = "SignupActivity"
    val sdk = Build.VERSION.SDK_INT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_layout)
        auth = FirebaseAuth.getInstance()
        next_btn.setOnClickListener {
            signupUser()
        }


    }

    private fun signupUser() {
        email = signupEmail.text.toString()
        val password = signup_password.text.toString()
        preferences = input_interests.text.toString()
        if (email.isEmpty()) {
            signupEmail.error = "Please enter an email"
            signupEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.error = "Please enter a valid email"
            signupEmail.requestFocus()
            return
        }
        if (password.isEmpty()) {
            signup_password.error = "Please enter your password"
            signup_password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        saveUserToFirebaseDatabase(uid)
                    }
                    val firestoreDB = FirebaseFirestore.getInstance()
                    val user1 = hashMapOf(
                        "preference" to preferences,
                        "email" to email,
                        "photo" to photoUri.toString(),
                        "uid" to uid
                    )

                    firestoreDB.collection("users")
                        .add(user1)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }

                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()

                } else {
                    Toast.makeText(
                        baseContext, "Sign up failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }


    private fun saveUserToFirebaseDatabase(uid: String) {
        //val uid = FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        val user = AppUser(uid,email,"",preferences)
        ref.setValue(user)
        Log.w(TAG,"succes???")

    }

}


