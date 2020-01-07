package com.example.movieapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.MovieAdapter;
import com.example.movieapp.MovieResponse;
import com.example.movieapp.R;
import com.example.movieapp.interfaces.MovieInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getCanonicalName();
    private HomeViewModel homeViewModel;
    private View rootView;
    String SEARCH_URL = "https://www.themoviedb.org/search";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // final TextView textView = root.findViewById(R.id.text_home);
        //  homeViewModel.getText().observe(this, new Observer<String>() {
        // @Override
        //public void onChanged(@Nullable String s) {
        //    textView.setText(s);
        //  }
        // });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MovieInterface service = retrofit.create(MovieInterface.class);

        final RecyclerView recyclerView = rootView.findViewById(R.id.movie_recyclerview);

        Call<MovieResponse> call = service.getMovies(MovieInterface.API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, final Response<MovieResponse> response) {
                if (response.body() == null || response.body().getList() == null) {
                    Log.d(TAG, "Success, empty list!");
                } else {
                    Log.d(TAG, "Success, Movie list size: " + response.body().getList().size());
                    recyclerView.setHasFixedSize(true);
                    GridLayoutManager layoutManager = new GridLayoutManager(rootView.getContext(), 1);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new MovieAdapter(response.body().getList()));

                }


            }





            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(TAG, "Fail to get movies", t);
            }
        });





    }
}