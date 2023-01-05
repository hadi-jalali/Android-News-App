package com.example.laknews.setting

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.laknews.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {

    private val DEFAULT_IMAGE_URL = "https://picsum.photos/200"

    lateinit var imageUri: Uri
    private val REQUEST_IMAGE_CAPTURE = 100
    val TAG = "ProfileFRagment"

    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUser?.let { user ->
            Glide.with(this)
                .load(user.photoUrl)
                .fallback(R.drawable.profile)
                .transform(CircleCrop())
                .into(image_view)
            Log.w(TAG, "uri is:${user.photoUrl}")

            //interests_text.text = if (user.phoneNumber.isNullOrEmpty()) "Add Number" else user.phoneNumber

            if (user.isEmailVerified) {
                text_not_verified.visibility = View.INVISIBLE
            } else {
                text_not_verified.visibility = View.VISIBLE
            }
        }

        image_view.setOnClickListener {
            takePictureIntent()
        }

        button_save.setOnClickListener {

            val photo = when {
                ::imageUri.isInitialized -> imageUri
                currentUser?.photoUrl == null -> Uri.parse(DEFAULT_IMAGE_URL)
                else -> currentUser.photoUrl
            }

            /*val name = edit_text_name.text.toString().trim()

            if (name.isEmpty()) {
                edit_text_name.error = "name required"
                edit_text_name.requestFocus()
                return@setOnClickListener
            }*/

            val updates = UserProfileChangeRequest.Builder()
                .setPhotoUri(photo)
                .build()

            progressbar.visibility = View.VISIBLE

            currentUser?.updateProfile(updates)
                ?.addOnCompleteListener { task ->
                    progressbar.visibility = View.INVISIBLE
                    if (task.isSuccessful) {
                        Toast.makeText(this.context, "Profile Updated", Toast.LENGTH_SHORT)
                    } else {
                        Toast.makeText(this.context, task.exception?.message!!, Toast.LENGTH_SHORT)
                    }
                }

        }


        text_not_verified.setOnClickListener {

            currentUser?.sendEmailVerification()
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this.context, "Verificcation email Sent", Toast.LENGTH_SHORT)
                    } else {
                        Toast.makeText(this.context, it.exception?.message!!, Toast.LENGTH_SHORT)
                    }
                }

        }

        text_email.setOnClickListener {
            val action = ProfileFragmentDirections.actionUpdateEmail()
            Navigation.findNavController(it).navigate(action)
        }

        text_password.setOnClickListener {
            val action = ProfileFragmentDirections.actionUpdatePassword()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun takePictureIntent() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            //image_view.setImageURI(imageUri)
            uploadImageAndSaveUri(imageUri)
        }
    }

    private fun uploadImageAndSaveUri(imageUri: Uri) {
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("images/${FirebaseAuth.getInstance().currentUser?.uid}")

        val upload = storageRef.putFile(imageUri)

        progressbar_pic.visibility = View.VISIBLE
        upload.addOnCompleteListener { uploadTask ->
            progressbar_pic.visibility = View.INVISIBLE

            if (uploadTask.isSuccessful) {
                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        this.imageUri = it
                        Log.w(TAG, "uri is $imageUri.toString()")
                        image_view.setImageURI(imageUri)
                    }
                }
            } else {
                uploadTask.exception?.let {
                    Toast.makeText(this.activity, it.message, Toast.LENGTH_SHORT)
                }
            }
        }

    }

}