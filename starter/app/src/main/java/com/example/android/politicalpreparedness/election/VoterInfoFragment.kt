package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        val binding = FragmentVoterInfoBinding.inflate(inflater)

        val selectedElection = VoterInfoFragmentArgs.fromBundle(requireArguments()).selectedElection
        val viewModelFactory = VoterInfoViewModel.Factory(selectedElection, application)


        binding.voterInfoViewModel = ViewModelProvider(
                this, viewModelFactory).get(VoterInfoViewModel::class.java)
        binding.lifecycleOwner = this


        return binding.root
    }

}