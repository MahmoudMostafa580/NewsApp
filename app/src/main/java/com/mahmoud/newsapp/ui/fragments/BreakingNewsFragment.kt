package com.mahmoud.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmoud.newsapp.MainActivity
import com.mahmoud.newsapp.R
import com.mahmoud.newsapp.adapters.NewsAdapter
import com.mahmoud.newsapp.databinding.FragmentBreakingNewsBinding
import com.mahmoud.newsapp.repository.NewsRepository
import com.mahmoud.newsapp.ui.NewsViewModel
import com.mahmoud.newsapp.ui.NewsViewmodelFactory
import com.mahmoud.newsapp.utils.Resource

class BreakingNewsFragment : Fragment() {

    lateinit var viewModel: NewsViewModel
    lateinit var breakingNewsBinding: FragmentBreakingNewsBinding
    lateinit var newsAdapter: NewsAdapter
    val TAG: String = "BreakingNewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        breakingNewsBinding =
            FragmentBreakingNewsBinding.inflate(inflater, container, false)

        return breakingNewsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle= Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message")
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()

                }
            }
        })

    }

    private fun hideProgressBar() {
        breakingNewsBinding.paginationProgressBar.visibility = View.INVISIBLE
    }


    private fun showProgressBar() {
        breakingNewsBinding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        breakingNewsBinding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}