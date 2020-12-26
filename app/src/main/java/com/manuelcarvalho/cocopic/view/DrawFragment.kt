package com.manuelcarvalho.cocopic.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class DrawFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var view = activity?.let { TileCanvas(it.applicationContext) }

    }
//override fun onCreateView(
//    inflater: LayoutInflater, container: ViewGroup?,
//    savedInstanceState: Bundle?
//): View? {
//    // Inflate the layout for this fragment
//    return inflater.inflate(R.layout.fragment_first, container, false)
//}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_draw, container, false)
        return activity?.let { TileCanvas(it.applicationContext) }
    }


}