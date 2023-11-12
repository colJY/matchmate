package com.lee.matchmate.main.add

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lee.matchmate.R

class AddSpaceFragment : Fragment() {

    companion object {
        fun newInstance() = AddSpaceFragment()
    }

    private lateinit var viewModel: AddSpaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_space, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddSpaceViewModel::class.java)
        // TODO: Use the ViewModel
    }

}