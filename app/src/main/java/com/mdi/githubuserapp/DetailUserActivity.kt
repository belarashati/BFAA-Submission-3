package com.mdi.githubuserapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mdi.githubuserapp.adapter.PagerAdapter
import com.mdi.githubuserapp.database.FavoriteDb
import com.mdi.githubuserapp.databinding.ActivityDetailUserBinding
import com.mdi.githubuserapp.response.UserDetailResponse
import com.mdi.githubuserapp.response.UsersResponse
import com.mdi.githubuserapp.viewmodel.DetailUserViewModel
import com.mdi.githubuserapp.viewmodel.FavUpdateViewModel
import com.mdi.githubuserapp.viewmodel.ViewModelFactory

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var userDetailViewModel: DetailUserViewModel
    private lateinit var favUpdateViewModel: FavUpdateViewModel
    private lateinit var userData: UserDetailResponse


    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FAV = "extra_fav"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userDetailViewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]
        favUpdateViewModel = obtainViewModel(this@DetailUserActivity)


        val user = intent.getParcelableExtra<UsersResponse>(EXTRA_USER) as UsersResponse?
        val userFav = intent.getParcelableExtra<FavoriteDb>(EXTRA_FAV) as FavoriteDb?

        binding.ivUnFav.isEnabled = false
        binding.ivFav.isEnabled = false

        if(user != null){
            initData(user.login)
        } else {
            if (userFav?.favorite!!){
                binding.ivFav.visibility = View.VISIBLE
                binding.ivUnFav.visibility = View.INVISIBLE
            }
            initData(userFav.username)
        }


        supportActionBar!!.title = user?.login

        val pagerAdapter = PagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = pagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(DetailUserActivity.TAB_TITLES[position])
        }.attach()


        binding.btShare.setOnClickListener {
            shareDetail(userData)

        }

        binding.ivUnFav.setOnClickListener {
            if(binding.ivUnFav.visibility == View.VISIBLE){
                binding.ivFav.visibility = View.VISIBLE
                binding.ivUnFav.visibility = View.INVISIBLE
                var fav = FavoriteDb()
                fav.id = userData.id
                fav.username = userData.login
                fav.avatarUrl = userData.avatarUrl
                fav.favorite = true
                favUpdateViewModel.insert(fav)
                showToast(getString(R.string.added))
            }
            
        }

        binding.ivFav.setOnClickListener {
            var fav = FavoriteDb()
            fav.id = userData.id
            fav.username = userData.login
            fav.avatarUrl = userData.avatarUrl
            fav.favorite = false
            favUpdateViewModel.delete(fav)
            showToast(getString(R.string.remove))
            binding.ivUnFav.visibility = View.VISIBLE
            binding.ivFav.visibility = View.INVISIBLE
            
        }


    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initData(user: String?) {
        showLoading(true)
        userDetailViewModel.setUserDetail(user!!, this)
        userDetailViewModel.getUserDetail().observe(this, Observer { userResp ->
            if(userResp != null){
                showLoading(false)
                userData = userResp
                if(userResp.name == null){
                    binding.tvName.visibility = View.GONE
                }
                if(userResp.location == null){
                    binding.tvLocation.visibility = View.GONE
                }
                if(userResp.company == null){
                    binding.tvCompany.visibility = View.GONE
                }
                binding.tvName.text = userResp.name
                binding.tvRepo.text = userResp.publicRepos.toString()
                binding.tvLocation.text = userResp.location
                binding.tvCompany.text = userResp.company
                binding.tvFollower.text = userResp.followers.toString()
                binding.tvFollowing.text = userResp.following.toString()

                binding.ivUnFav.isEnabled = true
                binding.ivFav.isEnabled = true


                Glide.with(this@DetailUserActivity)
                    .load(userResp.avatarUrl)
                    .into( binding.ivUser)
            } else {
                showLoading(false)
            }
        })
    }

    private fun shareDetail(user: UserDetailResponse) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Hi, this is ${user.login} / ${user.url}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavUpdateViewModel::class.java)
    }
}