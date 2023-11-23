package com.example.rtjhapp.debugModule

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rtjhapp.R
import com.example.rtjhapp.adapter.DebugListAdapter
import com.example.rtjhapp.databinding.DebugListBinding
import com.example.rtjhapp.event.UpdateDebugMessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class DebugFragment : Fragment() {

    private lateinit var binding: DebugListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DebugListAdapter // 假设有一个 DebugListAdapter

    // 假设你的调试信息数据列表
    private val debugDataList = mutableListOf<String>()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.debug_list, container, false)
        recyclerView = binding.debugListView

        // 设置 RecyclerView 的布局管理器和适配器
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = DebugListAdapter(debugDataList)
        recyclerView.adapter = adapter

        return binding.root
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
    @Subscribe
    fun onDebugMessageEvent(event:UpdateDebugMessageEvent){
        debugDataList.add(0,event.debugMsg)

        adapter.notifyItemInserted(0)
    }
}
