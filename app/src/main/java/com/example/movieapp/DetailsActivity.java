package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {


    TextView title,popularity,language,average,voteCount,overview;
    String mTitle,mLanguage,mOverview,mImage;
    Float mPopularity;
    Double mVoteCount;
    Double mAverage;
    ImageView image;
    String baseImageUrl="https://image.tmdb.org/t/p/w500";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        title = findViewById(R.id.title);
        popularity = findViewById(R.id.popularity);
        language = findViewById(R.id.language);
        average = findViewById(R.id.average);
        voteCount = findViewById(R.id.vote);
        overview = findViewById(R.id.overview);
        image = findViewById(R.id.image);

        mTitle = getIntent().getStringExtra("title");
        mPopularity = getIntent().getFloatExtra("popularity", 0);
        mLanguage = getIntent().getStringExtra("originallanguage");
        mAverage = getIntent().getDoubleExtra("average",0);
        mVoteCount = getIntent().getDoubleExtra("vote",0);
        mOverview = getIntent().getStringExtra("overview");
        mImage = getIntent().getStringExtra("posterpath");

        String url = baseImageUrl+mImage;

        Glide.with(this).load(url).placeholder(R.drawable.baseline_star_black_18dp).into(image);

        title.setText(mTitle);
        popularity.setText(mPopularity.toString());
        language.setText(mLanguage);
        average.setText(mAverage.toString());
        voteCount.setText(mVoteCount.toString());
        overview.setText(mOverview);

    }
}
