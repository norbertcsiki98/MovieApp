package com.example.movieapp.ui.favourite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.Movie;
import com.example.movieapp.MovieAdapter;
import com.example.movieapp.R;
import com.example.movieapp.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private Context context;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    private DatabaseHelper db;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOG_TAG = MovieAdapter.class.getName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = root.findViewById(R.id.movie_recyclerview);


        movieList = new ArrayList<>();
        /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
        adapter = new MovieAdapter(movieList);
        //recyclerView.setLayoutManager(new GridLayoutManager(context,4));
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        db = new DatabaseHelper(getActivity());
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("username", "");
        getAllFavorite(user);


        return root;
    }

    private void getAllFavorite(final String user) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                movieList.clear();
                movieList.addAll(db.getAllFavorite(user));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }


}