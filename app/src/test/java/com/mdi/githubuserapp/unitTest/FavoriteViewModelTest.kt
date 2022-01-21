package com.mdi.githubuserapp.unitTest

import androidx.lifecycle.LiveData
import com.mdi.githubuserapp.database.FavoriteDb
import junit.framework.TestCase

class FavoriteViewModelTest : TestCase() {

    fun testGetAllFavs(): LiveData<List<FavoriteDb>> = mFavRepository.getAllFav()
}