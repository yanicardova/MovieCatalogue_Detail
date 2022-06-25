package com.example.moviecatalouge

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var movies: List<Movie>? = null
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_movie_list.layoutManager = LinearLayoutManager(this)
        rv_movie_list.setHasFixedSize(true)
        getMovieData { movies : List<Movie> ->
            rv_movie_list.adapter = MovieAdapter(movies, object : MovieAdapter.OnAdapterListener {
                override fun onClick(result: Movie) {
                    val intent = Intent(applicationContext, DetailMovieActivity::class.java)
                    intent.putExtra(DetailMovieActivity.EXTRA_DATA, result)
                    startActivity(intent)
                }
            })
        }
    }

    private fun setupRecyclerView(){
        movieAdapter = MovieAdapter(arrayListOf(), object : MovieAdapter.OnAdapterListener {
            override fun onClick(result: Movie) {
                val intent = Intent(applicationContext, DetailMovieActivity::class.java)
                intent.putExtra(DetailMovieActivity.EXTRA_DATA, result)
                startActivity(intent)
            }
        })
        rv_movie_list.apply { layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter
        }
    }

    private fun getMovieData(callback: (List<Movie>) -> Unit){
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

        })
    }
}

