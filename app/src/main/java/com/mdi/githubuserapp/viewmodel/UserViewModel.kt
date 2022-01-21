package com.mdi.githubuserapp.viewmodel

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdi.githubuserapp.DetailUserActivity
import com.mdi.githubuserapp.adapter.UserAdapter
import com.mdi.githubuserapp.network.ApiConfig
import com.mdi.githubuserapp.response.SearchUserResponse
import com.mdi.githubuserapp.response.UserDetailResponse
import com.mdi.githubuserapp.response.UsersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel: ViewModel() {
    val listUser = MutableLiveData<List<UsersResponse>>()
    val listUserSearch = MutableLiveData<List<UsersResponse?>?>()


    fun setSearchUser(type: String, mContext: Context){
        val client = ApiConfig.getApiService(mContext).getSearchUser(type)
        client.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                if (response.isSuccessful) {
                    val userResp = response.body()!!.items
                    listUserSearch.postValue(userResp)

                } else {
                    Log.e("User", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                Log.e("User", "onFailure: ${t.message}")
            }
        })
    }

    fun  showUserList(mContext : Context){
        val client = ApiConfig.getApiService(mContext).getUsers()
        client.enqueue(object : Callback<List<UsersResponse>> {
            override fun onResponse(
                call: Call<List<UsersResponse>>,
                response: Response<List<UsersResponse>>
            ) {
                if (response.isSuccessful) {
                    val userResp = response.body()!!
                    listUser.postValue(userResp)
                } else {
                    Log.e("User", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<UsersResponse>>, t: Throwable) {
                Log.e("User", "onFailure: ${t.message}")
            }
        })
    }

    fun getSearchUser(): MutableLiveData<List<UsersResponse?>?> {
        return listUserSearch
    }

    fun getUser(): MutableLiveData<List<UsersResponse>> {
        return listUser
    }
}