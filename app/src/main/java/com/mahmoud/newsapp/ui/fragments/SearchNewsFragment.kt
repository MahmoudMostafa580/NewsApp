package com.mahmoud.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.newsapp.MainActivity
import com.mahmoud.newsapp.R
import com.mahmoud.newsapp.adapters.NewsAdapter
import com.mahmoud.newsapp.databinding.FragmentSearchNewsBinding
import com.mahmoud.newsapp.ui.NewsViewModel
import com.mahmoud.newsapp.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.mahmoud.newsapp.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentSearchNewsBinding
    lateinit var newsAdapter: NewsAdapter
    val TAG: String = "SearchNewsFragment"

    /**
     * Boolean variables for pagination process
     */
    var isLoading = false
    var isScrolling = false
    var isLastPage = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()


        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }
        /**
         * Delay for listener on search edit text for search query
         */
        var job: Job? = null
//        binding.etSearch.editText?.addTextChangedListener { editable ->
//            job?.cancel()
//            job = MainScope().launch {
//                delay(SEARCH_NEWS_TIME_DELAY)
//                editable?.let{
//                    if (editable.toString().isNotEmpty()){
//                        viewModel.searchNews(editable.toString())
//                    }
//                }
//            }
//        }
        binding.etSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                job?.cancel()
//                job = MainScope().launch {
//                    delay(SEARCH_NEWS_TIME_DELAY)
//                    query?.let {
//                        if (query.isNotEmpty()) {
//                            viewModel.searchNews(query)
//                        }else{
//                            newsAdapter.differ.submitList(null)
//                        }
//                    }
//                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(SEARCH_NEWS_TIME_DELAY)
                    newText?.let {
                        if (newText.isNotEmpty()) {
                            if (binding.etSearch.hasFocus()){
                                viewModel.searchNewsResponse = null
                                viewModel.searchNewsPage = 1
                                viewModel.searchNews(newText)
                            }
                        }else{
                            newsAdapter.differ.submitList(listOf())
                        }
                    }
                }
                return false
            }

        })

        /**
         * Observe on searchNewsLiveData for incoming changes in data retrieved.
         */
        viewModel.searchNewsLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
//                        val totalPage = newsResponse.totalResults / 20 + 2
//                        isLastPage = viewModel.searchNewsPage == totalPage
//                        if (isLastPage) {
//                            binding.rvSearchNews.setPadding(0, 0, 0, 0)
//                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An Error Occurred: $message")
                        Toast.makeText(activity, "An Error Occurred: $message", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false

    }


    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true

    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val visibleItemCount = layoutManager.childCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = visibleItemCount + firstVisibleItemPosition >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getBreakingNews("us")
                isScrolling = false
            }

        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)

        }
    }
}