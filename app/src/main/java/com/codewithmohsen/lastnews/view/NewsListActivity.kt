package com.codewithmohsen.lastnews.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.codewithmohsen.lastnews.R
import com.codewithmohsen.lastnews.adapter.ItemListAdapter
import com.codewithmohsen.lastnews.databinding.ActivityNewsListBinding
import com.codewithmohsen.lastnews.models.Category
import com.codewithmohsen.lastnews.repository.Status
import com.codewithmohsen.lastnews.vm.NewsListViewModel
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import com.google.android.material.snackbar.Snackbar


@AndroidEntryPoint
class NewsListActivity : AppCompatActivity() {

    private val job: Job = Job()

    private val viewModel: NewsListViewModel by viewModels()

    private lateinit var binding: ActivityNewsListBinding
    private lateinit var adapter: ItemListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_list)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = ItemListAdapter { item ->
            DetailsActivity.startActivity(this, item)
        }

        binding.itemList.layoutManager = GridLayoutManager(this, 2)
        binding.itemList.adapter = adapter

        lifecycleScope.launch(job) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getNewsAsFlow().collect { resource ->
                    adapter.submitList(resource.data)
                    binding.status = resource.status
                    binding.hasData = !resource.data.isNullOrEmpty()
                    Timber.d("data is ${binding.hasData}")

                    if (binding.swipeRefresh.isRefreshing) {
                        Timber.d("result = isRefreshing")
                        binding.swipeRefresh.isRefreshing = Status.LOADING == resource.status
                                || Status.LONG_LOADING == resource.status
                    }
                    else {
                        Timber.d("result = is Not Refreshing")
                        binding.swipeRefresh.isRefreshing = resource.data.isNullOrEmpty() &&
                                Status.LOADING == resource.status
                                || Status.LONG_LOADING == resource.status
                    }

                    if (resource.status == Status.ERROR && resource.messageResource != null) {
                        Snackbar.make(binding.root, resources.getString(resource.messageResource),
                            Snackbar.LENGTH_LONG).also { snackBar ->
                            snackBar.setAction(resources.getString(R.string.yes)) {
                                viewModel.refresh()
                                snackBar.dismiss()
                            }
                        }.show()
                    }

                    if(resource.status == Status.NETWORK_ERROR && resource.messageResource != null)
                        Snackbar.make(binding.root, resources.getString(resource.messageResource),
                            Snackbar.LENGTH_LONG).show()
                }
            }
        }

        viewModel.fetchNews(category = Category.general)

        binding.itemList.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(binding.itemList.layoutManager!!, 0) {
            override fun onLoadMore() {
                Timber.d("fetch more")
                viewModel.fetchMoreNews()
            }
        })

        binding.cancelContainer.cancelButton.setOnClickListener {
            viewModel.cancel()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_filter -> {
                SelectCategoryDialogFragment.newInstance()
                    .show(supportFragmentManager, this::class.java.name)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}