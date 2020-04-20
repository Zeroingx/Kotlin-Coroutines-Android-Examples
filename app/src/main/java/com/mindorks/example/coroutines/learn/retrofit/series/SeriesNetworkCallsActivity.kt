package com.mindorks.example.coroutines.learn.retrofit.series

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mindorks.example.coroutines.R
import com.mindorks.example.coroutines.data.api.ApiHelper
import com.mindorks.example.coroutines.data.api.RetrofitBuilder
import com.mindorks.example.coroutines.data.model.User
import com.mindorks.example.coroutines.learn.retrofit.base.UserAdapter
import com.mindorks.example.coroutines.utils.Status
import com.mindorks.example.coroutines.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_network_call.*

class SeriesNetworkCallsActivity : AppCompatActivity() {

    private lateinit var viewModel: SeriesNetworkCallsViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_call)
        setupUI()
        setupViewModel()
        setupAPICall()
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter =
            UserAdapter(
                arrayListOf()
            )
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupAPICall() {
        viewModel.getUsers().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { users -> renderList(users) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
        viewModel.fetchUsers()
    }

    private fun renderList(users: List<User>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(SeriesNetworkCallsViewModel::class.java)
    }
}