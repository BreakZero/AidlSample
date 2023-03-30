package com.easy.aidlserver

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String? = "",
    var age: Int = 0,
    var byteArray: ByteArray = ByteArray(1)
): Parcelable {
    fun readFromParcel(reply: Parcel) {
        this.name = reply.readString().toString()
        this.age = reply.readInt()
        this.byteArray = reply.marshall()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (name != other.name) return false
        if (age != other.age) return false
        if (!byteArray.contentEquals(other.byteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + age
        result = 31 * result + byteArray.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "Age: $age, Name: $name"
    }
}