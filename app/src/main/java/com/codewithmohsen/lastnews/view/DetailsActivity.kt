package com.codewithmohsen.lastnews.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import com.codewithmohsen.lastnews.R
import com.codewithmohsen.lastnews.databinding.ActivityDetailsBinding
import com.codewithmohsen.lastnews.models.Article
import com.codewithmohsen.lastnews.vm.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DetailsActivity: AppCompatActivity() {

    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        binding.lifecycleOwner = this

        if(savedInstanceState == null) {
            val article = intent.getParcelableExtra<Article>(ARTICLE_KEY)
            check(article != null)
            Timber.d("Article from intent: $article")
            viewModel.setArticle(article)
        }

        viewModel.article.observe(this) { article ->
            Timber.d("Article form liveData: $article")
            if(article != null)
                binding.item = article
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_share -> {
                share()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun share() {
        ShareCompat.IntentBuilder(this)
            .setType("text/plain")
            .setChooserTitle("Share URL")
            .setText("${binding.item?.title}\n${binding.item?.url}")
            .startChooser()
    }


    companion object {

        private const val ARTICLE_KEY = "article_key"

        fun startActivity(context: Context, article: Article) {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(ARTICLE_KEY, article)
            context.startActivity(intent)
        }
    }
}