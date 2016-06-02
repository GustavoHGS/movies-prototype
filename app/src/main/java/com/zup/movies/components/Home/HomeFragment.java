package com.zup.movies.components.Home;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.zup.movies.R;
import com.zup.movies.components.MovieDetails.MovieDetailsFragment;
import com.zup.movies.model.Movie;
import com.zup.movies.services.Constants.GeneralConstants;
import com.zup.movies.services.MoviesManager.MoviesService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ghenrique on 30/05/16.
 */
public class HomeFragment extends Fragment {
    private SearchView searchView;
    private MoviesService moviesService;
    private ArrayList<Movie> moviesList;
    private RecyclerView moviesRecyclerView;
    private View rootView;
    private MoviesRecyclerAdapter moviesRecyclerAdapter;
    private SwipeRefreshLayout swipeContainer;

    public static HomeFragment newInstance() {
        HomeFragment fragmentFirst = new HomeFragment();
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
        setRootView(inflater.inflate(R.layout.fragment_component_home, container, false));


        getMoviesAdapter().SetOnItemClickListener(new MoviesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callDetailsFragment(position);
            }
        });
        getMoviesRecyclerView().setAdapter(getMoviesAdapter());


        setUpSwipeView();
        return getRootView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        final MenuItem myActionMenuItem = menu.findItem(R.id.search_menu);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SearchOnQueryTextSubmit", query);
                getMoviesByTitle(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                Log.d("SearchOnQueryTextChange", query);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);


    }

    private void setUpSwipeView(){
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainerHome);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {
                if(searchView!= null && !searchView.getQuery().toString().isEmpty())
                    getMoviesByTitle(searchView.getQuery().toString());
                else
                    swipeContainer.setRefreshing(false);
            }

        });
    }

    private void callDetailsFragment(int position){
        Bundle args = new Bundle();
        args.putString(GeneralConstants.HOME_MOVIE_ID,getMoviesList().get(position).getId());
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(GeneralConstants.FAVORITES_FRAGMENT)
                .add(R.id.fragment_container, fragment).commit();
        getActivity().findViewById(R.id.tabs).setVisibility(View.GONE);
        getActivity().findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
    }

    private void getMoviesByTitle(String query){
        if(moviesService == null)
            moviesService = new MoviesService(getContext(),getRootView());

        moviesService.getMoviesFromServerByTitle(
                query,
                new MoviesService.VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        processResponse(result);
                    }
                });
    }

    private void processResponse(JSONObject result) {
        Movie movie;
        getMoviesList().clear();
        getMoviesAdapter().notifyDataSetChanged();
        try {
            if(result.getString("Response").equals("False")) {
                Snackbar.make(getRootView(), R.string.volley_error_not_found, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }
            Log.d("OMDb  JSON: ",result.toString());
            JSONArray array  = result.getJSONArray("Search");
            for(int i=0;i<array.length();i++) {
                movie = new Movie();
                movie.shortExtract(array.getJSONObject(i));
                getMoviesList().add(0,movie);
            }
            getMoviesAdapter().notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
        }catch (Exception e) {
            e.printStackTrace();
            swipeContainer.setRefreshing(false);
        }

    }



    /*
     |
     | Getters and Setters
     |
     */



    private RecyclerView getMoviesRecyclerView(){
        if(moviesRecyclerView == null)
            moviesRecyclerView = (RecyclerView) getRootView().findViewById(R.id.moviesList);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        moviesRecyclerView.setLayoutManager(llm);

        return moviesRecyclerView;
    }


    private void setRootView(View rootView){
        this.rootView = rootView;
    }
    private View getRootView(){
        return this.rootView;
    }

    private MoviesRecyclerAdapter getMoviesAdapter(){
        if(moviesRecyclerAdapter == null)
            moviesRecyclerAdapter = new MoviesRecyclerAdapter(getMoviesList(),HomeFragment.this.getContext());
        return moviesRecyclerAdapter;
    }

    private ArrayList<Movie> getMoviesList(){
        if(moviesList == null)
            moviesList = new ArrayList<Movie>();
        return moviesList;
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }


}

