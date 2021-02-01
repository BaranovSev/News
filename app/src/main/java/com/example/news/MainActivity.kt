package com.example.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.adaptersforrecycler.RecyclerAdapter
import com.example.news.fragments.AllNews
import com.example.news.fragments.Favorites
import com.example.news.fragments.Search
import com.example.news.fragments.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_all_news.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

const val BASE_URL = "http://newsapi.org"

class MainActivity : AppCompatActivity() {

    private var titlesList = mutableListOf<String>()
    private var descriptionList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()
    private var linksList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpTabs()
        makeAPIRequest()
    }
    private fun setUpRecyclerView(){
        rv_recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        rv_recyclerView.adapter= RecyclerAdapter(titlesList,descriptionList,imagesList,linksList)

    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(AllNews(), "All news")
        adapter.addFragment(Favorites(), "Favorites")
        adapter.addFragment(Search(), "Search")
        viewPager.adapter=adapter
        tabLayout.setupWithViewPager(viewPager)

    }
    private fun addToList(title:String, description:String, image:String, link:String){
        titlesList.add(title)
        descriptionList.add(description)
        imagesList.add(image)
        linksList.add(link)
    }

    private fun makeAPIRequest(){
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)
        GlobalScope.launch (Dispatchers.IO){
            try {
                val response = api.getNews()
                for(article in response.articles) {
                    Log.i("MainActivity", "Result = $article")
                    addToList(article.title, article.description, article.urlToImage, article.url)
                }
                withContext(Dispatchers.Main){
                    setUpRecyclerView()
                }

            } catch (e:Exception){
                Log.e ("MainActivity", e.toString())
            }
        }
    }
}