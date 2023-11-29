package com.neotica.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.neotica.core.data.remote.ApiResult
import com.neotica.core.data.remote.model.MovieModel
import com.neotica.main.databinding.FragmentMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import com.neotica.cinepoly.ui.fragment.adapter.MovieAdapter
import com.neotica.cinepoly.R.string
import com.neotica.cinepoly.common.repeatCollectionOnCreated

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bind = FragmentMainBinding.bind(view)

        loadKoinModules(mainModule)

        welcome(bind)
        apiHandling()

    }

    private fun welcome(bind: FragmentMainBinding){

        val token = viewModel.token.value

        viewModel.getUsername(token).observe(viewLifecycleOwner) {
            val username = it.fullname ?: it.username
            bind.tvWelcome.text = resources.getString(string.welcome, username)
        }

        val action =
            MainFragmentDirections.actionMainFragmentToProfileFragment()
        bind.ivProfile.setOnClickListener {
            findNavController().navigate(action)
        }
    }

    private fun apiHandling(){
        repeatCollectionOnCreated {
            viewModel.getNowPlaying().collect{
                when (it) {
                    is ApiResult.Error -> "Error"
                    is ApiResult.Success -> recView(it.data)
                    is ApiResult.Empty -> "Empty"
                }
            }
        }
    }

    private fun recView(list: List<MovieModel>) {
        val binding = view?.let { FragmentMainBinding.bind(it) }
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false) //(context, LinearLayoutManager.HORIZONTAL, false)
        val recView = binding?.rvMovies
        val adapter = MovieAdapter(list, object : MovieAdapter.MoviesListener{
            override fun onMovieClick(movie: MovieModel) {
                val action =
                    MainFragmentDirections.actionMainFragmentToDetailFragment(
                        movie.title,
                        movie.releaseDate,
                        movie.overview,
                        movie.posterPath
                    )
                findNavController().navigate(action)
            }
        })

        recView?.layoutManager = layoutManager
        recView?.adapter = adapter
    }
}