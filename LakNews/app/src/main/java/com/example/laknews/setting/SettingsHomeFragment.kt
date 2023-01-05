package com.example.laknews.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.laknews.R

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsHomeFragment : Fragment() {

    class HomeFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_home, container, false)
        }


    }

}