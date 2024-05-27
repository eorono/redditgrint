package com.example.redditgrint.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.redditgrint.R
import com.example.redditgrint.api.RedditPost
import com.example.redditgrint.databinding.RowPostBinding

class PostRowAdapter(private val viewModel: MainViewModel)
    : ListAdapter<RedditPost, PostRowAdapter.VH>(RedditDiff())
{

    inner class VH(rowPostBinding: RowPostBinding) : RecyclerView.ViewHolder(rowPostBinding.root) {

        val title = rowPostBinding.title
        val image = rowPostBinding.image
        val selfText = rowPostBinding.selfText
        val score = rowPostBinding.score
        val comments = rowPostBinding.comments
        val rowFav = rowPostBinding.rowFav
        var imgUrlT = String()
        lateinit var post1 : RedditPost

        init {

            rowPostBinding.title.setOnClickListener {
                viewModel.postClickedListener(title.text.toString(), post1.imageURL, selfText.text.toString(),
                    post1.thumbnailURL)
            }

            rowPostBinding.rowFav.setOnClickListener {
                viewModel.favClicked(post1, adapterPosition)
            }
        }

        fun bind(post : RedditPost) {

            post1 = post
            title.text = post.title

            Log.d("0000", " image:"+post.imageURL)
            Log.d("0000", " thumbnail:"+post.thumbnailURL)
            Log.d("0000", " text:"+post.selfText)

            //Posts without images
            if(post.imageURL.isEmpty())
            {
                image.visibility = View.GONE
                selfText.visibility = View.VISIBLE
                if(post.selfText?.length!! > 0)
                    selfText.text = post.selfText
            }
            //Posts containing texts
            else if((post.thumbnailURL.isEmpty() || post.thumbnailURL == "self") && post.imageURL.endsWith("/"))
            {
                image.visibility = View.GONE
                selfText.visibility = View.VISIBLE
                if(post.selfText?.length!! > 0)
                    selfText.text = post.selfText
            }
            //Posts containing images
            else
            {
                image.visibility = View.VISIBLE
                selfText.visibility = View.GONE
                viewModel.netFetchImage(post.imageURL, post.thumbnailURL, image)
            }

            score.text = post.score.toString()
            comments.text = post.commentCount.toString()

            if(viewModel.getFavsList()?.contains(post) == true)
                rowFav.setImageResource(R.drawable.ic_favorite_black_24dp)
            else
                rowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RowPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {

        return currentList.size
    }


    class RedditDiff : DiffUtil.ItemCallback<RedditPost>()
    {
        override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {

            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {

            return RedditPost.spannableStringsEqual(oldItem.title, newItem.title) &&
                    oldItem.selfText?.let { RedditPost.spannableStringsEqual(it, newItem.selfText) } == true &&
                    (oldItem.imageURL == newItem.imageURL) &&
                    (oldItem.score == newItem.score) &&
                    (oldItem.commentCount == newItem.commentCount)
        }
    }
}
