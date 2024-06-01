package com.example.finalprojectbinar.view.fragments.bottomsheets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finalprojectbinar.databinding.FragmentBottomSheetSuccessRegisterBinding
import com.example.finalprojectbinar.util.Enum
import com.example.finalprojectbinar.util.SharedPreferenceHelper
import com.example.finalprojectbinar.view.ui.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetSuccessRegisterFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetSuccessRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetSuccessRegisterBinding.inflate(inflater, container, false)
        SharedPreferenceHelper.init(requireContext()) // Initialize SharedPreferenceHelper

        binding.btnToLoginActivity.setOnClickListener {
            navigateToLogin()
        }

        binding.imageClose.setOnClickListener {
            navigateToLogin()
        }

        return binding.root
    }

    private fun navigateToLogin() {
        SharedPreferenceHelper.write(Enum.PREF_REGISTER.value, null)
        dismiss()
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // Finish the current activity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
