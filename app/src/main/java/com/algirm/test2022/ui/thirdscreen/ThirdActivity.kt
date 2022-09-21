package com.algirm.test2022.ui.thirdscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algirm.test2022.R
import com.algirm.test2022.databinding.ActivityThirdBinding
import com.algirm.test2022.util.AppConstants.Companion.QUERY_PAGE_SIZE
import com.algirm.test2022.util.AppConstants.Companion.TAG
import com.algirm.test2022.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdActivity : AppCompatActivity() {

    lateinit var binding: ActivityThirdBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        loadData()
    }

    private fun loadData() {
        userViewModel.responseLiveData.observe(this, { response ->
            Log.d(TAG, "observe data is $response")
            when(response) {
                is Resource.Failure -> {
                    toastError(response.throwable)
                    binding.progressBar.hide()
                }
                is Resource.Success -> {
                    userAdapter.differ.submitList(userViewModel.userResponse?.data?.toList())
                    binding.progressBar.hide()
                    isLoading = false
                    val totalPages = response.data.total / QUERY_PAGE_SIZE + 2
                    isLastPage = userViewModel.page == totalPages
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is Resource.Init -> {
                    userAdapter.differ.submitList(emptyList())
                }
                is Resource.Loading -> {
                    binding.progressBar.show()
                    isLoading = true
                }
            }
        })
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                userViewModel.getUsers()
                binding.progressBar.show()
                isScrolling = false
            } else {
                binding.rcvUser.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun initView() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        val arrow = findViewById<ImageView>(R.id.arrow_toolbar)
        val titleToolbar = findViewById<TextView>(R.id.title_toolbar)
        titleToolbar.text = getString(R.string.third_screen)
        arrow.setOnClickListener {
            finish()
        }
        userAdapter = UserAdapter()
        binding.rcvUser.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@ThirdActivity)
            addOnScrollListener(this@ThirdActivity.scrollListener)
        }
        userAdapter.setOnItemClickListener {
            val intent = Intent().apply {
                putExtra("user_fullname", "${it.first_name} ${it.last_name}")
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        binding.swipeRefreshLayout.setOnRefreshListener { swipeRefresh() }
    }

    private fun swipeRefresh() {
        userViewModel.responseLiveData.postValue(Resource.Init())
        userViewModel.userResponse = null
        userViewModel.page = 1
        userViewModel.getUsers()
    }

    private fun toastError(e: Throwable?) {
        val error = e?.message
        Toast.makeText(
            this,
            "An Error Has Occurred: ${error ?: "Something Wrong"}",
            Toast.LENGTH_SHORT
        ).show()
    }

}