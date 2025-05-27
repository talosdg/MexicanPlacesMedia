package com.talos.mexicanplacesrf.ui.adapters.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.talos.mexicanplacesrf.R
import com.talos.mexicanplacesrf.application.PlacesRFApp
import com.talos.mexicanplacesrf.data.PlaceRepository
import com.talos.mexicanplacesrf.databinding.FragmentGameDetailBinding
import kotlinx.coroutines.launch


private const val ARG_PLACEID = "id"

class PlaceDetailFragment : Fragment() {

    private var _binding: FragmentGameDetailBinding? = null
    private val binding get() = _binding!!

    private var placeId: String? = null

    private lateinit var repository: PlaceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            placeId = it.getString(ARG_PLACEID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        // se instancia el repositorio desde PlacesRFApp
        repository = (requireActivity().application as PlacesRFApp).repository

        lifecycleScope.launch {
            try {
                val placeDetail = repository.getPlaceDetailApiary(placeId.toString())

                binding.apply {
                    tvTitle.text = placeDetail.title
                    Glide.with(requireActivity())
                        .load(placeDetail.image)
                        .into(ivImage)
                    tvState.text = placeDetail.state
                    tvAttractions.text = placeDetail.attractions
                    tvDish.text = placeDetail.dish
                    tvLongDesc.text = placeDetail.abstract



                }

            }catch (e: Exception){
                binding.tvState.text = getString(R.string.conection_error)
                binding.tvAttractions.text = ""
                binding.tvDish.text = ""

            }finally {

                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        @JvmStatic
        fun newInstance(id: String) =
            PlaceDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PLACEID, id)
                }
            }
    }
}