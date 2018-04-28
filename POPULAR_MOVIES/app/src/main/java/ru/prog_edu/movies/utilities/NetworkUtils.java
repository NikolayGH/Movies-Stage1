package ru.prog_edu.movies.utilities;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private final static String MOVIES_BASE_URL = "https://api.themoviedb.org";
    private final static String PARAM_QUERY_API = "api_key";
    private final static String API_KEY = "091eb633efcd8f2811530d725b717336";

    public static URL buildMovieUrl(String searchParameter){
        Uri movieUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath("3")
                .appendPath("movie")
                .appendPath(searchParameter)
                .appendQueryParameter(PARAM_QUERY_API, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(movieUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
