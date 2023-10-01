package com.panther.contentai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.panther.contentai.databinding.FragmentEmailAuthScreenBinding


class EmailAuthScreen : Fragment() {
lateinit var emailBinding:FragmentEmailAuthScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        emailBinding = FragmentEmailAuthScreenBinding.inflate(layoutInflater)
        val view = emailBinding.root

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return emailBinding.root
    }


    }
