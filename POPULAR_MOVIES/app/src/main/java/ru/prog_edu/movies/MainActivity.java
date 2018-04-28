package ru.prog_edu.movies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import ru.prog_edu.movies.utilities.NetworkUtils;

/* Проект реализован в рамках обучения на  Android Developer Nanodegree Programm (Udacity)
   c учетом возможной отправки в школу мобильной разработки (Yandex).
   Так как опыт в разработке небольшой, сознательно ушел от применения сторонних библиотек (Retrofit)
   с целью освоения особенностей клиент-серверного взаимодействия (вначале использовал просто класс AsyncTask,
   затем для предотвращения вызова повторных потоков при смене конфигурации применялся  AsyncTaskLoader. */


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, MoviesAdapter.OnSelectedItemListener{

    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private RecyclerView movieRecycler;
    private final static String POPULAR_MOVIES_PARAMETER = "popular";
    private final static String TOP_RATED_MOVIES_PARAMETER = "top_rated";
    private final static int ID_LOADER = 18;
    private ArrayList<Movie> movies;
    private final ArrayList<String> imagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isOnline();

        movieRecycler = findViewById(R.id.images_recycler_view);

        getSupportLoaderManager().initLoader(ID_LOADER, null, this);

        if(isOnline()) {

            makeMoviesQuery(TOP_RATED_MOVIES_PARAMETER);

            movieRecycler.setHasFixedSize(true);
            movieRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        }else{
            Toast.makeText(this, "NO INTERNET CONNECTION!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void makeMoviesQuery(String searchParameter){
        URL moviesQueryUrl = NetworkUtils.buildMovieUrl(searchParameter);
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, moviesQueryUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> moviesLoader = loaderManager.getLoader(ID_LOADER);

        if (moviesLoader == null) {
            loaderManager.initLoader(ID_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(ID_LOADER, queryBundle, this);
        }
    }

    @NonNull
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String myMoviesJson;
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }
                if (myMoviesJson != null) {
                    deliverResult(myMoviesJson);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                String searchQueryUrlString = args.getString(SEARCH_QUERY_URL_EXTRA);
                if (searchQueryUrlString == null || TextUtils.isEmpty(searchQueryUrlString)) {
                    return null;
                }
                try {
                    URL movUrl = new URL(searchQueryUrlString);
                    return  NetworkUtils.getResponseFromHttpUrl(movUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    return  null;
                }
            }

            @Override
            public void deliverResult(String githubJson) {
                myMoviesJson = githubJson;
                super.deliverResult(githubJson);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        if(data != null&& !data.equals("")){

            try {
                JSONObject moviesResponse = new JSONObject(data);
                JSONArray arrayMovies = moviesResponse.getJSONArray("results");

                movies = new ArrayList<>();

                for (int i = 0; i < arrayMovies.length(); i++) {
                    movies.add(new Movie());
                    movies.get(i).setPosterPath(arrayMovies.getJSONObject(i).getString("poster_path"));
                    movies.get(i).setTitle(arrayMovies.getJSONObject(i).getString("title"));
                    movies.get(i).setReleaseDate(arrayMovies.getJSONObject(i).getString("release_date"));
                    movies.get(i).setVoteAverage(arrayMovies.getJSONObject(i).getDouble("vote_average"));
                    movies.get(i).setOverview(arrayMovies.getJSONObject(i).getString("overview"));
                    imagesList.add(arrayMovies.getJSONObject(i).getString("poster_path"));
                }

                Log.d(" arrayMovies", Integer.toString(movies.size()));
                Log.d(" Movie first ID - ", movies.get(0).getTitle());
                RecyclerView.Adapter imageAdapter;
                imageAdapter = new MoviesAdapter(movies, this);
                movieRecycler.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }


    @Override
    public void onListItemClick(int selectedMovie) {

        Log.i("Activity clicked", Integer.toString(selectedMovie));

        Movie movie =  movies.get(selectedMovie);

        Intent intent = new Intent(this, DetailsMovieActivity.class);
        intent.putExtra(Movie.class.getCanonicalName(), movie);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id){
            case R.id.top_rated:
                makeMoviesQuery(TOP_RATED_MOVIES_PARAMETER);
                movieRecycler.setHasFixedSize(true);
                movieRecycler.setLayoutManager(new GridLayoutManager(this, 2));
                return true;
            case R.id.most_popular:
                makeMoviesQuery(POPULAR_MOVIES_PARAMETER);
                movieRecycler.setHasFixedSize(true);
                movieRecycler.setLayoutManager(new GridLayoutManager(this, 2));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //метод для проверки соединения
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
