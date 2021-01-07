package com.manuelcarvalho.cocopic.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.manuelcarvalho.cocopic.viewmodel.AppViewModel


private const val TAG = "TileFragment"
class TileFragment : Fragment() {

    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProviders.of(this)[AppViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_tile, container, false)
        return activity?.let { TileCanvas(it.applicationContext, viewModel) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        viewModel.dispMenuDraw.value = false
//        Log.d(TAG, "dispMenuDraw.value = false")
//
        invalidateOptionsMenu(activity)
    }

//    override fun onPrepareOptionsMenu(menu: Menu) {
//        super.onPrepareOptionsMenu(menu)
//        Log.d(TAG, "Tile onPrepareOptionsMenu")
//        menu.getItem(7).isEnabled = false
//        menu.getItem(8).isEnabled = false
//    }


}