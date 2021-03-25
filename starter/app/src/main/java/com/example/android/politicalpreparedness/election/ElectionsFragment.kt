package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.ElectionsFragmentDirections.actionShowDetail
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment: Fragment() {

    private lateinit var binding: FragmentElectionBinding

    /**
     * Lazily initialize our [ElectionsViewModel].
     */

    private val viewModel: ElectionsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, ElectionsViewModel.Factory(activity.application)).get(
                ElectionsViewModel::class.java
        )
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        //Data binding
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        binding.electionListViewModel = viewModel

        binding.upcomingElectionRecycler.adapter = ElectionListAdapter(ElectionListAdapter.OnClickListener{
            viewModel.displayElectionDetails(it)
        })

        /*
        * Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        * After navigating, call displayAsteroidDetailsComplete() so that the ViewModel is ready
        * for another navigation event.
        * */
        viewModel.navigateToSelectedElection.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                // Must find the NavController from the Fragment
                this.findNavController()
                        .navigate(ElectionsFragmentDirections.actionShowDetail(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayElectionDetailsComplete()
            }
        })

        binding.savedElectionRecycler.adapter = ElectionListAdapter(ElectionListAdapter.OnClickListener{
            viewModel.displayElectionDetails(it)
        })

        return binding.root
    }



}