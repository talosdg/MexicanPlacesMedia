package com.talos.mexicanplacesrf.ui.adapters.fragments

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.talos.mexicanplacesrf.R
import com.talos.mexicanplacesrf.application.PlacesRFApp
import com.talos.mexicanplacesrf.data.PlaceRepository
import com.talos.mexicanplacesrf.databinding.FragmentPlacesListBinding
import com.talos.mexicanplacesrf.ui.adapters.PlacesAdapter
import kotlinx.coroutines.launch

class PlacesListFragment : Fragment() {
    private var _binding: FragmentPlacesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var repository: PlaceRepository

    private var wasPlayingBeforeNavigate = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPlacesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btReload.setOnClickListener {
            binding.tvConectionError.visibility = View.INVISIBLE
            binding.ivLele.visibility = View.INVISIBLE
            binding.btReload.visibility = View.INVISIBLE
            loadData()
        }
        loadData()

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.jarabe)
        mediaPlayer.start()
        binding.tbPause.isChecked = true

        binding.tbPause.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mediaPlayer.start()
            } else {
                mediaPlayer.pause()
            }
        }

        mediaPlayer.setOnCompletionListener {
            binding.tbPause.isChecked = false
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        _binding = null
    }
    override fun onDestroyView() {
        super.onDestroyView()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        if (::mediaPlayer.isInitialized && wasPlayingBeforeNavigate) {
            mediaPlayer.start()
            binding.tbPause.isChecked = true
            wasPlayingBeforeNavigate = false
        }
    }
    override fun onPause() {
        super.onPause()
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            binding.tbPause.isChecked = false
        }
    }

    private fun loadData(){
        // se instancia el repositorio desde PlacesRFApp
        repository = (requireActivity().application as PlacesRFApp).repository

        lifecycleScope.launch {
            try {
                val places = repository.getPlacesApiary()

                binding.rvGames.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = PlacesAdapter(places) { selectedPlace ->

                        wasPlayingBeforeNavigate = mediaPlayer.isPlaying

                        binding.tvConectionError.visibility = View.INVISIBLE
                        binding.ivLele.visibility = View.INVISIBLE
                        binding.btReload.visibility = View.INVISIBLE

                        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                            mediaPlayer.pause()
                            binding.tbPause.isChecked = false
                        }
                        selectedPlace.id?.let { id ->

                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.fragment_container,
                                    PlaceDetailFragment.newInstance(id)
                                )
                                .addToBackStack(null) // a la pila de navegación
                                .commit()
                        }
                    }

                }

            }catch (e: Exception){
                binding.tvConectionError.visibility = View.VISIBLE
                binding.ivLele.visibility = View.VISIBLE
                binding.btReload.visibility = View.VISIBLE

            } finally {
                binding.pbLoading.visibility = View.GONE

            }
        }
    }

}