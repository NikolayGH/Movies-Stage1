package ru.prog_edu.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsMovieActivity extends AppCompatActivity {




    private TextView originalTitleTextView;
    private TextView synopsisTextView;
    private TextView releaseDateTextView;
    private TextView userRatingTextView;
    private ImageView moviePosterImageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_movie_activity);
        final String mainImageUrl = "http://image.tmdb.org/t/p/w185/";

        Movie movie = getIntent().getParcelableExtra(
                Movie.class.getCanonicalName());

        initViews();

        String imageURL = mainImageUrl+movie.getPosterPath();

        Picasso.get()
                .load(imageURL)
                .into(moviePosterImageView);

        originalTitleTextView.setText(movie.getTitle());
        synopsisTextView.setText(movie.getOverview());
        releaseDateTextView.setText(movie.getReleaseDate());
        userRatingTextView.setText(String.valueOf(movie.getVoteAverage()));

    }

    private void initViews() {
        originalTitleTextView = findViewById(R.id.original_title);
        synopsisTextView = findViewById(R.id.synopsis);
        releaseDateTextView = findViewById(R.id.release_date);
        userRatingTextView = findViewById(R.id.user_rating);
        moviePosterImageView = findViewById(R.id.movie_poster);
    }
}
