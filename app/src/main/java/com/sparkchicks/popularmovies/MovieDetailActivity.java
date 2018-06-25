package com.sparkchicks.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.sparkchicks.popularmovies.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

  @BindView(R.id.iv_poster)
  ImageView mPoster;
  @BindView(R.id.tv_title)
  TextView mTitle;
  @BindView(R.id.tv_user_rating)
  TextView mUserRating;
  @BindView(R.id.tv_release_date)
  TextView mReleaseDate;
  @BindView(R.id.tv_description)
  TextView mDescription;

  private Movie mMovie;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);
    ButterKnife.bind(this);

    if (savedInstanceState == null || !savedInstanceState.containsKey(Constants.MOVIE_DETAILS_BUNDLE_KEY)) {
      Intent intent = getIntent();
      if (intent.hasExtra(Constants.MOVIE_EXTRA)) {
        mMovie = intent.getParcelableExtra(Constants.MOVIE_EXTRA);
        displayMovie();
      }
    } else {
      mMovie = savedInstanceState.getParcelable(Constants.MOVIE_DETAILS_BUNDLE_KEY);
      displayMovie();
    }
  }

  private void displayMovie() {
    String posterUrl = Constants.BASE_IMAGE_URL + Constants.IMAGE_SIZE + mMovie.getPosterPath();
    Picasso.with(this).load(posterUrl).into(mPoster);
    mTitle.setText(mMovie.getTitle());
    mUserRating.setText(mMovie.getUserRating());
    mReleaseDate.setText(mMovie.getReleaseDate().substring(0, 4));
    mDescription.setText(mMovie.getOverview());
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putParcelable(Constants.MOVIE_DETAILS_BUNDLE_KEY, mMovie);
    super.onSaveInstanceState(outState);
  }
}
