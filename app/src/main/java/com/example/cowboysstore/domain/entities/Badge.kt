package com.example.cowboysstore.domain.entities

import android.os.Parcel
import android.os.Parcelable

data class Badge(
    val color: String,
    val value: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(color)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Badge> {
        override fun createFromParcel(parcel: Parcel): Badge {
            return Badge(parcel)
        }

        override fun newArray(size: Int): Array<Badge?> {
            return arrayOfNulls(size)
        }
    }
}