package com.manuelcarvalho.cocopic.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.fragment.app.Fragment
import com.manuelcarvalho.cocopic.viewmodel.AppViewModel


private const val TAG = "TileFragment"
class TileFragment : Fragment() {

    private lateinit var viewModel: AppViewModel
    private lateinit var view1: TileCanvas

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return activity?.let { TileCanvas(it.applicationContext) }!!

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        invalidateOptionsMenu(activity)
    }


}