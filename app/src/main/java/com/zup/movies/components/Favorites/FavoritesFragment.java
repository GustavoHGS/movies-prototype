package com.zup.movies.components.Favorites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.zup.movies.MainActivity;
import com.zup.movies.R;
import com.zup.movies.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghenrique on 30/05/16.
 */
public class FavoritesFragment extends Fragment {

    private List<Movie> favoritesList;
    private RecyclerView favoritesRecyclerView;
    private View rootView;
    private FavoritesRecyclerAdapter favoritesRecyclerAdapter;
    private SwipeRefreshLayout swipeContainer;


    public static FavoritesFragment newInstance() {
        FavoritesFragment fragmentFirst = new FavoritesFragment();
        return fragmentFirst;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRootView(inflater.inflate(R.layout.fragment_component_favorites, container, false));

        favoritesList = new Select().from(Movie.class).queryList();

        getFavoritesRecyclerView().setAdapter(getFavoritesAdapter());
        setUpSwipeView();

        setHasOptionsMenu(true);
        return getRootView();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.global,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onResume() {
        MainActivity.toggle.setDrawerIndicatorEnabled(true);
        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        getActivity().findViewById(R.id.tabs).setVisibility(View.VISIBLE);
        super.onResume();

    }

    private void setUpSwipeView(){
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override

            public void onRefresh() {
                favoritesList = new Select().from(Movie.class).queryList();
                getFavoritesRecyclerView().setAdapter(createNewAdapter());
                getFavoritesAdapter().notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

        });
    }


    private RecyclerView getFavoritesRecyclerView(){
        if(favoritesRecyclerView == null)
            favoritesRecyclerView = (RecyclerView) getRootView().findViewById(R.id.favoritesList);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        favoritesRecyclerView.setLayoutManager(llm);

        return favoritesRecyclerView;
    }


    private void setRootView(View rootView){
        this.rootView = rootView;
    }
    private View getRootView(){
        return this.rootView;
    }

    private FavoritesRecyclerAdapter getFavoritesAdapter(){
        if(favoritesRecyclerAdapter == null)
            favoritesRecyclerAdapter = new FavoritesRecyclerAdapter(getFavoritesList(),FavoritesFragment.this.getContext());
        return favoritesRecyclerAdapter;
    }

    private List<Movie> getFavoritesList(){
        if(favoritesList == null)
            favoritesList = new ArrayList<Movie>();
        return favoritesList;
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
    private FavoritesRecyclerAdapter createNewAdapter(){
        favoritesRecyclerAdapter = new FavoritesRecyclerAdapter(getFavoritesList(),FavoritesFragment.this.getContext());
        return favoritesRecyclerAdapter;
    }


}
