package com.udacity.shoestore.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShoeListViewModel : ViewModel() {


    private val _shoeList = MutableLiveData<MutableList<Shoe>>(mutableListOf())
    val shoeList: LiveData<MutableList<Shoe>>
        get() = _shoeList

    fun addOrUpdate(shoe: Shoe) {
        _shoeList.value?.let { shoes ->
            // check if the shoe already exists
            val existingShoeIndex = shoes.indexOfFirst { it.id == shoe.id }

            if (existingShoeIndex != -1) {
                // if shoe exists, update it
                shoes[existingShoeIndex] = shoe
            } else {
                // if shoe doesn't exist, add it
                shoes.add(shoe)
            }

            _shoeList.value = shoes
        }
    }

    fun remove(shoe: Shoe) {
        _shoeList.value?.let { shoes ->
            shoes.remove(shoe)
            _shoeList.value = shoes
        }
    }
}