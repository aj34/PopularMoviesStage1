package com.sparkchicks.popularmovies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.sparkchicks.popularmovies.MovieAdapter.MovieAdapterOnClickHandler;
import com.sparkchicks.popularmovies.model.MovieResponse;
import com.sparkchicks.popularmovies.network.GetMovieService;
import com.sparkchicks.popularmovies.network.RetrofitClientInstance;
import com.sparkchicks.popularmovies.model.Movie;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler, AdapterView.OnItemSelectedListener {

    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.tv_error_message)
    TextView mErrorMessage;
    @BindView(R.id.pb_loading)
    ProgressBar mProgress;
    @BindView(R.id.rv_movie_posters)
    RecyclerView mRecyclerView;

    private MovieAdapter mMovieAdapter;
    private ArrayList<com.sparkchicks.popularmovies.model.Movie> movies;

    private int mLastSpinnerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mMovieAdapter = new MovieAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.hasFixedSize();
        mRecyclerView.setAdapter(mMovieAdapter);

        if (savedInstanceState == null || !savedInstanceState.containsKey(Constants.MOVIE_BUNDLE_KEY)) {
            loadMovies(Constants.POPULAR);
        } else {
            movies = savedInstanceState.getParcelableArrayList(Constants.MOVIE_BUNDLE_KEY);
            mMovieAdapter.setMoviesData(movies);
            mLastSpinnerPosition = savedInstanceState.getInt(Constants.SPINNER_POSITION_BUNDLE_KEY);
            showMovies();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        state.putParcelableArrayList(Constants.MOVIE_BUNDLE_KEY, movies);
        state.putInt(Constants.SPINNER_POSITION_BUNDLE_KEY, mLastSpinnerPosition);
        super.onSaveInstanceState(state);
    }

    private void loadMovies(String sortBy) {
        mProgress.setVisibility(View.VISIBLE);
        GetMovieService service = RetrofitClientInstance.getRetrofitInstance().create(GetMovieService.class);

        Call<MovieResponse> call = service.getMovies(sortBy, BuildConfig.TMDB_API_KEY);
        call.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                Log.d(TAG, "Retrofit response: " + response.body().toString());
                movies = response.body().getResults();
                mMovieAdapter.setMoviesData(movies);
                showMovies();
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                showErrorMessage(MainActivity.this.getResources().getString(R.string.error_message));
            }
        });
    }

    private void showMovies() {
        mProgress.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String errorMessage) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.INVISIBLE);
        mErrorMessage.setText(errorMessage);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Constants.MOVIE_EXTRA, movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sortByArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapter);
        spinner.setSelection(mLastSpinnerPosition, false);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();

        if (mLastSpinnerPosition == position) {
            return; // do nothing
        }

        switch (selectedItem) {
            case "Top Rated":
                loadMovies(Constants.TOP_RATED);
                break;
            case "Popular":
                loadMovies(Constants.POPULAR);
                break;
        }

        mLastSpinnerPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}
