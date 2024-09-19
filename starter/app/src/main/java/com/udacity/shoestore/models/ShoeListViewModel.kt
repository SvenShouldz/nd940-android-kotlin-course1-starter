package com.udacity.shoestore.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class ShoeListViewModel: ViewModel() {


    private val _shoeList = MutableLiveData<MutableList<Shoe>>(mutableListOf())
    val shoeList: LiveData<MutableList<Shoe>>
        get() =_shoeList

    fun add(shoe: Shoe){
        _shoeList.value?.let { shoes ->
            shoes.add(shoe)
            _shoeList.value = shoes // Trigger LiveData update
        }
    }

    fun remove(shoe: Shoe){
        _shoeList.value?.let { shoes ->
            shoes.remove(shoe)
            _shoeList.value = shoes
        }
    }
}