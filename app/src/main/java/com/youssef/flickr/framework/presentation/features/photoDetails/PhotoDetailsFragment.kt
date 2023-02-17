package com.youssef.flickr.framework.presentation.features.photoDetails

import androidx.fragment.app.viewModels
import com.youssef.flickr.R
import com.youssef.flickr.databinding.FragmentPhotoDetailsBinding
import com.youssef.flickr.framework.presentation.features.base.BaseFragment
import com.youssef.flickr.framework.utils.states.DataState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PhotoDetailsFragment : BaseFragment<FragmentPhotoDetailsBinding>() {

    private val viewModel by viewModels<PhotoDetailsViewModel>()

    override fun bindViews() {
        initUI()
        subscribeOnViewObservers()
    }

    private fun initUI() {
        binding.photo = viewModel.photo
        binding.isFavourite = viewModel.photo.isFavourite
        binding.favouriteIV.setOnClickListener { viewModel.didClickFavourite() }
        binding.downloadIV.setOnClickListener { viewModel.downloadPhoto() }
    }

    private fun subscribeOnViewObservers() {
        viewModel.addToFavouriteDataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    binding.isFavourite = true
                    binding.favouriteLoading = false
                    showMessage(getString(R.string.add_to_favourite_success_message))
                }
                is DataState.Failure -> {
                    binding.favouriteLoading = false
                    showMessage(getString(R.string.action_failure_message))
                }
                DataState.Loading -> binding.favouriteLoading = true
            }
        }
        viewModel.removeFromFavouriteDataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    binding.isFavourite = false
                    binding.favouriteLoading = false
                    showMessage(getString(R.string.remove_from_favourite_success_message))
                }
                is DataState.Failure -> {
                    binding.favouriteLoading = false
                    showMessage(getString(R.string.action_failure_message))
                }
                DataState.Loading -> binding.favouriteLoading = true
            }
        }
        viewModel.downloadPhotoDataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    binding.downloadLoading = false
                    showMessage(getString(R.string.download_success_message, it.data))
                }
                is DataState.Failure -> {
                    binding.downloadLoading = false
                    showMessage(getString(R.string.action_failure_message))
                }
                DataState.Loading -> binding.downloadLoading = true
            }
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_photo_details

}