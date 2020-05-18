package com.example.fablixandroid;

import android.util.Log;
import android.widget.*;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SingleMovie extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlemovie);

        String url = "https://10.0.2.2:8443/fablix_pj_war_exploded/single-movie?id=";

        RequestQueue mQueue = Volley.newRequestQueue(this);
        url += String.valueOf(getIntent().getExtras().get("singleMovie"));

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("SingleMovie json", jsonArray.toString());
                        for(int i=0; i<jsonArray.length(); i++){
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("movie_id");
                                String title = jsonObject.getString("movie_title");
                                String year = jsonObject.getString("movie_year");
                                String director = jsonObject.getString("movie_director");
                                String genre = jsonObject.getString("movie_genres");
                                String[] genres = genre.split(",");
                                String starId = jsonObject.getString("star_id");
                                String[] starIdArray = starId.split(",");
                                String star = jsonObject.getString("movie_stars");
                                String[] stars = star.split(",");
                                String rating = jsonObject.getString("movie_rating");
                                updateScreen(title, year, director, genres, stars);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonArrayRequest);
    }

    private void updateScreen(String title, String year, String director, String[] genres, String[] stars){

        TextView titleView = findViewById(R.id.single_title);
        TextView yearView = findViewById(R.id.single_year);
        TextView directorView = findViewById(R.id.single_director);
        TextView genreView = findViewById(R.id.single_genres);
        TextView starView = findViewById(R.id.single_stars);

        titleView.setText(title);
        yearView.setText(year);
        directorView.setText(director);

        String genre_str = "";
        if(genres.length < 1){
            genre_str += "N/A";
        }else{
            for(int i=0; i<genres.length; i++){
                genre_str += genres[i];
                if(i < genres.length-1){
                    genre_str += ", ";
                }
            }
        }
        genreView.setText(genre_str);

        String star_str = "";
        if(stars.length < 1){
            star_str += "N/A";
        }else{
            for(int i=0; i<stars.length; i++){
                star_str += stars[i];
                if(i < stars.length-1){
                    star_str += "\n";
                }
            }
        }
        starView.setText(star_str);

    }
}
