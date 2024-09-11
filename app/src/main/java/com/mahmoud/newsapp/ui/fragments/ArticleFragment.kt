package com.mahmoud.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.mahmoud.newsapp.MainActivity
import com.mahmoud.newsapp.databinding.FragmentArticleBinding
import com.mahmoud.newsapp.ui.NewsViewModel

class ArticleFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel

    private lateinit var binding: FragmentArticleBinding
    val args: ArticleFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = (activity as MainActivity).viewModel

        binding = FragmentArticleBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
            Log.v("ArticleFragment", article.url)
            Toast.makeText(requireContext(), article.url, Toast.LENGTH_SHORT).show()
        }

        /**
         * Add article to database
         */
        binding.fab.setOnClickListener {
            viewModel.upsert(article)
            Snackbar.make(it, "Article added successfully", Snackbar.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}