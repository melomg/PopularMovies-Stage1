package com.projects.melih.popularmovies.ui.movielist;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.components.GridAutoFitLayoutManager;
import com.projects.melih.popularmovies.databinding.FragmentMovieListBinding;
import com.projects.melih.popularmovies.ui.base.BaseFragment;
import com.projects.melih.popularmovies.ui.base.EndlessRecyclerViewScrollListener;
import com.projects.melih.popularmovies.ui.moviedetail.MovieDetailFragment;

/**
 * Created by Melih Gültekin on 1.03.2018
 */

abstract class BaseMovieListFragment extends BaseFragment {
    @SuppressWarnings("WeakerAccess")
    protected FragmentMovieListBinding binding;
    @SuppressWarnings("WeakerAccess")
    protected MovieListAdapter adapter;
    @SuppressWarnings("WeakerAccess")
    protected EndlessRecyclerViewScrollListener scrollListener;

    protected abstract void onLoadMore();

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false);

        binding.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        adapter = new MovieListAdapter(context, movie -> navigationListener.addFragment(MovieDetailFragment.newInstance(movie)));

        GridAutoFitLayoutManager layoutManager = new GridAutoFitLayoutManager(context, R.dimen.list_item_width);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView recyclerView) {
                BaseMovieListFragment.this.onLoadMore();
            }
        };

        binding.recyclerView.addOnScrollListener(scrollListener);
        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.recyclerView.removeOnScrollListener(scrollListener);
    }

    @SuppressWarnings("WeakerAccess")
    protected void onRefreshListener() {
        adapter.submitMovies(null);
        scrollListener.resetState();
    }
}