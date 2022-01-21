package com.mdi.githubuserapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.mdi.githubuserapp.database.FavRepository
import com.mdi.githubuserapp.database.FavoriteDb

class FavUpdateViewModel(application: Application) : ViewModel() {


    private val mFavRepository: FavRepository = FavRepository(application)
    fun insert(fav: FavoriteDb) {
        mFavRepository.insert(fav)
    }
    fun update(fav: FavoriteDb) {
        mFavRepository.update(fav)
    }
    fun delete(fav: FavoriteDb) {
        mFavRepository.delete(fav)
    }
}