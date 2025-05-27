package com.talos.mexicanplacesrf.ui.adapters.fragments

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

    private lateinit var repository: PlaceRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPlacesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    // cuando el usuario ya esta viendo el fragmento
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // se instancia el repositorio desde PlacesRFApp
        repository = (requireActivity().application as PlacesRFApp).repository

        lifecycleScope.launch {
            try {
                val places = repository.getPlacesApiary()

                binding.rvGames.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = PlacesAdapter(places) { selectedPlace ->

                        binding.tvConectionError.visibility = View.INVISIBLE
                        binding.ivLele.visibility = View.INVISIBLE
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
            } finally {
                binding.pbLoading.visibility = View.GONE

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}