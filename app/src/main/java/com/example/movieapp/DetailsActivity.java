package com.example.movieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.movieapp.database.DatabaseHelper;

public class DetailsActivity extends AppCompatActivity {


    TextView title, popularity, language, average, voteCount, overview;
    String mTitle, mLanguage, mOverview, mImage;
    Float mPopularity;
    Double mVoteCount;
    Double mAverage;
    ImageView image, star;
    private final AppCompatActivity activity = DetailsActivity.this;
    public static final String SHARED_PREFS = "sharedPrefs";
    private DatabaseHelper db;
    private Movie favorite;
    private String user;
    String baseImageUrl = "https://image.tmdb.org/t/p/w500";
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        db = new DatabaseHelper(activity);
        title = findViewById(R.id.title);
        popularity = findViewById(R.id.popularity);
        language = findViewById(R.id.language);
        average = findViewById(R.id.average);
        voteCount = findViewById(R.id.vote);
        overview = findViewById(R.id.overview);
        image = findViewById(R.id.image);
        star = findViewById(R.id.star);

        mTitle = getIntent().getStringExtra("title");
        mPopularity = getIntent().getFloatExtra("popularity", 0);
        mLanguage = getIntent().getStringExtra("originallanguage");
        mAverage = getIntent().getDoubleExtra("average", 0);
        mVoteCount = getIntent().getDoubleExtra("vote", 0);
        mOverview = getIntent().getStringExtra("overview");
        mImage = getIntent().getStringExtra("posterpath");

        String url = baseImageUrl + mImage;

        Glide.with(this).load(url).placeholder(R.drawable.baseline_star_black_18dp).into(image);

        title.setText(mTitle);
        popularity.setText(mPopularity.toString());
        language.setText(mLanguage);
        average.setText(mAverage.toString());
        voteCount.setText(mVoteCount.toString());
        overview.setText(mOverview);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        user = sharedPreferences.getString("username", "");

        isFavorite();

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    deleteFavorite(user);
                } else {
                    saveFavorite(user);
                }
                isFavorite = !isFavorite;
                setFavoriteState();
            }
        });

    }

    public void saveFavorite(String user) {
        favorite = new Movie();
        int movie_id = getIntent().getExtras().getInt("id");

        favorite.setId(movie_id);
        favorite.setTitle(mTitle);
        favorite.setPoster_path(mImage);
        favorite.setPopularity(mPopularity);
        favorite.setOverview(mOverview);

        db.addFavorite(favorite, user);
    }

    public void deleteFavorite(String user) {
        int movie_id = getIntent().getExtras().getInt("id");
        db.deleteFavourite(movie_id, user);
    }

    private void isFavorite() {
        int movie_id = getIntent().getExtras().getInt("id");
        isFavorite = db.isFavorite(movie_id, user);
        setFavoriteState();
    }

    private void setFavoriteState() {
        star.setBackgroundColor(isFavorite ? Color.YELLOW : Color.TRANSPARENT);
    }

}
