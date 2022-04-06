package com.nekhaev.android.smartled.presentation.control_panel

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nekhaev.android.smartled.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ControlPanelFragment : Fragment(R.layout.fragment_control_panel) {

    private lateinit var mBtnUp: Button
    private lateinit var mBtnDown: Button

    private val mViewModel: ControlPanelViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi(view)
    }

    private fun setupUi(view: View) {
        mBtnDown = view.findViewById(R.id.controlPanel_btnDown)
        mBtnUp = view.findViewById(R.id.controlPanel_btnUp)

        mBtnDown.setOnClickListener {
            mViewModel.onButtonDownClick()
        }

        mBtnUp.setOnClickListener {
            mViewModel.onButtonUpClick()
        }
    }
}