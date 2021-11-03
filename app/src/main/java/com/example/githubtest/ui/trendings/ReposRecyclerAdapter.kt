package com.example.githubtest.ui.trendings

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.githubtest.domain.Repo
import com.example.githubtest.BR
import com.example.githubtest.R

class ReposRecyclerAdapter(val repos: MutableList<Repo>)
    : RecyclerView.Adapter<DataBindingViewHolder>() {
    var onRepoItemClickListener: ((Repo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            DataBindingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.row_repo, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun getItemCount(): Int = repos.size

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        holder.bindVariable(BR.repo, repos[position])
        holder.itemView.setOnClickListener { _ ->
            onRepoItemClickListener?.let { it(repos[holder.adapterPosition]) }
        }
    }

}