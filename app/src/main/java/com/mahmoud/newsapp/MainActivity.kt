package com.mahmoud.newsapp

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mahmoud.newsapp.database.ArticlesDatabase
import com.mahmoud.newsapp.databinding.ActivityMainBinding
import com.mahmoud.newsapp.repository.NewsRepository
import com.mahmoud.newsapp.ui.NewsViewModel
import com.mahmoud.newsapp.ui.NewsViewmodelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        /**
         * Initialize ViewModel
         */
        val newsRepository = NewsRepository(ArticlesDatabase.getInstance(this))
        val viewModelFactory = NewsViewmodelFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java)

        /**
         * Bind layout
         */
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Handle BottomNavigationView Transition
         */
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment)
        navController = navHostFragment!!.findNavController()
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}