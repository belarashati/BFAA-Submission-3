package com.mdi.githubuserapp.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdi.githubuserapp.DetailUserActivity
import com.mdi.githubuserapp.adapter.FollowAdapter
import com.mdi.githubuserapp.database.FavoriteDb
import com.mdi.githubuserapp.databinding.FragmentFollowerBinding
import com.mdi.githubuserapp.network.ApiConfig
import com.mdi.githubuserapp.response.FollowResponse
import com.mdi.githubuserapp.response.UsersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowerFragment : Fragment() {

    private lateinit var binding : FragmentFollowerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
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

        if (username != null){
            getFollower(username)
        }
    }

    private fun getFollower(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService(requireContext()).getFollowers(username)
        client.enqueue(object : Callback<List<FollowResponse>> {
            override fun onResponse(
                call: Call<List<FollowResponse>>,
                response: Response<List<FollowResponse>>
            ) {
                showLoading(false)
                val userResp = response.body()
                if (userResp!!.isNotEmpty()) {
                    if (activity!!.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        binding.rvFollower.layoutManager = GridLayoutManager(context, 2)
                    } else {
                        binding.rvFollower.layoutManager = LinearLayoutManager(context)
                    }

                    val followAdapter = FollowAdapter(requireContext(), userResp)
                    binding.rvFollower.adapter = followAdapter

                } else {
                    binding.tvNoneFollower.visibility = View.VISIBLE
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