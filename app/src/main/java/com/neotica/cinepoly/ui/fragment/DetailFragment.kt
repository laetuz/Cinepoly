package com.neotica.cinepoly.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.neotica.cinepoly.R
import com.neotica.cinepoly.databinding.DetailMovieBinding

class DetailFragment : Fragment(R.layout.detail_movie) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DetailMovieBinding.bind(view)

        val args = DetailFragmentArgs.fromBundle(arguments as Bundle)
        val poster = args.poster.toString()

        with(binding) {
            with(args) {
                tvTitle.text = title
                tvRelease.text = release
                tvDesc.text = desc
                ivBack.setOnClickListener {
                    NavHostFragment.findNavController(this@DetailFragment).navigateUp()
                }
            }

            Glide
                .with(this@DetailFragment)
                .load("https://www.themoviedb.org/t/p/w600_and_h900_bestv2${poster}")
                .into(ivPoster)
        }
    }
}