package com.zup.movies.services.MoviesManager;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.zup.movies.R;
import com.zup.movies.services.Constants.UrlConstants;
import com.zup.movies.services.VolleyRequest.CustomJSONObjectRequest;
import com.zup.movies.services.VolleyRequest.CustomVolleyRequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ghenrique on 31/05/16.
 */
public class MoviesService implements Response.ErrorListener{
    private RequestQueue mQueue;
    private View view;

    public MoviesService(Context context, View view){
        mQueue = CustomVolleyRequestQueue.prepareInstance(context).getRequestQueue();
        this.view = view;
    }


    public void getMoviesFromServerByTitle(String title,final VolleyCallback callback){
        title = title.replaceAll(" ","+");
        final String url = UrlConstants.OMDB_API_URL_SEARCH
                .replace(UrlConstants.MOVIE_TITLE,title)
                .replace(UrlConstants.MOVIE_YEAR,"");

        Log.d("URL A SER NO OMBD", url);
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                MoviesService.this
        );
        jsonObjectRequest.setTag(title);
        mQueue.add(jsonObjectRequest);

    }

    public void getMoviesFromServerByID(String id,final VolleyCallback callback){
        id = id.replaceAll(" ","+");
        final String url = UrlConstants.OMDB_API_URL_ID
                .replace(UrlConstants.MOVIE_ID,id);

        Log.d("URL A SER NO OMBD", url);
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                MoviesService.this
        );
        jsonObjectRequest.setTag(id);
        mQueue.add(jsonObjectRequest);

    }


    public interface VolleyCallback{
        void onSuccess(JSONObject result);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("Volley error! " + error.getMessage());
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Snackbar.make(view, R.string.volley_timeout_error, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (error instanceof ServerError) {
            Snackbar.make(view, R.string.volley_error_not_found, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (error instanceof NetworkError) {
            Snackbar.make(view, R.string.volley_connection_error, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (error instanceof ParseError) {
            Snackbar.make(view,R.string.volley_parse_error, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}
