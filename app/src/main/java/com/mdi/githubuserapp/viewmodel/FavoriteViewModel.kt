package com.mdi.githubuserapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mdi.githubuserapp.database.FavRepository
import com.mdi.githubuserapp.database.FavoriteDb

class FavoriteViewModel (application: Application) : ViewModel() {
    private val mFavRepository: FavRepository = FavRepository(application)

    fun getAllFavs(): LiveData<List<FavoriteDb>> = mFavRepository.getAllFav()
}