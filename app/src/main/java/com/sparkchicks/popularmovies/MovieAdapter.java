package com.sparkchicks.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sparkchicks.popularmovies.model.Movie;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ArrayList<Movie> mMovies;
    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movie_list_item, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        String posterUrl = Constants.BASE_IMAGE_URL + Constants.IMAGE_SIZE + mMovies.get(position).getPosterPath();
        Picasso.with(holder.mMovieImageView.getContext()).load(posterUrl).into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) { return 0; }
        return mMovies.size();
    }

    public void setMoviesData(ArrayList<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }


    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final ImageView mMovieImageView;

        MovieAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = view.findViewById(R.id.movie_list_iv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovies.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }
}
