package com.easy.aidlserver

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String? = "",
    var age: Int = 0
): Parcelable {
    fun readFromParcel(reply: Parcel) {
        this.name = reply.readString().toString()
        this.age = reply.readInt()
    }
}
