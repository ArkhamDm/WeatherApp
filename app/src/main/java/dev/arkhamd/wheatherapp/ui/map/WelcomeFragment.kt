package dev.arkhamd.wheatherapp.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dev.arkhamd.wheatherapp.R
import dev.arkhamd.wheatherapp.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater)

        binding.typewriterTextView.setCharacterDelay(100)
        binding.button.setOnClickListener {
            it.findNavController().navigate(R.id.action_welcomeFragment_to_mapFragment)
        }

        return binding.root
    }

    companion object {
        fun newInstance() = WelcomeFragment()
    }
}