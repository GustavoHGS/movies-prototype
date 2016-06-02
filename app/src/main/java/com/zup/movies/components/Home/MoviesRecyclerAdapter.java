package com.zup.movies.components.Home;

import android.content.Context;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.zup.movies.R;
import com.zup.movies.model.Movie;
import com.zup.movies.services.Utils.BitMapLruCache;

import java.util.List;

/**
 * Created by ghenrique on 31/05/16.
 */

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MoviesViewHolder> {
    private List<Movie> movies;
    private Context context;
    static OnItemClickListener mItemClickListener;

    public MoviesRecyclerAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
    @Override
    public long getItemId(int pos){
        return super.getItemId(pos);
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder viewHolder, int i) {
        Movie movie = movies.get(i);
        viewHolder.title.setText(movie.getTitle());
        viewHolder.year.setText(movie.getYear());
        viewHolder.type.setText(movie.getType());

        ImageLoader.ImageCache imageCache = new BitMapLruCache();
        ImageLoader imageLoader = new ImageLoader(Volley.newRequestQueue(context), imageCache);
        viewHolder.thumbnail.setImageUrl(movie.getPosterUrl(), imageLoader);

    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.movies_list_item, viewGroup, false);
        return new MoviesViewHolder(itemView);
    }

    public static class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected NetworkImageView thumbnail;
        protected TextView title;
        protected TextView year;
        protected TextView type;
        protected Button details;

        public MoviesViewHolder(View v) {
            super(v);
            thumbnail =  (NetworkImageView) v.findViewById(R.id.movie_card_thumbnail);
            title = (TextView)  v.findViewById(R.id.movie_card_title);
            year = (TextView)  v.findViewById(R.id.movie_card_year);
            type = (TextView)  v.findViewById(R.id.movie_card_type);
            details = (Button) v.findViewById(R.id.movie_card_details_btn);
            details.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null && v instanceof Button) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


}