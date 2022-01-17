package com.codewithmohsen.lastnews.adapter

import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.codewithmohsen.lastnews.R
import com.codewithmohsen.lastnews.databinding.NewsItemBinding
import com.codewithmohsen.lastnews.di.DefaultDispatcher
import com.codewithmohsen.lastnews.models.Article
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * A RecyclerView adapter for [Item List] class.
 */
class ItemListAdapter(
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val dataBindingComponent: DataBindingComponent,
    private val itemClickCallback: ((Article) -> Unit)?
) : DataBoundListAdapter<Article, NewsItemBinding>(
    defaultDispatcher,
    diffCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.author == newItem.author &&
            oldItem.title == newItem.title &&
            oldItem.content == newItem.content &&
            oldItem.source == newItem.source &&
            oldItem.description == newItem.description &&
            oldItem.publishedAt == newItem.publishedAt &&
            oldItem.url == newItem.url &&
            oldItem.urlToImage == newItem.urlToImage


        }
    }
) {

    override fun createBinding(parent: ViewGroup): NewsItemBinding {

        val binding = DataBindingUtil.inflate<NewsItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.news_item,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener { _ ->
            binding.item.let {
                if(it != null) itemClickCallback?.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: NewsItemBinding, item: Article) {
        binding.item = item
    }
}
