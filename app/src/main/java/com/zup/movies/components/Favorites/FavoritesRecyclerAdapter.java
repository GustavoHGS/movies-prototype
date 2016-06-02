package com.zup.movies.components.Favorites;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.zup.movies.R;
import com.zup.movies.model.Movie;
import com.zup.movies.services.FavoritesManger.FavoritesDAOUtils;


import java.io.File;
import java.util.List;

/**
 * Created by ghenrique on 02/06/16.
 */
public class FavoritesRecyclerAdapter  extends RecyclerView.Adapter<FavoritesRecyclerAdapter.FavoritesViewHoler> {
    private List<Movie> movies;
    private Context context;
    static OnItemClickListener mItemClickListener;

    public FavoritesRecyclerAdapter(List<Movie> movies, Context context) {
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
    public void onBindViewHolder(FavoritesViewHoler viewHolder, int i) {
        Movie movie = movies.get(i);
        viewHolder.movieTitle.setText(movie.getTitle());
        viewHolder.movieYear.setText(movie.getYear());
        viewHolder.movieGenre.setText(movie.getGenre());
        viewHolder.movieType.setText(movie.getType());
        viewHolder.moviePlot.setText(movie.getPlot());
        viewHolder.movieRuntime.setText(movie.getRuntime());

        FavoritesDAOUtils favoritesDAOUtils = new FavoritesDAOUtils(context);
        try {
            viewHolder.thumbnail.setImageBitmap(favoritesDAOUtils.getImageFromStorage(movie));

        }catch (Exception e)
        {
            e.printStackTrace();
            viewHolder.thumbnail.setImageResource(R.drawable.ic_broken_image);
        }

    }

    @Override
    public FavoritesViewHoler onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.favorites_list_item, viewGroup, false);
        return new FavoritesViewHoler(itemView);
    }

    public static class FavoritesViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView thumbnail;
        protected TextView movieTitle;
        protected TextView movieYear;
        protected TextView movieGenre;
        protected TextView movieType;
        protected TextView moviePlot;
        protected TextView movieRuntime;

        public FavoritesViewHoler(View v) {
            super(v);
            thumbnail =  (ImageView) v.findViewById(R.id.favorites_card_thumbnail);
            movieTitle = (TextView)  v.findViewById(R.id.favorites_card_title);
            movieYear = (TextView)  v.findViewById(R.id.favorites_card_year);
            movieGenre = (TextView)  v.findViewById(R.id.favorites_card_year);
            movieType = (TextView)  v.findViewById(R.id.favorites_card_type);
            moviePlot = (TextView)  v.findViewById(R.id.favorites_card_description);
            movieRuntime = (TextView)  v.findViewById(R.id.favorites_card_time);

            v.setOnClickListener(this);

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