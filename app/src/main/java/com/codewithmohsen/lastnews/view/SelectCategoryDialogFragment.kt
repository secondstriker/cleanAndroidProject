package com.codewithmohsen.lastnews.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.codewithmohsen.lastnews.R
import com.codewithmohsen.lastnews.databinding.BottomSheetSelectCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber
import android.widget.RadioButton
import androidx.fragment.app.activityViewModels
import com.codewithmohsen.lastnews.models.Category
import com.codewithmohsen.lastnews.vm.NewsListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SelectCategoryDialogFragment: BottomSheetDialogFragment() {

    val viewModel: NewsListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: BottomSheetSelectCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_select_category, container, false)

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val checkedRadioButton = binding.root.findViewById(checkedId) as RadioButton
            Timber.d("BottomSheetDialogFragment selected category ${checkedRadioButton.tag as String}")

            if(viewModel.getSelectedCategory().value != checkedId) {
                viewModel.fetchNews(Category.valueOf(checkedRadioButton.tag as String))
                viewModel.setSelectedCategory(checkedId)
                dismiss()
            }
        }

        viewModel.getSelectedCategory().observe(viewLifecycleOwner) { checkedId ->
            val checkedRadioButton = binding.root.findViewById(checkedId) as RadioButton
            checkedRadioButton.isChecked = true
        }

        return binding.root
    }


    companion object {

        @JvmStatic
        fun newInstance(): SelectCategoryDialogFragment {
            return SelectCategoryDialogFragment()
        }
    }
}