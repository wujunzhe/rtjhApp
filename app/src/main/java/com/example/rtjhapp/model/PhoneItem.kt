package com.example.rtjhapp.model

import android.os.Parcel
import android.os.Parcelable
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem

data class PhoneItem(val departmentName: String, val phoneNumber: String) : AsymmetricItem,
    Parcelable {

    override fun getColumnSpan(): Int {
        // 定义此项应跨越的列数
        // 根据你的布局要求进行自定义
        return 1
    }

    override fun getRowSpan(): Int {
        // 定义此项应跨越的行数
        // 根据你的布局要求进行自定义
        return 1
    }

    // 实现 Parcelable 接口的成员

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(departmentName)
        parcel.writeString(phoneNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhoneItem> {
        override fun createFromParcel(parcel: Parcel): PhoneItem {
            return PhoneItem(parcel)
        }

        override fun newArray(size: Int): Array<PhoneItem?> {
            return arrayOfNulls(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )
}