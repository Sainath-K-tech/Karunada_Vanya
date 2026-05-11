package com.karunada.vanya.ui.sounds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.karunada.vanya.data.SoundData
import com.karunada.vanya.databinding.FragmentSoundsBinding

class SoundsFragment : Fragment() {

    private var _binding: FragmentSoundsBinding? = null
    private val binding get() = _binding!!
    private var soundsAdapter: SoundsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSoundsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sounds = SoundData.getSounds()
        soundsAdapter = SoundsAdapter(sounds, requireContext())
        binding.soundsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.soundsRecyclerView.adapter = soundsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        soundsAdapter?.releasePlayer()
        _binding = null
    }
}