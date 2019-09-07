package com.example.pcremote.ui.connection_status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_connection_status.*

class ConnectionStatusFragment: Fragment() {

    private var viewModel: MainViewModel? = null

    companion object {
        fun newInstance(): ConnectionStatusFragment {
            return ConnectionStatusFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_connection_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { viewModel = ViewModelProviders.of(it).get(MainViewModel::class.java) }

        rootLayout.setOnClickListener {

        }
    }
}