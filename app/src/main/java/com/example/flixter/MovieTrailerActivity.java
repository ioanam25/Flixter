package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.R;
import com.example.flixter.databinding.ActivityMovieDetailsBinding;
import com.example.flixter.databinding.ActivityMovieTrailerBinding;
import com.example.flixter.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieTrailerActivity extends YouTubeBaseActivity {
    private ActivityMovieTrailerBinding binding;
    public static final String VIDEO_PLAYING_URL = "https://api.themoviedb.org/3/movie/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieTrailerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // temporary test video id -- TODO replace with movie trailer video id
        Movie movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        AsyncHttpClient client = new AsyncHttpClient();
        String concat_url = VIDEO_PLAYING_URL + movie.getId().toString() + "/videos?api_key="+ getString(R.string.movie_api_key);
        Log.d("","");
        client.get(concat_url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    // Log.i(TAG, "Results: " + results.toString());
                    if(results.length() > 0)
                    {
                        String yt_id = results.getJSONObject(0).getString("key");
                        // call runvideo
                        runVideo(yt_id);
                    }
                    // movies.addAll(Movie.fromJsonArray(results));
                    // movieAdapter.notifyDataSetChanged();
                    // Log.i(TAG, "Movies: " + movies.size());

                }
                catch (JSONException e) {
                    Log.e("xx", "Hit json exception", e);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("x", "onFailure");
            }
        });
    }
    private void runVideo(String videoId) {
        binding.player.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }
}