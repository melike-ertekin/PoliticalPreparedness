package com.example.android.politicalpreparedness.representative


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.domain.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class RepresentativeFragment : Fragment() {

    private lateinit var binding: FragmentRepresentativeBinding
    private val viewModel by viewModels<RepresentativeViewModel>()

    private val FINE_LOCATION_ACCESS_REQUEST_CODE = 1
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //Data binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        binding.representativeViewModel = viewModel
        binding.myRepresentativeRecycler.adapter = RepresentativeListAdapter()
        binding.address = Address("", "", "", "", "")


        binding.buttonLocation.setOnClickListener {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            getLocation()
        }

        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                Log.d("spinner selected", p0?.selectedItem.toString())
                binding.hiddenState.text = p0?.selectedItem.toString()
            }
        }

        return binding.root
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check if location permissions are granted and if so enable the location

        when (requestCode) {
            FINE_LOCATION_ACCESS_REQUEST_CODE -> {

                if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue...
                    getLocation()

                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast.makeText(context, "Location permission was not granted.", Toast.LENGTH_LONG).show()
                }

            }

        }

    }


    private fun getLocation() {

        when {
            (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) -> {

                // You can use the API that requires the permission.

                fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        lastLocation = location

                        val address = geoCodeLocation(lastLocation)
                        binding.address = address
                        val stateText = address.state

                        if (stateText != "") {
                            val statesForSpinner = requireContext().resources.getStringArray(R.array.states)
                            var statesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statesForSpinner)
                            val spinnerPosition: Int = statesAdapter.getPosition(stateText)
                            binding.state.setSelection(spinnerPosition)
                        }

                    }
                }


                Toast.makeText(context, "Location permission is granted.", Toast.LENGTH_LONG).show()
            }
            (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) -> {
                // Explain why you need the permission
                // Add dialog
                requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        FINE_LOCATION_ACCESS_REQUEST_CODE
                )
            }

            else ->
                //Request permission
                requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        FINE_LOCATION_ACCESS_REQUEST_CODE
                )
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}
