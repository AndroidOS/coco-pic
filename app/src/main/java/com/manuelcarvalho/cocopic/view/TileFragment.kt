package com.manuelcarvalho.cocopic.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
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
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_tile, container, false)
        return activity?.let { TileCanvas(it.applicationContext) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this)[AppViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        viewModel.dispMenuDraw.value = false

        ActivityCompat.invalidateOptionsMenu(activity)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        Log.d(TAG, "Tile onPrepareOptionsMenu")
        menu.getItem(7).isEnabled = false
        menu.getItem(8).isEnabled = false
    }


}