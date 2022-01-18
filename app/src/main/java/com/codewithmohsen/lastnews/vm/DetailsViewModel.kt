package com.codewithmohsen.lastnews.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codewithmohsen.lastnews.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle): ViewModel() {

    val article: LiveData<Article> =
        savedStateHandle.getLiveData(SAVED_STATE_KEY)

    fun setArticle(article: Article) {
        savedStateHandle[SAVED_STATE_KEY] = article
    }

    companion object {

        private const val SAVED_STATE_KEY = "saved_state_key"
    }
}