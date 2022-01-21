package com.mdi.githubuserapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.mdi.githubuserapp.database.FavoriteDb


class FavDiffCallBack(private val mOldFavList: List<FavoriteDb>, private val mNewFavList: List<FavoriteDb>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavList.size
    }

    override fun getNewListSize(): Int {
        return mNewFavList.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavList[oldItemPosition].id == mNewFavList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldFavList[oldItemPosition]
        val newEmployee = mNewFavList[newItemPosition]
        return oldEmployee.id == newEmployee.id && oldEmployee.username == newEmployee.username && oldEmployee.favorite == newEmployee.favorite
    }
}
