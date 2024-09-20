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
    private lateinit var shoe: Shoe

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
        // TODO error while saving edited shoe
        binding.saveButton.setOnClickListener { view ->
            val name = binding.editName.text.toString()
            val company = binding.editCompany.text.toString()
            val sizeString = binding.editSize.text.toString()
            val description = binding.editDescription.text.toString()

            // check if textEdits are empty
            if (name.isEmpty() || company.isEmpty() || sizeString.isEmpty() || description.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.empty_field), Toast.LENGTH_SHORT)
                    .show()
            }

            // check if size is bigger than 60
            val size: Double = if (sizeString.toInt() >= 60) {
                Toast.makeText(requireContext(), getString(R.string.invalid_size), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {
                sizeString.toDouble()
            }

            // create new Shoe Object
            val newShoe = Shoe(
                name = name,
                company = company,
                size = size,
                description = description
            )

            // add new Shoe and navigate back
            viewModel.add(newShoe)
            view.findNavController().navigateUp()
        }

        binding.cancelButton.setOnClickListener { view ->
            view.findNavController()
                .navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
        }

        return binding.root
    }

    //TODO delete Button is not shown yet
    private fun setupAppBarConfig(savedShoe: Shoe?) {
        // init new menuProvider
        val menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear() // Clear old menu items
                menuInflater.inflate(R.menu.menu_shoe_list, menu)
            }
            // delete shoe and navigate back
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_delete_shoe -> {
                        viewModel.remove(shoe)
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
        val shoe = args.shoe
        shoe?.let {
            binding.editName.setText(shoe.name)
            binding.editCompany.setText(shoe.company)
            binding.editSize.setText(shoe.size.toString())
            binding.editDescription.setText(shoe.description)
        }
    }

}