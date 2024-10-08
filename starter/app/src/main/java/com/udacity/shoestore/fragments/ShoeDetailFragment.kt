package com.udacity.shoestore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoeDetailBinding
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.models.ShoeListViewModel

class ShoeDetailFragment : Fragment() {

    private lateinit var viewModel: ShoeListViewModel
    private var shoe: Shoe = Shoe()
    private lateinit var menuProvider: MenuProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentShoeDetailBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_shoe_detail, container, false
        )

        // get Safe Args
        val args: ShoeDetailFragmentArgs by navArgs()
        val savedShoe: Shoe = args.shoe

        //apply savedShoe to binding
        shoe = savedShoe
        setupAppBarConfig(shoe)
        binding.shoe = shoe

        viewModel = ViewModelProvider(requireActivity())[ShoeListViewModel::class.java]

        // save Shoe to ViewModel
        binding.saveButton.setOnClickListener { view ->
            shoe.let {
                // Check if any fields are empty
                if (shoe.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.empty_field),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                // Check if the size is greater than or equal to 60
                if (it.size >= 60) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.invalid_size),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                // add new Shoe and navigate back
                viewModel.addOrUpdate(it)
                view.findNavController().navigateUp()
            }
        }

        binding.cancelButton.setOnClickListener { view ->
            view.findNavController()
                .navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).removeMenuProvider(menuProvider)
    }

    private fun setupAppBarConfig(savedShoe: Shoe) {
        // init new menuProvider
        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear() // Clear old menu items
                menuInflater.inflate(R.menu.menu_shoe_detail, menu)
            }

            // delete shoe and navigate back
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_delete_shoe -> {
                        shoe.let { viewModel.remove(it) }
                        view?.findNavController()
                            ?.navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
                        true
                    }

                    else -> false
                }
            }
        }
        // change title when open existing shoeDetail
        (activity as AppCompatActivity).apply {
            supportActionBar?.show()
            if (shoe.isFresh()) {
                supportActionBar?.title = getString(R.string.new_shoe)
            } else {
                supportActionBar?.title = "${shoe.company} - ${shoe.name}"
                addMenuProvider(menuProvider, viewLifecycleOwner)
            }
        }
    }
}