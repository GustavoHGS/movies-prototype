package com.zup.movies.components.MovieDetails;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.zup.movies.MainActivity;
import com.zup.movies.R;
import com.zup.movies.model.Movie;
import com.zup.movies.services.Constants.GeneralConstants;
import com.zup.movies.services.MoviesManager.MoviesService;
import com.zup.movies.services.Utils.BitMapLruCache;

import org.json.JSONObject;

/**
 * Created by ghenrique on 01/06/16.
 */
public class MovieDetailsFragment extends Fragment {
    private View rootView;
    private String movieID;
    private MoviesService moviesService;
    private NetworkImageView movieThumbnail;
    private TextView movieTitle;
    private TextView movieYear;
    private TextView movieGenre;
    private TextView movieType;
    private TextView moviePlot;
    private TextView movieRuntime;
    private Button favoriteBtn;
    private Movie movie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_component_movie_details, container,false);

        getActionBar().setDisplayHomeAsUpEnabled(false);
        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setHasOptionsMenu(true);

        getMoviesService().getMoviesFromServerByID(
                getMovieID(),
                new MoviesService.VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        processResponse(result);
                    }
                });

        getFavoriteBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    movie.storeUserPicture(getContext());
                    movie.save();
                    Snackbar.make(v, R.string.db_flow_save_success, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Snackbar.make(v, R.string.db_flow_save_failure, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.global,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void processResponse(JSONObject result) {
        movie = new Movie();
        try {
            Log.d("OMDb  JSON: ",result.toString());
            movie.fullExtract(result);
            setUpMovieContents(movie);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpMovieContents(Movie movie){
        movieThumbnail = (NetworkImageView)getRootView().findViewById(R.id.movie_details_card_thumbnail);
        movieTitle = (TextView)getRootView().findViewById(R.id.movie_details_card_title);
        movieYear = (TextView)getRootView().findViewById(R.id.movie_details_card_year);
        movieGenre = (TextView)getRootView().findViewById(R.id.movie_details_card_genre);
        movieType = (TextView)getRootView().findViewById(R.id.movie_details_card_type);
        movieRuntime = (TextView)getRootView().findViewById(R.id.movie_details_card_time);
        moviePlot = (TextView)getRootView().findViewById(R.id.movie_details_card_description);

        ImageLoader.ImageCache imageCache = new BitMapLruCache();
        ImageLoader imageLoader = new ImageLoader(Volley.newRequestQueue(getContext()), imageCache);
        movieThumbnail.setImageUrl(movie.getPosterUrl(), imageLoader);

        movieTitle.setText(movie.getTitle());
        movieYear.setText(movie.getYear());
        movieGenre.setText(movie.getGenre());
        movieType.setText(movie.getType());
        moviePlot.setText(movie.getPlot());
        movieRuntime.setText(movie.getRuntime());


    }



    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    private String getMovieID(){
        if(movieID == null)
            movieID = getArguments().getString(GeneralConstants.HOME_MOVIE_ID);
        return movieID;
    }

    private MoviesService getMoviesService(){
        if(moviesService == null)
            moviesService = new MoviesService(getContext(),getRootView());
        return moviesService;
    }

    private View getRootView(){
        return this.rootView;
    }

    private Button getFavoriteBtn() {
        if(favoriteBtn == null)
            favoriteBtn = (Button) getRootView().findViewById(R.id.details_save_movie_btn);
        return favoriteBtn;
    }
}
