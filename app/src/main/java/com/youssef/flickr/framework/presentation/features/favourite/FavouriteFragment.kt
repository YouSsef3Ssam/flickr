package com.youssef.flickr.framework.presentation.features.favourite

import androidx.fragment.app.viewModels
import com.youssef.flickr.R
import com.youssef.flickr.databinding.FragmentFavouriteBinding
import com.youssef.flickr.framework.presentation.callback.OnItemClickListener
import com.youssef.flickr.framework.presentation.entities.Photo
import com.youssef.flickr.framework.presentation.features.base.BaseFragment
import com.youssef.flickr.framework.presentation.features.photos.PhotosAdapter
import com.youssef.flickr.framework.utils.ext.navigateTo
import com.youssef.flickr.framework.utils.states.DataState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>() {

    private val viewModel by viewModels<FavouritesViewModel>()

    @Inject
    lateinit var adapter: PhotosAdapter

    override fun bindViews() {
        initUI()
        subscribeOnViewObservers()
    }

    private fun initUI() {
        binding.favouriteFragmentLayout.setOnRefreshListener { viewModel.refresh() }
        setupRV()
    }

    private fun setupRV() {
        binding.favouritesPhotosRV.adapter = adapter
        adapter.listen(object : OnItemClickListener<Photo> {
            override fun onItemClicked(item: Photo) {
                navigateTo(FavouriteFragmentDirections.openPhotoDetails(item))
            }
        })
    }

    private fun subscribeOnViewObservers() {
        viewModel.favouritePhotosDataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    binding.favouriteFragmentLayout.isRefreshing = false
                    binding.isEmpty = it.data.isEmpty()
                    adapter.submitList(it.data)
                }
                is DataState.Failure -> {
                    binding.favouriteFragmentLayout.isRefreshing = false
                    showMessage(getString(R.string.action_failure_message))
                }
                DataState.Loading -> binding.favouriteFragmentLayout.isRefreshing = true
            }
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_favourite

    override fun onDestroyView() {
        binding.favouritesPhotosRV.adapter = null
        binding.favouritesPhotosRV.layoutManager = null
        super.onDestroyView()
    }
}
