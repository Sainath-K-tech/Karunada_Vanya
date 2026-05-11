package com.karunada.vanya.ui.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.karunada.vanya.AdminAddWikiActivity
import com.karunada.vanya.ManageUsersActivity
import com.karunada.vanya.databinding.FragmentAdminBinding

class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("karunada_prefs", Context.MODE_PRIVATE)
        val role = prefs.getString("role", "user")

        if (role == "main_admin") {
            binding.btnManageUsers.setOnClickListener {
                startActivity(Intent(requireContext(), ManageUsersActivity::class.java))
            }
            
            binding.btnAddWildlifeCard.setOnClickListener {
                startActivity(Intent(requireContext(), AdminAddWikiActivity::class.java))
            }
        } else {
            binding.btnManageUsers.visibility = View.GONE
            binding.btnAddWildlifeCard.visibility = View.GONE
            Toast.makeText(requireContext(), "Access Denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}