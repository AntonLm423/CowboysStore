package com.example.cowboysstore.domain.entities

import android.os.Parcel
import android.os.Parcelable


data class Product(
    val badge: List<Badge> = emptyList(),
    val department: String = "",
    val description: String = "",
    val details: List<String> = emptyList(),
    val id: String = "",
    val images: List<String> = emptyList(),
    val preview: String = "",
    val price: Int = 0,
    val sizes: List<Size> = emptyList(),
    val title: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Badge)!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.createTypedArrayList(Size)!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(badge)
        parcel.writeString(department)
        parcel.writeString(description)
        parcel.writeStringList(details)
        parcel.writeString(id)
        parcel.writeStringList(images)
        parcel.writeString(preview)
        parcel.writeInt(price)
        parcel.writeTypedList(sizes)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}