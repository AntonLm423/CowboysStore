package com.example.cowboysstore.presentation.ui.map

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.FragmentMapBinding
import com.example.cowboysstore.utils.Constants
import okio.IOException
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import java.util.*

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding

    companion object {
        // Саранск, демократическая 14
        const val START_LATITUDE = 54.180501
        const val START_LONGITUDE = 45.179098
        const val START_ZOOM = 18.0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        initializeMapView()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            textViewCurrentLocation.text = getAddress(START_LATITUDE, START_LONGITUDE)

            buttonSelect.setOnClickListener {
                pickLocation()
            }

            buttonCancel.setOnClickListener {
                parentFragmentManager.popBackStack()
            }

            mapView.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        updateCurrentLocation()
                    }
                }
                false
            }
        }
    }

    /* Initializing mapview with start location and start zoom */
    private fun initializeMapView() = with(binding) {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        val mapController = mapView.controller
        mapController.setZoom(START_ZOOM)
        val startPoint = GeoPoint(START_LATITUDE, START_LONGITUDE)
        mapController.setCenter(startPoint)
        Configuration.getInstance().userAgentValue = requireContext().packageName
    }

    /* Update current location on screen or display error hint */
    private fun updateCurrentLocation() {
        val projection = binding.mapView.projection
        val center = projection.fromPixels(binding.mapView.width / 2, binding.mapView.height / 2)
        val address = getAddress(center.latitude, center.longitude)
        if (address != null) {
            binding.textViewCurrentLocation.text = address
        } else {
            Toast.makeText(requireContext(), R.string.map_geocoder_error, Toast.LENGTH_SHORT).show()
        }
    }

    /* Geocoding location */
    private fun getAddress(lat: Double, lon: Double): String? {
        return try {
            val geocoder = Geocoder(requireContext())
            val list = geocoder.getFromLocation(lat, lon, 1)
            val address = list?.get(0)
            val city = address?.locality ?: ""
            val street = address?.thoroughfare ?: ""
            val houseNumber = address?.featureName ?: ""
            return "г. $city, ул. $street, $houseNumber" // TODO: В ресурсы
        } catch (ioException: IOException) {
            null
        } catch (illegalArgumentException: IllegalArgumentException) {
            null
        }
    }

    /* Pick location and navigate to checkout fragment */
    private fun pickLocation() {
        setFragmentResult(
            Constants.LOCATION_KEY,
            bundleOf(Constants.LOCATION_BUNDLE_KEY to binding.textViewCurrentLocation.text.toString())
        )
        parentFragmentManager.popBackStack()
    }
}