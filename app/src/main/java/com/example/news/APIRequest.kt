package com.example.news

import com.example.news.api.NewsApiJSON
import retrofit2.http.GET

interface APIRequest {
    @GET ("/v2/top-headlines?country=ru&apiKey=c935978be6444546b954e1bb7d68a829")
    suspend fun  getNews(): NewsApiJSON
}