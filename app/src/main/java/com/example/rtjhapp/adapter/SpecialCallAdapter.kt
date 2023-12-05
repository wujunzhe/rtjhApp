package com.example.rtjhapp.adapter

import android.content.Context
import com.example.rtjhapp.databinding.SpecialCallDialogBinding
import com.example.rtjhapp.dialog.AddSpecialCallDialog
import com.example.rtjhapp.model.PhoneItem
import com.example.rtjhapp.utils.SharedPreferencesManager
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter

interface AsymmetricItem {
    fun getColumnSpan(): Int
    fun getRowSpan(): Int
}
class SpecialCallAdapter(
    private val binding : SpecialCallDialogBinding,
    private val specialCallItems: List<PhoneItem>,
    private val asymmetricGridView: AsymmetricGridView){
    private val addBtn = binding.addSpecialCallBtn
    private val listView = binding.specialCallListView
    init {
        setBtnOnClickListener()
    }

    private fun setBtnOnClickListener() {
        addBtn.setOnClickListener {
            val addDialog = AddSpecialCallDialog(binding.root.context)
            addDialog.show()
        }
    }
}