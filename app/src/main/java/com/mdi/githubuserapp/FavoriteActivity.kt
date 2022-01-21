package com.mdi.githubuserapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdi.githubuserapp.adapter.FavoriteAdapter
import com.mdi.githubuserapp.database.FavoriteDb
import com.mdi.githubuserapp.databinding.ActivityFavoriteBinding
import com.mdi.githubuserapp.viewmodel.FavoriteViewModel
import com.mdi.githubuserapp.viewmodel.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavoriteBinding
    private lateinit  var favoriteViewModel : FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        showLoading(true)

        favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavs().observe(this, Observer { it ->
            if (!it.isNullOrEmpty()){
                showLoading(false)
                if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    binding?.rvUserFav?.layoutManager = GridLayoutManager(applicationContext, 2)
                } else {
                    binding?.rvUserFav?.layoutManager = LinearLayoutManager(applicationContext)
                }

                val favAdapter = FavoriteAdapter(applicationContext, it)
                binding?.rvUserFav?.adapter = favAdapter

                favAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: FavoriteDb?) {
                        val intent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                        intent.putExtra(DetailUserActivity.EXTRA_FAV, data)
                        startActivity(intent)
                    }
                })
            } else {
                showLoading(false)
                binding?.tvNoneFav?.visibility = View.VISIBLE
                binding?.rvUserFav?.visibility = View.GONE
            }

        })
    }

    private fun obtainViewModel(activity: FavoriteActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }
}