package com.karunada.vanya.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.karunada.vanya.LoginActivity
import com.karunada.vanya.R
import com.karunada.vanya.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("karunada_prefs", Context.MODE_PRIVATE)

        binding.profileName.text = prefs.getString("name", "Unknown")
        binding.profilePhone.text = prefs.getString("phone", "N/A")
        binding.profileCity.text = "District: ${prefs.getString("district", "N/A")}"
        binding.profileArea.text = "Area: ${prefs.getString("area", "N/A")}"
        binding.profileRole.text = "Role: ${prefs.getString("role", "user")?.uppercase()}"

        val photoUrl = prefs.getString("photo", "")
        if (!photoUrl.isNullOrEmpty()) {
            Glide.with(this).load(photoUrl).placeholder(R.drawable.app_logo).into(binding.profilePic)
        }

        val role = prefs.getString("role", "user")
        if (role == "offline_user") {
            binding.offlineModeButton.visibility = View.GONE
        }

        binding.offlineModeButton.setOnClickListener {
            prefs.edit()
                .putString("phone", "OFFLINE")
                .putString("name", "Student / Guest")
                .putString("role", "offline_user")
                .apply()
            
            requireActivity().recreate()
        }

        binding.logoutButton.setOnClickListener {
            prefs.edit().clear().apply()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
