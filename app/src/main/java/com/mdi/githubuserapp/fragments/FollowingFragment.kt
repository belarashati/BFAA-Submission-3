package com.mdi.githubuserapp.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdi.githubuserapp.DetailUserActivity
import com.mdi.githubuserapp.MainActivity
import com.mdi.githubuserapp.adapter.FollowAdapter
import com.mdi.githubuserapp.adapter.UserAdapter
import com.mdi.githubuserapp.database.FavoriteDb
import com.mdi.githubuserapp.databinding.FragmentFollowingBinding
import com.mdi.githubuserapp.network.ApiConfig
import com.mdi.githubuserapp.response.FollowResponse
import com.mdi.githubuserapp.response.UsersResponse
import com.mdi.githubuserapp.viewmodel.DetailUserViewModel
import com.mdi.githubuserapp.viewmodel.FollowingViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {

    private lateinit var binding : FragmentFollowingBinding
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intentUser =
            activity?.intent?.getParcelableExtra(DetailUserActivity.EXTRA_USER) as UsersResponse?
        val intentFav =
            activity?.intent?.getParcelableExtra(DetailUserActivity.EXTRA_FAV) as FavoriteDb?

        val username = if(intentUser != null) {
            intentUser?.login
        } else {
            intentFav?.username
        }

        followingViewModel = ViewModelProvider(this)[FollowingViewModel::class.java]

        if (username != null){
            initData(username)
        }
    }

    private fun initData(username: String) {
        showLoading(true)
        followingViewModel.setFowllowing(username, requireContext())
        followingViewModel.getFollowing().observe(requireActivity(), Observer { userResp ->
            if (userResp!!.isNotEmpty()) {
                showLoading(false)
                if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    binding.rvFollowing.layoutManager = GridLayoutManager(context, 2)
                } else {
                    binding.rvFollowing.layoutManager = LinearLayoutManager(context)
                }


                val followAdapter = FollowAdapter(requireContext(),userResp)
                binding.rvFollowing.adapter = followAdapter


            } else {
                showLoading(false)
                binding.tvNoneFollowing.visibility = View.VISIBLE
            }
        })
    }

    private fun getFollower(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService(requireContext()).getFollowing(username)
        client.enqueue(object : Callback<List<FollowResponse>> {
            override fun onResponse(
                call: Call<List<FollowResponse>>,
                response: Response<List<FollowResponse>>
            ) {
                showLoading(false)
                val userResp = response.body()
                if (userResp!!.isNotEmpty()) {
                    if (activity!!.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        binding.rvFollowing.layoutManager = GridLayoutManager(context, 2)
                    } else {
                        binding.rvFollowing.layoutManager = LinearLayoutManager(context)
                    }


                    val followAdapter = FollowAdapter(requireContext(),userResp)
                    binding.rvFollowing.adapter = followAdapter


                } else {
                    binding.tvNoneFollowing.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<List<FollowResponse>>, t: Throwable) {
                showLoading(false)
                Log.e("User", "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}