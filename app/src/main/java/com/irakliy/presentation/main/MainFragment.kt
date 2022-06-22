package com.irakliy.presentation.main

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.irakliy.databinding.FragmentMainBinding
import com.irakliy.util.RxSearchObservable
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    private val adapter = MovieAdapter()

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchView()

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(context, getSpanCount(), RecyclerView.VERTICAL, false)

        viewModel.getMovies.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.setProgress.observe(viewLifecycleOwner) { isVisible ->
            binding.progressHorizontal.visibility = if (isVisible) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        viewModel.loadMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSearchView() {
        RxSearchObservable.fromView(binding.searchView)
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.computation())
            .subscribe({ query ->
                val newList = viewModel.srcList?.filter {
                    it.title.contains(query, true) || it.subtitle.contains(query, true)
                }
                binding.root.post {
                    adapter.submitList(newList)
                }
            }, {
                Log.e(TAG, it.message.toString())
            })

    }

    private fun getSpanCount(): Int {
        val orientation = resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            4
        } else {
            2
        }
    }

    companion object {
        const val TAG = "MainFragment"
    }

}