package com.example.rtjhapp.adapter

import android.os.Parcel
import android.os.Parcelable
import com.example.rtjhapp.model.PhoneItem
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem

class AsymmetricPhoneItemAdapter(private val phoneItem: PhoneItem) : AsymmetricItem, Parcelable {

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
        parcel.writeString(phoneItem.departmentName)
        parcel.writeString(phoneItem.phoneNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AsymmetricPhoneItemAdapter> {
        override fun createFromParcel(parcel: Parcel): AsymmetricPhoneItemAdapter {
            val departmentName = parcel.readString() ?: ""
            val phoneNumber = parcel.readString() ?: ""
            return AsymmetricPhoneItemAdapter(PhoneItem(departmentName, phoneNumber))
        }

        override fun newArray(size: Int): Array<AsymmetricPhoneItemAdapter?> {
            return arrayOfNulls(size)
        }
    }
}
