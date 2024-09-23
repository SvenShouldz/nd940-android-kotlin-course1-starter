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
    private var shoe: Shoe? = null
    private lateinit var menuProvider: MenuProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentShoeDetailBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_shoe_detail, container, false
        )

        // get Safe Args & add to bindings
        val args: ShoeDetailFragmentArgs by navArgs()
        setupNavArguments(args, binding)

        // set ActionBar Title & Icon
        val savedShoe: Shoe? = args.shoe
        setupAppBarConfig(savedShoe)

        viewModel = ViewModelProvider(requireActivity())[ShoeListViewModel::class.java]

        // save Shoe to ViewModel
        binding.saveButton.setOnClickListener { view ->
            val name = binding.editName.text.toString()
            val company = binding.editCompany.text.toString()
            val sizeString = binding.editSize.text.toString()
            val description = binding.editDescription.text.toString()

            val size: Double
            try {
                size = sizeString.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.empty_field),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Check if size is greater than 60
            if (size >= 60) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.invalid_size),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // create new Shoe Object
            val shoe = args.shoe?.copy( // args.shoe is passed from the navigation
                name = name,
                company = company,
                size = size,
                description = description
            ) ?: Shoe(  // If it's a new shoe
                name = name,
                company = company,
                size = size,
                description = description
            )

            // check if textEdits are empty
            if (name.isEmpty() || company.isEmpty() || sizeString.isEmpty() || description.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.empty_field),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                // add new Shoe and navigate back
                viewModel.addOrUpdate(shoe)
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

    private fun setupAppBarConfig(savedShoe: Shoe?) {

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
                        shoe?.let { viewModel.remove(it) }
                        view?.findNavController()
                            ?.navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
                        true
                    }

                    else -> false
                }
            }
        }

        (activity as AppCompatActivity).supportActionBar?.show()

        // change title when open existing shoeDetail
        if (savedShoe != null) {
            (activity as AppCompatActivity).supportActionBar?.title =
                "${savedShoe.company} - ${savedShoe.name}"
            (activity as AppCompatActivity).addMenuProvider(menuProvider, viewLifecycleOwner)

            shoe = savedShoe
        } else {
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.new_shoe)
        }
    }

    // add navArgs to binding
    private fun setupNavArguments(
        args: ShoeDetailFragmentArgs,
        binding: FragmentShoeDetailBinding
    ) {
        shoe = args.shoe
        shoe?.let {
            it.name.let { name -> binding.editName.setText(name) }
            it.company.let { company -> binding.editCompany.setText(company) }
            it.size.let { size -> binding.editSize.setText(size.toString()) }
            it.description.let { description -> binding.editDescription.setText(description) }
        }
    }

}