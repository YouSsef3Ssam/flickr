package com.youssef.flickr.framework.presentation.features.photos

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.youssef.flickr.R
import com.youssef.flickr.databinding.FragmentPhotosBinding
import com.youssef.flickr.framework.presentation.callback.OnItemClickListener
import com.youssef.flickr.framework.presentation.entities.Photo
import com.youssef.flickr.framework.presentation.features.base.BaseFragment
import com.youssef.flickr.framework.utils.PaginationController
import com.youssef.flickr.framework.utils.ext.navigateTo
import com.youssef.flickr.framework.utils.states.PaginationDataState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhotosFragment : BaseFragment<FragmentPhotosBinding>() {
    private val viewModel by viewModels<PhotosViewModel>()

    @Inject
    lateinit var adapter: PhotosAdapter

    @Inject
    lateinit var paginationController: PaginationController

    override fun bindViews() {
        initUI()
        listenOnPagination()
        subscribeOnViewObservers()
    }

    private fun listenOnPagination() {
        binding.imagesRV.layoutManager.apply {
            if (this is LinearLayoutManager) {
                paginationController.setLayoutManager(this)
                paginationController.onLoadMoreItems { pageNumber ->
                    viewModel.loadNextPage(pageNumber)
                }
                binding.imagesRV.addOnScrollListener(paginationController)
            }
        }
    }

    private fun initUI() {
        binding.viewModel = viewModel
        binding.swipeRefreshLayout.setOnRefreshListener { paginationController.reload() }
        setupRV()
    }

    private fun setupRV() {
        adapter.listen(object : OnItemClickListener<Photo> {
            override fun onItemClicked(item: Photo) {
                navigateTo(PhotosFragmentDirections.openPhotoDetails(item))
            }
        })
        binding.imagesRV.adapter = adapter
    }

    private fun subscribeOnViewObservers() {
        viewModel.photosDataState.observe(viewLifecycleOwner) {
            when (it) {
                is PaginationDataState.First -> {
                    hideLoading()
                    paginationController.isLastPage(false)
                    adapter.submitList(it.data)
                }
                is PaginationDataState.Append -> {
                    hideLoading()
                    val photos = adapter.currentList.toMutableSet()
                    photos.addAll(it.data)
                    adapter.submitList(photos.toList())
                }
                is PaginationDataState.Failure -> {
                    hideLoading()
                    handleError(it.throwable)
                }
                PaginationDataState.FirstLoading -> binding.swipeRefreshLayout.isRefreshing = true
                PaginationDataState.PaginationEnded -> paginationController.isLastPage(true)
                PaginationDataState.PaginationLoading -> paginationController.isLoading(true)
            }
        }
    }

    private fun hideLoading() {
        binding.swipeRefreshLayout.isRefreshing = false
        paginationController.isLoading(false)
    }

    override fun getLayoutResId() = R.layout.fragment_photos

    override fun onDestroyView() {
        paginationController.clean()
        binding.imagesRV.adapter = null
        binding.imagesRV.layoutManager = null
        super.onDestroyView()
    }
}