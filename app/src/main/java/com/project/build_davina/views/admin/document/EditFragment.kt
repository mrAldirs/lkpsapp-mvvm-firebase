package com.project.build_davina.views.admin.document

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.project.build_davina.databinding.FragmentAdmDocFormBinding

class EditFragment : DialogFragment() {
    private lateinit var binding: FragmentAdmDocFormBinding
    private lateinit var v: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdmDocFormBinding.inflate(inflater, container, false)
        v = binding.root

        return v
    }
}