package com.example.githubtest.ui.repo

import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.githubtest.R
import com.example.githubtest.databinding.FragmentRepoDetailsBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class RepoDetailsFragment : Fragment() {

    lateinit var binding: FragmentRepoDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRepoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val bundle = arguments
        if (bundle == null) {
            findNavController().navigateUp()
            return
        }

        val repo = RepoDetailsFragmentArgs.fromBundle(bundle).data

        Picasso
            .get()
            .load(Uri.parse(repo.owner.avatar))
            .placeholder(R.drawable.ic_octoface)
            .error(R.drawable.ic_octoface)
            .centerCrop()
            .fit()
            .into(binding.imageRepoOwner)

        binding.textRepoTitle.text = repo.name
        binding.textAuthor.text = "@${repo.owner.username}"
        binding.textRepoDescription.text = repo.repoDescription
        binding.textStars.text = repo.stars.toString()
        binding.textForks.text = repo.forks.toString()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val updatedAt: Date = dateFormat.parse(repo.updatedAt)

        val text = DateUtils.getRelativeDateTimeString(
            context, updatedAt.time, DateUtils.SECOND_IN_MILLIS,
            DateUtils.YEAR_IN_MILLIS, 0
        )

        binding.textUpdatedAt.text = "${getString(R.string.prefix_updated)} $text"
    }
}