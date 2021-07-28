package com.example.blapoc.customviews

import android.hardware.SensorEventListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.blapoc.MainActivity
import com.example.blapoc.R


class CustomViewsFragment : Fragment(){

    val TAG = "AcceleroFragment"
    lateinit var mActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mActivity = activity as MainActivity
        return inflater.inflate(R.layout.fragment_customs_views, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }




}