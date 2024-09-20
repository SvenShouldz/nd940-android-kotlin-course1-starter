package com.udacity.shoestore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.CellShoeListItemBinding
import com.udacity.shoestore.databinding.FragmentShoeListBinding
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.models.ShoeListViewModel

class ShoeListFragment : Fragment() {

    private val viewModel: ShoeListViewModel by activityViewModels()
    private lateinit var linearLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentShoeListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_shoe_list, container, false
        )

        val menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear() // Clear old menu items
                menuInflater.inflate(R.menu.menu_shoe_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_logout -> {
                        view?.findNavController()
                            ?.navigate(ShoeListFragmentDirections.actionShoeListFragmentToLoginFragment())
                        true
                    }

                    else -> false
                }
            }
        }

        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.your_shoes)
        (activity as AppCompatActivity).addMenuProvider(menuProvider, viewLifecycleOwner)

        linearLayout = binding.shoeList

        setupShoeList(viewModel.shoeList)

        binding.addShoeButton.setOnClickListener { view ->
            view.findNavController()
                .navigate(ShoeListFragmentDirections.actionShoeListFragmentToShoeDetailFragment(null))
        }

        return binding.root
    }

    private fun setupShoeList(shoeList: LiveData<MutableList<Shoe>>) {
        shoeList.observe(viewLifecycleOwner) { list ->

            linearLayout.removeAllViews()
            list?.let {
                for (shoe in it) {
                    val itemBinding: CellShoeListItemBinding = DataBindingUtil.inflate(
                        layoutInflater,
                        R.layout.cell_shoe_list_item,
                        linearLayout,
                        false
                    )

                    itemBinding.shoe = shoe

                    itemBinding.root.setOnClickListener {
                        // Navigate to the ShoeDetailFragment using Safe Args
                        val action = ShoeListFragmentDirections
                            .actionShoeListFragmentToShoeDetailFragment(shoe)
                        findNavController().navigate(action)
                    }

                    linearLayout.addView(itemBinding.root)
                }
            }
        }
    }
}