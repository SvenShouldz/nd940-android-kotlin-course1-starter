package com.udacity.shoestore.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class Shoe(val id: UUID = UUID.randomUUID(), var name: String = "", var size: Double = 0.0, var company: String = "", var description: String = "",
                val images: List<String> = mutableListOf()) : Parcelable {
    var sizeString: String
        get() = if (size == 0.0) "" else size.toString() //return empty string if size is 0.0
        set(value) {
            size = value.toDoubleOrNull() ?: 0.0
        }

    // Property to check if the shoe object is "fresh" or empty
    val isFresh: Boolean
        get() = name.isEmpty() && size == 0.0 && company.isEmpty() && description.isEmpty()
}