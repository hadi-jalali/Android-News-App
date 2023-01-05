package com.example.laknews.Fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.laknews.Adapters.NewsAdapter
import com.example.laknews.R
import com.example.laknews.UI.NewsActivity
import com.example.laknews.UI.NewsViewModel
import com.example.laknews.Util.Constants
import com.example.laknews.Util.Resource
import com.example.laknews.Util.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_recent_news.*



class SelectedNewsFragment:Fragment(R.layout.fragment_recent_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG = "CustomizedNews"
    lateinit var swipeRefreshLayout:SwipeRefreshLayout

    val currentUSer = FirebaseAuth.getInstance().currentUser
    private lateinit var interests:String

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        val currentUSer = FirebaseAuth.getInstance().currentUser
        if (currentUSer != null) {
            interests = getInterests(currentUSer.uid)
            Log.w(TAG,interests)
        }
        else{
            Log.w(TAG,"user not found")
        }
        swipeRefreshLayout = getView()?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)!!
        swipeRefreshLayout.setOnRefreshListener{
            Toast.makeText(this.context,"Refreshed",Toast.LENGTH_LONG)
            val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false)
            }
            ft.detach(this).attach(this).commit()

        }

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_recentNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.selectedNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = (newsResponse.totalResults / Constants.QUERY_PAGE_SIZE) + 2
                        islastPage = viewModel.selectedNewsPage == totalPages

                        if (islastPage) {
                            rvRecentNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message ")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isloading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isloading = true
    }

    var isloading = false
    var islastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstItemPos = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingNotLastPage = !isloading && !islastPage
            val isLast = firstItemPos +visibleItemCount >= totalItemCount
            val isNotBegging = firstItemPos >=0
            val isTotalMoreThanVisible =  totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingNotLastPage &&
                    isLast && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                if (interests == null){
                    viewModel.getSelectedNews(interests,"gb")
                }
                else{
                    viewModel.getSelectedNews("sports","gb")
                }
                isScrolling =false

            }
        }
    }

    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        rvRecentNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SelectedNewsFragment.scrollListener)
        }
    }
    fun getInterests(uid: String): String {
        var interest = ""
        val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)
            .child("interest")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                interest = snapshot.getValue().toString()
                Log.w(TAG, interest)
            }

            override fun onCancelled(error: DatabaseError) {
                interest = ""
                Log.w(TAG, "ffsssss")
            }
        })
        return interest


    }
}
