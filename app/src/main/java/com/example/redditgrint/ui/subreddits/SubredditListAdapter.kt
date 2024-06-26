package com.example.redditgrint.ui.subreddits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.redditgrint.R
import com.example.redditgrint.api.RedditPost
import com.example.redditgrint.databinding.RowSubredditBinding
import com.example.redditgrint.ui.MainViewModel

class SubredditListAdapter(private val viewModel: MainViewModel,
                           private val fragmentActivity: FragmentActivity
)
: ListAdapter<RedditPost, SubredditListAdapter.VH>(SubredditListAdapter.RedditDiff()) {

    // ViewHolder pattern
    inner class VH( rowSubredditBinding: RowSubredditBinding)
        : RecyclerView.ViewHolder(rowSubredditBinding.root)
    {

        // XXX Write me.
        // NB: This one-liner will exit the current fragment
        // fragmentActivity.supportFragmentManager.popBackStack()

        val subRowPic = rowSubredditBinding.subRowPic
        val subRowHeading = rowSubredditBinding.subRowHeading
        val subRowDetails = rowSubredditBinding.subRowDetails

        init {
            rowSubredditBinding.root.setOnClickListener {
                viewModel.setSearchTerm(subRowHeading.text.toString())
                viewModel.setTitle("r/${subRowHeading.text.toString()}")
                fragmentActivity.supportFragmentManager.popBackStack()
            }
        }

        fun bind(post : RedditPost)
        {
            viewModel.netFetchImage(post.iconURL, R.drawable.ic_cloud_download_black_24dp, subRowPic)
            subRowHeading.text = post.displayName
            subRowDetails.text = post.publicDescription
        }

    }

    //EEE // XXX Write me

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditListAdapter.VH {
        val binding = RowSubredditBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return VH(binding)
    }

    override fun onBindViewHolder(holder: SubredditListAdapter.VH, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        // return super.getItemCount()
        return currentList.size
    }


    class RedditDiff : DiffUtil.ItemCallback<RedditPost>()
    {
        override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {

            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {

            return RedditPost.spannableStringsEqual(oldItem.title, newItem.title) &&
                    oldItem.displayName?.let { RedditPost.spannableStringsEqual(it, newItem.displayName) } == true &&
                    (oldItem.iconURL == newItem.iconURL) &&
                    oldItem.publicDescription?.let { RedditPost.spannableStringsEqual(it, newItem.publicDescription) } == true

        }
    }

}
