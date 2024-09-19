package com.udacity.shoestore.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.CellShoeListItemBinding
import com.udacity.shoestore.databinding.FragmentShoeListBinding
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.models.ShoeListViewModel

class ShoeListFragment : Fragment() {

    private lateinit var viewModel: ShoeListViewModel
    private lateinit var linearLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentShoeListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_shoe_list, container, false
        )

        linearLayout = binding.shoeList

        viewModel = ViewModelProvider(requireActivity())[ShoeListViewModel::class.java]

        setupShoeList(viewModel.shoeList)

        binding.addShoeButton.setOnClickListener { view ->
            view.findNavController()
                .navigate(ShoeListFragmentDirections.actionShoeListFragmentToShoeDetailFragment())
        }

        return binding.root
    }

    private fun setupShoeList(shoeList: LiveData<MutableList<Shoe>>) {
        shoeList.observe(viewLifecycleOwner) { list ->

            linearLayout.removeAllViews()
            list?.let{
                for (shoe in it) {
                    val itemBinding: CellShoeListItemBinding = DataBindingUtil.inflate(
                        layoutInflater,
                        R.layout.cell_shoe_list_item,
                        linearLayout,
                        false
                    )

                    itemBinding.shoe = shoe

                    linearLayout.addView(itemBinding.root)
                }
            }

        }
    }
}