package com.udacity.shoestore.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentLoginBinding
import com.udacity.shoestore.databinding.FragmentShoeDetailBinding
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.models.ShoeListViewModel
import timber.log.Timber

class ShoeDetailFragment : Fragment() {

    private lateinit var viewModel: ShoeListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentShoeDetailBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_shoe_detail, container, false)

        (activity as AppCompatActivity).supportActionBar?.show()

        viewModel = ViewModelProvider(requireActivity())[ShoeListViewModel::class.java]

        binding.saveButton.setOnClickListener{ view ->
            val newShoe = Shoe(
                name = binding.editName.text.toString(),
                company = binding.editCompany.text.toString(),
                size = binding.editSize.text.toString().toDouble(),
                description = binding.editDescription.text.toString()
            )

            viewModel.add(newShoe)

            view.findNavController().navigateUp()
        }

        binding.cancelButton.setOnClickListener{ view ->
            view.findNavController().navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
        }

        return binding.root
    }
}