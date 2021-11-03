package com.example.githubtest.ui.trendings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubtest.data.Error
import com.example.githubtest.databinding.FragmentTrendingReposBinding
import com.example.githubtest.domain.Repo
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class TrendingReposFragment : Fragment() {

    var inject = { AndroidSupportInjection.inject(this) }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val trendingReposVM: TrendingReposViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TrendingReposViewModel::class.java]
    }

    private var loadMore = true
    private var refresh = true
    private var reposAdapter: ReposRecyclerAdapter? = null

    lateinit var binding: FragmentTrendingReposBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTrendingReposBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        inject()
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trendingReposVM.reposLiveData.observe(this, Observer(::onReceivedData))
        trendingReposVM.errorsLiveData.observe(this, Observer(::onReceivedError))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (reposAdapter == null) {
            reposAdapter = ReposRecyclerAdapter(mutableListOf())
            reposAdapter?.onRepoItemClickListener = ::onRepoClicked
            loadRepos(true)
        }

        binding.recyclerRepos.adapter = reposAdapter
        binding.recyclerRepos.layoutManager = LinearLayoutManager(context)
        binding.swipeRepos.setOnRefreshListener {
            loadRepos(true)
        }

        binding.recyclerRepos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager
                layoutManager?.let {
                    val last = (it as LinearLayoutManager).findLastVisibleItemPosition()
                    val count = recyclerView.adapter?.itemCount ?: 0
                    if (loadMore && !binding.swipeRepos.isRefreshing && last == count - 1) {
                        loadRepos(false)
                    }
                }
            }
        })
    }

    private fun loadRepos(reset: Boolean) {
        binding.swipeRepos.isRefreshing = true
        if (reset)
            refresh = true
        trendingReposVM.loadTrendingRepos(reset)
    }

    private fun onReceivedData(data: List<Repo>) {
        binding.swipeRepos.isRefreshing = false
        if (refresh) {
            reposAdapter!!.repos.clear()
            refresh = false
        }

        if(data.isNotEmpty()){
            reposAdapter!!.repos.addAll(data)
            binding.recyclerRepos.adapter?.notifyDataSetChanged()
        }else{
            loadMore = false
        }
    }

    private fun onReceivedError(err: Error){

    }

    private fun onRepoClicked(repo: Repo) {
        findNavController().navigate(
            TrendingReposFragmentDirections.actionTrendingReposFragmentToRepoDetailsFragment(repo)
        )
    }
}