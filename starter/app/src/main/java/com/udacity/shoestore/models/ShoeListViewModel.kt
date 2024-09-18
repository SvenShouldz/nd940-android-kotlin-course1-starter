package com.udacity.shoestore.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShoeListViewModel: ViewModel() {

    private lateinit var _shoeList: MutableLiveData<MutableList<Shoe>>
    val shoeList: LiveData<MutableList<Shoe>>
        get() =_shoeList

    fun add(shoe: Shoe){
        shoeList.value?.add(shoe)
    }

    fun remove(shoe: Shoe){
        shoeList.value?.remove(shoe)
    }
}