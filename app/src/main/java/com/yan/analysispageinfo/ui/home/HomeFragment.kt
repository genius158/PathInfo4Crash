package com.yan.analysispageinfo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yan.analysispageinfo.R
import com.yan.analysispageinfo.ui.home.ui.login.LoginFragment

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val button: Button = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            button.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.text_home).setOnClickListener {
            childFragmentManager.beginTransaction().add(R.id.container, LoginFragment())
                .commitAllowingStateLoss()
        }

        val rvdata = view.findViewById<RecyclerView>(R.id.rvData)
        rvdata.layoutManager = LinearLayoutManager(view.context)
        rvdata.adapter = Adapter().apply {
            loadMoreModule.isEnableLoadMore = true
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                
            }

        }
    }

    inner class Adapter : BaseQuickAdapter<Int, BaseViewHolder>(R.layout.test,
        arrayListOf(1,23,34,5,5)),
        LoadMoreModule {
        override fun convert(holder: BaseViewHolder, item: Int) {
        }
    }
}